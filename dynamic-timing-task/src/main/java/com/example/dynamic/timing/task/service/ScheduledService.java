package com.example.dynamic.timing.task.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

/**
 * @author leiduanchn
 * @create 2020-02-06 3:20 p.m.
 */
@EnableScheduling
@Service
@EnableAsync
public class ScheduledService {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledService.class);

    /**
     * 问题： 多个定时任务使用的是同一个调度线程，所以任务是阻塞执行的，执行效率不高。
     *
     * 可以使用@EnableAsync 启用异步任务，然后在定时任务的方法加上@Async即可，
     * 此方法默认使用的线程池为SimpleAsyncTaskExecutor（该线程池默认来一个任务创建一个线程，就会不断创建大量线程，极有可能压爆服务器内存。当然它有自己的限流机制，这里就不多说了，有兴趣的自己翻翻源码~）
     * 项目中为了更好的控制线程的使用，我们可以自定义我们自己的线程池，使用方式@Async("myThreadPool")
     * 链接：https://juejin.im/post/5ca761c36fb9a05e2726b18e
     * wiz://open_document?guid=9e0fadb8-5dac-43c6-9b40-9848cd2a8990&kbguid=&private_kbguid=d1140c4e-30fb-4957-b06a-18c38a93cded
     * wiz://open_document?guid=2caee6e1-d6b7-4587-adca-12439efb6676&kbguid=&private_kbguid=d1140c4e-30fb-4957-b06a-18c38a93cded
     * wiz://open_document?guid=55a60c2a-0ee0-41d5-9916-7cf3a70ed9ec&kbguid=&private_kbguid=d1140c4e-30fb-4957-b06a-18c38a93cded
     *
     */
    @Scheduled(cron = "0/4 * * * * MON-SAT")  //每4秒执行一次
    @Async
    public void testScheduled(){
        logger.info(LocalDateTime.now() + " -- testScheduled");
        //System.out.println(LocalDateTime.now() + " -- testScheduled");
    }
}
