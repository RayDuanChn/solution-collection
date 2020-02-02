package com.example.filter.returned.jsonproperties.model;

import lombok.Data;
import org.springframework.ui.Model;

import java.util.Date;

/**
 * BaseModel类
 *
 * @author leiduanchn
 * @create 2020-01-31 11:29 p.m.
 */
@Data
public class BaseModel{

    private Integer id;

    private String remarks;

    private Date createTime;
    private String createBy;
    private Date updateTime;
    private String updateBy;
    private String delFlag;
    private Integer tenantId;
}
