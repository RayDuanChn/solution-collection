package com.example.dynamic.timing.task.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Dynamic timing task
 * wiz://open_document?guid=55a60c2a-0ee0-41d5-9916-7cf3a70ed9ec&kbguid=&private_kbguid=d1140c4e-30fb-4957-b06a-18c38a93cded
 *
 * @author leiduanchn
 * @create 2020-02-06 3:30 p.m.
 */
@EnableScheduling
@Component
public class DynamicScheduledService  implements SchedulingConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(DynamicScheduledService.class);
    private static String cron;

    public DynamicScheduledService() {
        //项目部署时，会在这里执行一次，从数据库拿到cron表达式
        cron = "0/5 * * * * ?";
        //cron = timerQueryMapper.getCronTime();

        // 开启新线程模拟外部更改了任务执行周期
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(15 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                cron = "0/10 * * * * ?";
                System.err.println("cron change to: " + cron);
            }
        }).start();
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(new Runnable() {
            @Override
            public void run() {
                // 任务逻辑
                logger.info(LocalDateTime.now() + " -- dynamicCronTask is running...");
            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                // 任务触发，可修改任务的执行周期
                CronTrigger trigger = new CronTrigger(cron);
                Date nextExec = trigger.nextExecutionTime(triggerContext);
                return nextExec;
            }
        });
    }

}
