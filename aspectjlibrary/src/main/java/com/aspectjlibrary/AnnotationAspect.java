package com.aspectjlibrary;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * description : 注解的切面类
 */
@Aspect
public class AnnotationAspect {

    public static final String TAG = "AnnotationAspect";

    @Pointcut("execution(@com.aspectjlibrary.DebugTrace * *(..))")
    public void DebugTraceMethod() {
    }

    @Before("DebugTraceMethod()")
    public void beforeDebugTraceMethod(JoinPoint joinPoint) {
        String key = joinPoint.getSignature().toString();
        Log.e(TAG, "注解这个方法执行了: "+joinPoint.toString());
    }
}
