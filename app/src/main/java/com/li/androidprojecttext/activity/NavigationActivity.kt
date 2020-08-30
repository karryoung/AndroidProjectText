package com.li.androidprojecttext.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.li.androidprojecttext.R
import com.li.androidprojecttext.databinding.ActivityNavigationBinding

/**
 *
 * @CreateDate:     2020/6/15 18:22
 * @Description:     类作用描述
 * @Author:         李想
 */
class NavigationActivity : AppCompatActivity() {
    private var databing: ActivityNavigationBinding? = null
    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databing = DataBindingUtil.setContentView(this, R.layout.activity_navigation)
        if (savedInstanceState == null) {
            setupBottomNaigationBar()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNaigationBar()
    }

    private fun setupBottomNaigationBar() {
        val navGraphIds = listOf(R.navigation.test, R.navigation.list, R.navigation.form)
//        val controller = databing.bottomNavigation.setu
    }
}