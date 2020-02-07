package com.example.dynamic.timing.task.service;

import com.example.dynamic.timing.task.repository.CronRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author leiduanchn
 * @create 2020-02-06 4:08 p.m.
 */

/**
 1. 使用@EnableScheduling，同时不使用@EnableAsync,然后在taskRegistrar添加指定线程池，这时候执行定时任务，
 会使用我们添加的线程池,多个任务同步执行
 */
@Configuration
@EnableScheduling
public class DynamicTimingAsyncTask implements SchedulingConfigurer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static String cron;

    @Autowired
    private CronRepository repository;

    @Async
    public Runnable scheduledTask(){
        //项目部署时，会在这里执行一次，从数据库（文件等）拿到cron表达式
        cron = repository.findById(0).get().getCron();

        Runnable task = new Runnable() {
            @Override
            public void run() {
                //任务逻辑代码部分.
                logger.info("DynamicTimingAsyncTask is running..." + LocalDateTime.now());
            }
        };
        return task;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler(scheduler());
        Runnable task = scheduledTask();

        Trigger trigger = triggerContext -> {
            //任务触发，可修改任务的执行周期.
            //每一次任务触发，都会执行这里的方法一次，重新获取下一次的执行时间
            cron = repository.findById(0).get().getCron();
            CronTrigger trigger1 = new CronTrigger(cron);
            //下一次任务生效
            Date nextExec = trigger1.nextExecutionTime(triggerContext);
            return nextExec;
        };
        //传入task 和 trigger， 每一次执行任务都会触发trigger（先执行task，后trigger）
        taskRegistrar.addTriggerTask(task, trigger);
    }

    @Bean
    public TaskScheduler scheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("a");
        scheduler.setPoolSize(3);
        scheduler.initialize();
        return scheduler;
    }

}
