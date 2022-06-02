package com.example.app;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@org.springframework.context.annotation.Configuration
public class Configuration {

    public static final int ASSIGN_GATEWAY_TASK_EXECUTOR_CORE_POOL_SIZE = 5;
    public static final int ASSIGN_GATEWAY_TASK_EXECUTOR_MAX_POOL_SIZE = 25;
    public static final int ASSIGN_GATEWAY_TASK_EXECUTOR_QUEUE_CAPACITY = 100;

    @Bean
    public ThreadPoolTaskExecutor inputThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(ASSIGN_GATEWAY_TASK_EXECUTOR_CORE_POOL_SIZE);
        executor.setMaxPoolSize(ASSIGN_GATEWAY_TASK_EXECUTOR_MAX_POOL_SIZE);
        executor.setQueueCapacity(ASSIGN_GATEWAY_TASK_EXECUTOR_QUEUE_CAPACITY);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }

    @Bean
    public MessageChannel inputMessageChannel() {
        return new ExecutorChannel(inputThreadPoolTaskExecutor());
    }

    @Bean
    public ThreadPoolTaskExecutor outputThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(ASSIGN_GATEWAY_TASK_EXECUTOR_CORE_POOL_SIZE);
        executor.setMaxPoolSize(ASSIGN_GATEWAY_TASK_EXECUTOR_MAX_POOL_SIZE);
        executor.setQueueCapacity(ASSIGN_GATEWAY_TASK_EXECUTOR_QUEUE_CAPACITY);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }

    @Bean
    public ThreadPoolTaskExecutor routingThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(ASSIGN_GATEWAY_TASK_EXECUTOR_CORE_POOL_SIZE);
        executor.setMaxPoolSize(ASSIGN_GATEWAY_TASK_EXECUTOR_MAX_POOL_SIZE);
        executor.setQueueCapacity(ASSIGN_GATEWAY_TASK_EXECUTOR_QUEUE_CAPACITY);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }

    @Bean
    public MessageChannel outputMessageChannel() {
        return new ExecutorChannel(outputThreadPoolTaskExecutor());
    }

    @Bean
    public MessageChannel routingChannel() {
        return new ExecutorChannel(routingThreadPoolTaskExecutor());
    }
}
