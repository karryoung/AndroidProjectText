package com.li.androidprojecttext.kotlin.api

import com.li.androidprojecttext.kotlin.mvp.model.bean.HomeBean
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Api接口
 */
interface ApiService {
    /**
     * 首页精选
     */
    @GET("v2/feed?")
    fun getFirstHomeData(@Query("num") num:Int): io.reactivex.Observable<HomeBean>

    /**
     * 根据 nextPageUrl 请求数据下一页数据
     */
    @GET
    fun getMoreHomeData(@Url url: String): io.reactivex.Observable<HomeBean>
}