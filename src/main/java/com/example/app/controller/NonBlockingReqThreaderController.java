package com.example.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ForkJoinPool;

@RestController
public class NonBlockingReqThreaderController {
    Logger LOG = LoggerFactory.getLogger(getClass());

    /**
     * Request processing is done in a separate thread and once completed we invoke the setResult operation on the DeferredResult object.
     *
     * @param data
     * @return
     */
    @GetMapping("/async-deferredresult")
    public DeferredResult<ResponseEntity<?>> handleReqDefResult(String data) {
        LOG.info("Received async-deferredresult request started");
        DeferredResult<ResponseEntity<?>> output = new DeferredResult<>();
        output.onCompletion(() -> {
            LOG.info(Thread.currentThread().toString());
            LOG.info("Processing complete");
        });
        ForkJoinPool.commonPool().submit(() -> {
            LOG.info("Processing in separate thread started");
            try {
                Thread.sleep(6000);
                LOG.info("Processing in separate thread done");
            } catch (InterruptedException e) {
            }

            output.setResult(ResponseEntity.ok("ok"));
        });
        LOG.info("Received async-deferredresult request completed");
        return output;
    }

    /**
     * Let's trigger a timeout error by processing a request that takes more than the defined timeout values of 5 seconds:
     * @param model
     * @return
     */
    @GetMapping("/async-deferredresult-timeout")
    public DeferredResult<ResponseEntity<?>> handleReqWithTimeouts(String model) {
        LOG.info("Received async request with a configured timeout");
        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>(500l);
        deferredResult.onTimeout(new Runnable() {
            @Override
            public void run() {
                deferredResult.setErrorResult(
                        ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Request timeout occurred."));
            }
        });
        ForkJoinPool.commonPool().submit(() -> {
            LOG.info("Processing in separate thread");
            try {
                Thread.sleep(700l);
                deferredResult.setResult(ResponseEntity.ok("ok"));
            } catch (InterruptedException e) {
                LOG.info("Request processing interrupted");
                deferredResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("INTERNAL_SERVER_ERROR occurred."));
            }

        });
        LOG.info("servlet thread freed");
        return deferredResult;
    }

    /**
     * There will be scenarios where long-running computation fails due to some error or exception.
     * In this case, we can also register an onError() callback:
     * @param model
     * @return
     */
    @GetMapping("/async-deferredresult-failed")
    public DeferredResult<ResponseEntity<?>> handleAsyncFailedRequest(String model) {
        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();
        ForkJoinPool.commonPool().submit(() -> {
            try {
                // Exception occurred in processing
                throw new Exception();
            } catch (Exception e) {
                LOG.info("Request processing failed");
                deferredResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("INTERNAL_SERVER_ERROR occurred."));
            }
        });
        return deferredResult;
    }

}

