package com.aspectjlibrary;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * 异常信息
 * 有关项目中的错误信息
 */
@Aspect
public class ExceptionAspectj {

    private static final String TAG = "ExceptionAspectj";

    @Before("handler(java.lang.Exception)")
    public void handlerExceptionBeforeMethod() {
        Log.e(TAG, "handlerExceptionBeforeMethod: 异常产生了--");
    }

    @AfterThrowing(pointcut = "execution(* com.li.androidprojecttext.*.*(..))", throwing = "exception")
    public void catchExceptionMethod(Exception exception) {

        //处理程序中未处理的异常, 但是如果方法加上了try catch，则不会再走此方法
        String message = exception.toString();

        Log.d(TAG, "未处理的异常catchExceptionMethod: " + message);

    }
}
