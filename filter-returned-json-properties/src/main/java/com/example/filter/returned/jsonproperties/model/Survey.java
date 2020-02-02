package com.example.filter.returned.jsonproperties.model;

import lombok.Data;

/**
 * @author leiduanchn
 * @create 2020-02-01 8:13 p.m.
 */
@Data
public class Survey extends BaseModel {
    private Long clinicId;
    private String title;
    private String status;

    private TestEntity entity;
}
