package com.example.filter.returned.jsonproperties.controller;

import com.example.filter.returned.jsonproperties.annotation.FieldFilterAnnotation;
import com.example.filter.returned.jsonproperties.annotation.FieldFilterReturnResponseEntityAnnotation;
import com.example.filter.returned.jsonproperties.model.Survey;
import com.example.filter.returned.jsonproperties.model.TestEntity;
import net.minidev.json.JSONArray;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用注解的业务逻辑类
 *
 * @author leiduanchn
 * @create 2020-01-31 11:32 p.m.
 */
@RestController
public class TestEntityController {

    @GetMapping("/regular-object")
    @FieldFilterAnnotation(include = "name,time")
    public R testReturnObject(){
        TestEntity testEntity = new TestEntity();
        testEntity.setTime(LocalDateTime.now());
        testEntity.setName("name");
        testEntity.setRemarks("备注");
        return R.ok(testEntity);
    }

    @GetMapping("/nested-object")
    @FieldFilterReturnResponseEntityAnnotation
    public ResponseEntity<Survey> testReturnNestedObject() {
        TestEntity testEntity = new TestEntity();
        testEntity.setTime(LocalDateTime.now());
        testEntity.setName("name");
        testEntity.setRemarks("备注");

        Survey survey = new Survey();
        survey.setClinicId(100L);
        survey.setStatus("1");
        survey.setTitle("patient survey");
        survey.setEntity(testEntity);
        return ResponseEntity.ok(survey);
    }

    @GetMapping("/collection")
    @FieldFilterReturnResponseEntityAnnotation(classez = JSONArray.class)
    public ResponseEntity<List<Survey>> testReturnCollection() {
        ArrayList<Survey> list = new ArrayList<>();
        TestEntity testEntity = new TestEntity();
        testEntity.setTime(LocalDateTime.now());
        testEntity.setName("name");
        testEntity.setRemarks("备注");

        Survey survey = new Survey();
        survey.setClinicId(100L);
        survey.setStatus("1");
        survey.setTitle("patient survey");
        survey.setEntity(testEntity);
        list.add(survey);
        list.add(survey);
        return ResponseEntity.ok(list);
    }
}