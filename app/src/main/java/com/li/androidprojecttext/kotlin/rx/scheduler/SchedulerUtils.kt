package com.li.androidprojecttext.kotlin.rx.scheduler

import com.hazz.kotlinmvp.rx.scheduler.IoMainScheduler

object SchedulerUtils {

    fun <T> ioToMain(): IoMainScheduler<T>{
        return IoMainScheduler()
    }
}