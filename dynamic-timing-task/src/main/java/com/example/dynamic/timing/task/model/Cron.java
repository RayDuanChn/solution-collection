package com.example.dynamic.timing.task.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * @author leiduanchn
 * @create 2019-12-25 8:38 p.m.
 */
@Data
@Entity
public class Cron {

    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "cron")
    private String cron;

    protected Cron() {
    }


}
