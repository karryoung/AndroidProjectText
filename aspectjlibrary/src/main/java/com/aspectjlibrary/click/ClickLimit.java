package com.aspectjlibrary.click;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description:
 * 针对点击事件的操作，防抖动，默认间隔时间是500ms
 * 如果修改间隔时间需要在点击的地方添加@ClickLimit(value = 3000)
 * @ClickLimit(clickAble = 0)表示不需要ClickLimitAspect  processJoinPoint中的点击事件控制
 * 默认是需要防抖动的
 */
@Target({ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ClickLimit {

    int value() default 500;//默认间隔时间

    int clickAble() default 1;
}
