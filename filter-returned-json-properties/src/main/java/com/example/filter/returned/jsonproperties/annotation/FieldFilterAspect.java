package com.example.filter.returned.jsonproperties.annotation;

import com.example.filter.returned.jsonproperties.controller.R;
import com.example.filter.returned.jsonproperties.filter.FieldFilterSerializer;
import com.example.filter.returned.jsonproperties.model.BaseModel;
import com.example.filter.returned.jsonproperties.model.TestEntity;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.swing.text.html.parser.Entity;
import java.lang.reflect.Method;

/**
 * 自定义注解实现(也就是切面类)，定义通知方法
 *
 * @author leiduanchn
 * @create 2020-01-31 11:25 p.m.
 */


/**
 * AOP 全注解操作
 * 1. 步骤
 * 1、导入aop模块；Spring AOP：(spring-aspects)
 * 2、定义一个业务逻辑类（MathCalculator）；在业务逻辑运行的时候将日志进行打印（方法之前、方法运行结束、方法出现异常，xxx）
 * 3、定义一个日志切面类（LogAspects）：切面类里面的方法需要动态感知MathCalculator.div运行到哪里然后执行；
 *  通知方法：
 *      前置通知(@Before)：logStart：在目标方法(div)运行之前运行
 *      后置通知(@After)：logEnd：在目标方法(div)运行结束之后运行（无论方法正常结束还是异常结束）
 *      返回通知(@AfterReturning)：logReturn：在目标方法(div)正常返回之后运行
 *      异常通知(@AfterThrowing)：logException：在目标方法(div)出现异常以后运行
 *      环绕通知(@Around)：动态代理，手动推进目标方法运行（joinPoint.procced()）
 * 4、给切面类的目标方法标注何时何地运行（通知注解）；
 * 5、将切面类和业务逻辑类（目标方法所在类）都加入到容器中;
 * 6、必须告诉Spring哪个类是切面类(给切面类上加一个注解：@Aspect)
 *[7]、给配置类中加 @EnableAspectJAutoProxy 【开启基于注解的aop模式】, 在Spring中很多的 @EnableXXX;
 *
 * 简化三步：
 * 1）、将业务逻辑组件和切面类都加入到容器中；告诉Spring哪个是切面类（@Aspect）
 * 2）、在切面类上的每一个通知方法上标注通知注解，告诉Spring何时何地运行（切入点表达式）
 * 3）、开启基于注解的aop模式；@EnableAspectJAutoProxy
 *
 */

@Aspect     //告诉Spring哪个类是切面类(给切面类上加一个注解：@Aspect)
@Component  //将切面类和业务逻辑类（目标方法所在类）都加入到容器中;
public class FieldFilterAspect {

    @Pointcut("@annotation(FieldFilterAnnotation)")
    public void fieldFilterAspect(){}

    //给切面类的目标方法标注何时何地运行（通知注解）
    //Around注解改变服务的返回值
    //统一返回值使用R工具类,所以返回结果都是在R的data属性中
    @Around("fieldFilterAspect()")
    public R around(ProceedingJoinPoint pjp) throws Throwable {
        R r = (R) pjp.proceed();     //执行目标原方法
        if(r.getCode() == 1)return r;
        Object object = r.getData();
        if(object == null) return r;

        MethodSignature methodSignature =  (MethodSignature) pjp.getSignature();    // 获取目标方法
        Method method = methodSignature.getMethod();
        FieldFilterAnnotation annotation = method.getAnnotation(FieldFilterAnnotation.class);
        FieldFilterSerializer fieldFilter = new FieldFilterSerializer();

        //这里传入公共类 由于所有类都继承了mybatis的公共字段类BaseModel
        //所以只要传入BaseModel就可以进行全部类的过滤
        fieldFilter.filter(BaseModel.class, annotation.include(), annotation.exclude());

        //返回过滤后的json字符串
        String jsonString = fieldFilter.toJSONString(r.getData());

        //将返回的json字符串转换成 JSONObject 或者 JSONArray并放入到R的data属性中返回到前端
        r.setData(fieldFilter.parseObject(jsonString, annotation.classez()));
        return r;
    }



    @Pointcut("@annotation(FieldFilterReturnResponseEntityAnnotation)")
    public void fieldFilterAspectReturnResponseEntity(){}

    //给切面类的目标方法标注何时何地运行（通知注解）
    //Around注解改变服务的返回值
    //统一返回值使用R工具类,所以返回结果都是在R的data属性中
    @Around("fieldFilterAspectReturnResponseEntity()")
    public ResponseEntity aroundReturnResponseEntity(ProceedingJoinPoint pjp) throws Throwable {
        ResponseEntity responseEntity = (ResponseEntity) pjp.proceed();     //执行目标原方法

        MethodSignature methodSignature =  (MethodSignature) pjp.getSignature();    // 获取目标方法
        Method method = methodSignature.getMethod();
        FieldFilterReturnResponseEntityAnnotation annotation = method.getAnnotation(FieldFilterReturnResponseEntityAnnotation.class);
        FieldFilterSerializer fieldFilter = new FieldFilterSerializer();

        //这里传入公共类 由于所有类都继承了mybatis的公共字段类BaseModel
        //所以只要传入BaseModel就可以进行全部类的过滤
        fieldFilter.filter(BaseModel.class, annotation.include(), annotation.exclude());

        //返回过滤后的json字符串
        String jsonString = fieldFilter.toJSONString(responseEntity.getBody());

        //将返回的json字符串转换成 JSONObject 或者 JSONArray并放入到R的data属性中返回到前端
        return new ResponseEntity(fieldFilter.parseObject(jsonString, annotation.classez()), null, HttpStatus.OK);
    }
}
