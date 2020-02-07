package com.example.dynamic.timing.task.service;

import com.example.dynamic.timing.task.repository.CronRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
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
 * 问题：
 * 使用@Scheduled注解来完成设置定时任务，但是有时候我们往往需要对周期性的时间的设置会做一些改变，或者要动态的启停一个定时任务，
 * 那么这个时候使用此注解就不太方便了，原因在于这个注解中配置的cron表达式必须是常量，那么当我们修改定时参数的时候，就需要停止服务，重新部署。
 *
 * 解决办法：
 * 方式一：实现SchedulingConfigurer接口，重写configureTasks方法，重新制定Trigger,
 * 核心方法就是addTriggerTask(Runnable task, Trigger trigger) ，不过需要注意的是，此种方式修改了配置值后，需要在下一次调度结束后，
 * 才会更新调度器，并不会在修改配置值时实时更新，实时更新需要在修改配置值时额外增加相关逻辑处理。
 *方式二：使用threadPoolTaskScheduler类可实现动态添加删除功能，当然也可实现执行频率的调整
 *
 */
@Component
@EnableScheduling
public class DynamicTimingTaskFromDB implements SchedulingConfigurer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static String cron;

    @Autowired
    private CronRepository repository;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        //项目部署时，会在这里执行一次，从数据库（文件等）拿到cron表达式
        cron = repository.findById(0).get().getCron();

        Runnable task = new Runnable() {
            @Override
            public void run() {
                //任务逻辑代码部分.
                logger.info("DynamicTimingTaskFromDB is running..." + LocalDateTime.now());
            }
        };

        Trigger trigger = new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                //任务触发，可修改任务的执行周期.
                //每一次任务触发，都会执行这里的方法一次，重新获取下一次的执行时间
                cron = repository.findById(0).get().getCron();
                System.out.println("execute trigger to gain cron: --- " + cron);
                CronTrigger trigger = new CronTrigger(cron);
                //下一次任务生效
                Date nextExec = trigger.nextExecutionTime(triggerContext);
                return nextExec;
            }
        };
        //传入task 和 trigger， 每一次执行任务都会触发trigger（先执行task，后trigger）
        taskRegistrar.addTriggerTask(task, trigger);
    }

}
