package com.aspectjlibrary;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * 日志分析
 * 有关项目中的日志点击行为
 */
@Aspect
public class LogAspectj {
    private static final String TAG = "LogAspectj";

//    @Pointcut("call(* com.jinlong.aspectjdemo.MainActivity.callMethod(..))")
//    public void callMethod() {
//        //为了演示call方法的使用
//    }
//
//    @Before("callMethod()")
//    public void beforeCallMethod(JoinPoint joinPoint) {
//        Log.e(TAG, "call方法的演示");
//    }

    //上面代码可以简化为：
    @Before("call(* com.li.androidprojecttext.databing.viewmodel.ViewModelActivity.testApp(..))")
    public void beforeCallMethod(JoinPoint joinPoint) {
        Log.e(TAG, "testApp方法执行之前");
    }

    @Around("call(* com.li.androidprojecttext.databing.viewmodel.ViewModelActivity.testApp(..))")
    public void aroundCallMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        Log.e(TAG, "testApp方法执行的时间为" + (endTime - startTime));
    }

    @Before("execution(com.li.androidprojecttext.databing.viewmodel.model.User.new(..))")
    public void beforeConstructorExecution(JoinPoint joinPoint) {
        //这个是显示Constructor的
        Log.e(TAG, "貌似是未user赋值之前before->" + joinPoint.getThis().toString() + "#" + joinPoint.getSignature().getName());
    }

//    @After("execution(* com.jinlong.aspectjdemo.Person(..))")
//    public void beforeConstructorExecution(JoinPoint joinPoint) {
//        //todo 这里还有点问题没有研究明白
//        Log.e(TAG, "before->" + joinPoint.getThis().toString() + "#" + joinPoint.getSignature().getName());
//    }


    @Around("get(String com.li.androidprojecttext.databing.viewmodel.model.User.age)")
    public String aroundFieldGet(ProceedingJoinPoint joinPoint) throws Throwable {
        // 执行原代码
        Object obj = joinPoint.proceed();
        String age = obj.toString();
        Log.e(TAG, "执行User中的age--貌似是获取值: " + age);
        return "100";
    }

    @Around("set(String com.li.androidprojecttext.databing.viewmodel.model.User)&&!withincode(" +
            "com.li.androidprojecttext.databing.viewmodel.model.User.new(..))")
    public void aroundFieleSet(ProceedingJoinPoint joinPoint) throws Throwable {
        //设置相应的成员变量
        joinPoint.proceed();
        Log.e(TAG, "貌似执行User中的age赋值: ");
    }

}
