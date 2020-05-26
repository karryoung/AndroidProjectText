package com.li.androidprojecttext.kotlin

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.multidex.MultiDex
import com.facerecognition.dualdemoVL.ModelInit
import com.li.androidprojecttext.BuildConfig
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.squareup.leakcanary.RefWatcher
import kotlin.properties.Delegates

/**
 * kotlin项目里的application
 */
class MyApplication : Application() {

    private var refWatcher: RefWatcher? = null

    companion object {
        private val TAG = "MyApplication"
        var context: Context by Delegates.notNull()
            private set

        fun getRefWatcher(context: Context): RefWatcher? {
            val myApplication = context.applicationContext as MyApplication
            return myApplication.refWatcher
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        ModelInit.initFras() //初始化授权库
        ModelInit.initModelDirMap(context as Application?)
        initConfig()
        com.li.androidprojecttext.kotlin.utils.DisplayManager.init(this)
        registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)

        MultiDex.install(this);
    }

    private val mActivityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityPaused(activity: Activity) {
        }

        override fun onActivityStarted(activity: Activity) {
            Logger.d(TAG, "onStart:" + activity.componentName.className)
        }

        override fun onActivityDestroyed(activity: Activity) {
            Logger.d(TAG, "onDestroy:" + activity.componentName.className)
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }

        override fun onActivityStopped(activity: Activity) {
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            Logger.d(TAG, "onCreated:" + activity.componentName.className)
        }

        override fun onActivityResumed(activity: Activity) {
        }
    }

    /**
     * 初始化日志配置
     */
    private fun initConfig() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)//隐藏线程信息 默认：显示
                .methodCount(0)//决定打印多少行（每一行代表一个方法）默认：2
                .methodOffset(7)// (Optional) Hides internal method calls up to offset. Default 5
                .tag("text_kotlin")//(Optional) Global tag for every log. Default PRETTY_LOGGER
                .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }
}