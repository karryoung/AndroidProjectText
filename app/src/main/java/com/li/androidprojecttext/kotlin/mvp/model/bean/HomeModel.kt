package com.li.androidprojecttext.kotlin.mvp.model.bean

import com.li.androidprojecttext.kotlin.net.RetrofitManager
import com.li.androidprojecttext.kotlin.rx.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 * 首页获取数据
 */
class HomeModel{

    /**
     * 获取首页Banner数据
     */
    fun requestHomeData(num:Int): Observable<HomeBean> {
        return RetrofitManager.service.getFirstHomeData(num)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 加载更多
     */
    fun loadMoreData(url:String):Observable<HomeBean>{

        return RetrofitManager.service.getMoreHomeData(url)
                .compose(SchedulerUtils.ioToMain())
    }

}