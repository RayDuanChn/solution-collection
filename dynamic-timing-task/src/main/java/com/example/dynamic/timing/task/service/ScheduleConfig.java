package com.example.dynamic.timing.task.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import static org.hibernate.bytecode.BytecodeLogger.LOGGER;

/**
 * @author leiduanchn
 * @create 2020-02-07 11:54 a.m.
 */

/**
 * 使用@EnableScheduling和@EnableAsync，然后在任务上用@Async("xxx")指定线程池，taskRegistrar添加不添加线程池无影响，
 * 会使用指定的，不会用registrar里添加的，这时候执行定时任务，会使用各自指定的线程池,多个任务（非一个线程池）并行执行
 */
@EnableAsync
@Configuration
@EnableScheduling
public class ScheduleConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    }

    @Bean("aaa")
    public TaskScheduler a() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("a");
        scheduler.setPoolSize(3);
        scheduler.initialize();
        return scheduler;
    }

    @Bean("bbb")
    public TaskScheduler fileProcessScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("b");
        scheduler.setPoolSize(2);
        scheduler.initialize();
        return scheduler;
    }

    @Async("aaa")
    @Scheduled(fixedRate = 500)
    public void scheduled() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info(Thread.currentThread().getName() + " -- aaa");
    }

    @Async("bbb")
    @Scheduled(fixedDelay = 1000)
    public void fileProcessScheduled() {
        LOGGER.info(Thread.currentThread().getName() + " -- bbb");
    }
}


