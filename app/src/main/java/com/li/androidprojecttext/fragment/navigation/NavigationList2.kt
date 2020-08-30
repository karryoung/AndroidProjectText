package com.li.androidprojecttext.fragment.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.li.androidprojecttext.R
import com.li.androidprojecttext.databinding.NavigationList2Binding

/**
 *
 * @CreateDate:     2020/6/15 18:54
 * @Description:     类作用描述
 * @Author:         李想
 */
class NavigationList2 : Fragment() {

    private lateinit var binding : NavigationList2Binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.navigation_list2, container, false)
        return binding.root
    }
}