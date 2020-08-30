package com.li.permission

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData

/**
 *
 * @CreateDate:     2020/6/4 11:03
 * @Description:     获取权限请求时使用的对象
 * @Author:         李想
 */
class LivePermissions {
    @Volatile
    private var liveFragment: LiveFragment? = null
    companion object{
        const val TAG = "permissions"
    }

    constructor(activity: AppCompatActivity) {
        liveFragment = getInstance(activity.supportFragmentManager)
    }

    constructor(fragment: Fragment){
        liveFragment = getInstance(fragment.childFragmentManager)
    }

    private fun getInstance(fragmentManager: FragmentManager) =
            liveFragment ?: synchronized(this){
                liveFragment ?: if (fragmentManager.findFragmentByTag(TAG) == null) LiveFragment().run {
                    fragmentManager.beginTransaction().add(this, TAG).commitNow()
                    this
                } else fragmentManager.findFragmentByTag(TAG) as LiveFragment
            }

    fun request(vararg permissions: String) : MutableLiveData<PermissionResult>{
        return this.requestArray(permissions)
    }

    fun requestArray(permissions: Array<out String>): MutableLiveData<PermissionResult> {
        liveFragment!!.requestPermissions(permissions)
        return liveFragment!!.liveData
    }

}