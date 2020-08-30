package com.li.androidprojecttext.fragment.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.li.androidprojecttext.R
import com.li.androidprojecttext.databinding.NavigationForm2Binding

/**
 *
 * @CreateDate:     2020/6/16 10:12
 * @Description:     类作用描述
 * @Author:         李想
 */
class NavigationForm2 : Fragment() {

    private lateinit var binding: NavigationForm2Binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.navigation_form2, container, false)
        return binding.root
    }
}