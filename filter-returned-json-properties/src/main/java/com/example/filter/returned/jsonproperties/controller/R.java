package com.example.filter.returned.jsonproperties.controller;

import com.example.filter.returned.jsonproperties.model.TestEntity;

/**
 *
 * @author leiduanchn
 * @create 2020-01-31 11:33 p.m.
 */
public class R {

    private int code;
    private Object data;

    public R() {
    }

    public R(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    public static R ok(Object data) {
        return new R(200, data);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
