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
 * 如果同时开启了@EnableAsync和使用了@Async，便变成了按配置时间(T)，每T执行一次任务。如果任务执行时间大于T，
 * 则会选取线程池中其他的线程去执行任务，如果没有剩余线程，则会等待有线程空闲出来。
 */
@EnableAsync
@Configuration
public class ScheduleConfig1 implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler( scheduler());
    }

    @Bean("aaa")
    public TaskScheduler   scheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("a");
        scheduler.setPoolSize(4);
        scheduler.initialize();
        return scheduler;
    }

    @Async("aaa")
    @Scheduled(fixedRate = 500)
    public void scheduled(){
        LOGGER.info(Thread.currentThread().getName());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();

        }

    }
}

