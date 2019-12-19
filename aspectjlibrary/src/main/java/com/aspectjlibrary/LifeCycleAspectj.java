package com.aspectjlibrary;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * 生命周期
 * 有关页面生命周期的埋点
 */
@Aspect
public class LifeCycleAspectj {

    private static final String TAG = "LifeCycleAspectj";

    @Before("execution(* android.app.Activity.on*(..))")
    public void onActivityMethodBefore(JoinPoint joinPoint) {
        //检测所有activity中以on开头的方法,但是要在activity中写出来
        String key = joinPoint.getSignature().toString();
        Log.e(TAG, "onActivityMethodBefore: 切面的点执行了！" + key);
    }

    @Around("execution(* com.li.androidprojecttext.databing.viewmodel.ViewModelActivity.onCreate(..))")
    public void onActivityMethodAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String key = proceedingJoinPoint.getSignature().toString();

        //这句代码的含义就是执行了这些方法
        //@Around 会替换原先执行的代码，但如果你仍然希望执行原先的代码，可以使用joinPoint.proceed()。
        proceedingJoinPoint.proceed();

        long endTime = System.currentTimeMillis();
        Log.e(TAG, "方法用时: " + (endTime - startTime)+"-----"+key);
    }

    @After("call(* com.li.androidprojecttext.databing.viewmodel.ViewModelActivity.onCreate(..))")
    public void onCallBefore(JoinPoint joinPoint) {
        Log.e(TAG, "方法结束时 ");
    }

}
