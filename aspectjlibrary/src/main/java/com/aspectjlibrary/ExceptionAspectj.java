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
//     //指定的异常种类
//    @Before(value = "handler(java.lang.Exception) && args(ex)")
//    public void handlerExceptionBeforeMethod(Exception ex) {
//        Log.e(TAG, "handlerExceptionBeforeMethod: 异常产生了--");
//        Log.e(TAG, "handlerExceptionBeforeMethod: 出现Exception异常--"+ex);
//        //打印栈信息
//        ex.printStackTrace();
//        //错误的详细信息
//        Log.e(TAG, "handlerExceptionBeforeMethod: 出现Exception异常---详细信息--"+ex.getMessage());
////        ex.getMessage();
//        //可以统一写入文件中
//    }

    //获取所有的出错信息
    @Before(value = "handler(*) && args(ex)")
    public void handlerErrorExceptionBeforeMethod(Throwable ex) {
        Log.e(TAG, "handlerErrorExceptionBeforeMethod: 异常产生了--");
        Log.e(TAG, "handlerErrorExceptionBeforeMethod: 出现ErrorException异常--"+ex);
        //打印栈信息
        ex.printStackTrace();
        //错误的详细信息
        Log.e(TAG, "handlerErrorExceptionBeforeMethod: 出现Exception异常---详细信息--"+ex.getMessage());
//        ex.getMessage();
        //可以统一写入文件中
    }

    @AfterThrowing(pointcut = "execution(* com.li.androidprojecttext.*.*(..))", throwing = "exception")
    public void catchExceptionMethod(Exception exception) {

        //处理程序中未处理的异常, 但是如果方法加上了try catch，则不会再走此方法
        String message = exception.toString();

        Log.d(TAG, "未处理的异常catchExceptionMethod: " + message);

    }
}
