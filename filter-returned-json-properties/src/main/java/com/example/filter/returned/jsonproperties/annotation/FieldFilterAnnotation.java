package com.example.filter.returned.jsonproperties.annotation;

import com.example.filter.returned.jsonproperties.constants.FieldFilterConstants;
import net.minidev.json.JSONObject;

import java.lang.annotation.*;

/**
 * 自定义注解，返回自定义的response
 *
 * @author leiduanchn
 * @create 2020-01-31 11:24 p.m.
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldFilterAnnotation {
    //想要保留的字段,用逗号(,)分隔
    String include()  default "";

    //想要过滤的字段,用逗号(,)分隔    这里默认过滤数据库的公共字段
    //"createBy,remarks,createTime,updateTime,updateBy,delFlag,tenantId"
    String exclude()  default FieldFilterConstants.EXCLUDE_DEFAULT;

    //返回到前端数据类型 这里使用JOSNObject 如果是纯集合可以使用JSONArray
    Class classez() default JSONObject.class;

}


