package com.li.androidprojecttext.kotlin.ui.fragment

import com.li.androidprojecttext.R
import com.li.androidprojecttext.kotlin.base.BaseFragment
import com.li.androidprojecttext.kotlin.mvp.contract.HomeContract
import com.li.androidprojecttext.kotlin.mvp.model.bean.HomeBean

/**
 * 首页
 * (数据是 Banner 数据和一页数据组合而成的 HomeBean,查看接口然后在分析就明白了)
 */
class HomeFragment : BaseFragment(), HomeContract.View {

    private var bannerHomeBean: HomeBean? = null
    private var nextPageUrl: String? = null

    override fun setHomeData(homeBean: HomeBean) {
    }

    override fun setMoreData(itemList: ArrayList<HomeBean.Issue.Item>) {
    }

    override fun showError(msg: String, errorCode: Int) {
    }

    override fun showLoading() {
    }

    override fun dismissLoading() {
    }

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun initView() {
    }

    override fun lazyLoad() {
    }
}