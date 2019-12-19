package com.aspectjlibrary;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Arrays;

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

    /**
     * 切入方法
     */
    @Pointcut("call(* com.li.androidprojecttext.databing.viewmodel.ViewModelActivity.BackInt(..))")
    public void callWith() {
    }

    /**
     * 使用AfterReturning
     * @param jp JoinPoint，可获取方法、类、参数等信息(这个参数不是AfterReturning特有，不用关心)
     * @param ret 返回值 AfterReturning特有,注意：注解中的参数名称与方法中的名称要一样，比如这里的ret
     */
    @AfterReturning(returning = "ret",  pointcut = "callWith()")
    public void testCallAft(JoinPoint jp, Object ret) {
        //打印返回值
        Log.d(TAG, "callWith----BackInt方法的返回值,return:" + ret);
        Log.i(TAG, "callWith---,args:" + Arrays.toString(jp.getArgs()));
    }

    @Before("callWith()")
    public void testCallBef() {
        Log.d(TAG, "运行callWith之前before");
    }
}
