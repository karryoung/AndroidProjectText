package com.li.androidprojecttext.kotlin.ui.activity

import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.li.androidprojecttext.R
import com.li.androidprojecttext.kotlin.base.BaseActivity
import com.li.androidprojecttext.kotlin.ui.fragment.DiscoveryFragment
import com.li.androidprojecttext.kotlin.ui.fragment.HomeFragment
import com.li.androidprojecttext.kotlin.ui.fragment.HotFragment
import com.li.androidprojecttext.kotlin.ui.fragment.MineFragment
import com.li.androidprojecttext.kotlin.mvp.model.bean.TabEntity
import kotlinx.android.synthetic.main.activity_main_for_kotlin.*

/**
 * 首页
 */
class MainActivity : BaseActivity() {
    private val mTitles = arrayOf("每日精选", "发现", "热门", "我的")
    //未被选中的图标
    private val mIconUnSelectIds = intArrayOf(R.mipmap.ic_home_normal, R.mipmap.ic_discovery_normal, R.mipmap.ic_hot_normal, R.mipmap.ic_mine_normal)
    //选中的图标
    private val mIconSelectIds = intArrayOf(R.mipmap.ic_home_selected, R.mipmap.ic_discovery_selected, R.mipmap.ic_hot_selected, R.mipmap.ic_mine_selected)

    private val mTabEntities = ArrayList<CustomTabEntity>()

    private var mHomeFragment: HomeFragment? = null
    private var mDiscoveryFragment: DiscoveryFragment? = null
    private var mHotFragment: HotFragment? = null
    private var mMineFragment: MineFragment? = null

    //默认为0
    private var mIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mIndex = savedInstanceState.getInt("currTabIndex")
        }
        super.onCreate(savedInstanceState)
        initTab()
    }

    //初始化底部菜单
    private fun initTab() {
        (0 until mTitles.size)
                .mapTo(mTabEntities) {
                    TabEntity(mTitles[it],mIconSelectIds[it], mIconUnSelectIds[it])
                }

        //为Tab赋值
        tab_layout.setTabData(mTabEntities)
        tab_layout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabReselect(position: Int) {

            }

            override fun onTabSelect(position: Int) {
                //切换fragment
                switchFragment(position)
            }
        })
    }

    //切换fragment
    private fun switchFragment(position: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        hideFragments(transaction)
//        when(position){
//            0//首页
//            ->mHomeFragment?.let {
//                transaction.show(it)
//            }?:HomeFragment.
//        }
    }

    //隐藏fragment
    private fun hideFragments(transaction: FragmentTransaction) {
        mHomeFragment?.let { transaction.hide(it) }
//        mDiscoveryFragment?.let { transaction.hide(it) }
//        mHotFragment?.let { transaction.hide(it) }
//        mMineFragment?.let { transaction.hide(it) }
    }

    override fun layoutId(): Int = R.layout.activity_main_for_kotlin

    override fun initData() {
    }

    override fun initView() {
    }

    override fun start() {
    }
}