package com.example.filter.returned.jsonproperties.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 测试类
 *
 * @author leiduanchn
 * @create 2020-01-31 11:31 p.m.
 */

@Data
public class TestEntity extends BaseModel {

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime time;

}
