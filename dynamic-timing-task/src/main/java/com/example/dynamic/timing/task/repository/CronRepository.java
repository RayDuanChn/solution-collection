package com.example.dynamic.timing.task.repository;

import com.example.dynamic.timing.task.model.Cron;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author leiduanchn
 * @create 2020-01-16 10:05 p.m.
 */
public interface CronRepository extends JpaRepository<Cron, Integer> {

}
