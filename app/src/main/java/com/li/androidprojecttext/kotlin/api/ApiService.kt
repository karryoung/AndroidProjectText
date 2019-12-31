package com.li.androidprojecttext.kotlin.api

import android.database.Observable
import com.li.androidprojecttext.kotlin.mvp.model.bean.HomeBean
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Api接口
 */
interface ApiService {
    /**
     * 首页精选
     */
    @GET("v2/feed?")
    fun getFirstHomeData(@Query("num") num: Int): Observable<HomeBean>
}