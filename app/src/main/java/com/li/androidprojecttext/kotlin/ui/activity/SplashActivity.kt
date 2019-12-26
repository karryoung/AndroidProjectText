package com.li.androidprojecttext.kotlin.ui.activity

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.animation.AlphaAnimation
import com.li.androidprojecttext.R
import com.li.androidprojecttext.kotlin.MyApplication
import com.li.androidprojecttext.kotlin.base.BaseActivity

/**
 * 启动页
 */
class SplashActivity : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_splash

    override fun initData() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun start() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var textTypeface: Typeface? = null
    private var descTypeFace: Typeface? = null
    private var alphaAnimation: AlphaAnimation? = null

    init {
        textTypeface = Typeface.createFromAsset(MyApplication.context.assets,"fonts/Lobster-1.4.otf")
        descTypeFace = Typeface.createFromAsset(MyApplication.context.assets, "fonts/FZLanTingHeiS-L-GB-Regular.TTF")
    }

}