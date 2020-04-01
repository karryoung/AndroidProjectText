package com.li.androidprojecttext.kotlin.view.recyclerview

/**
 * 多布局条目类型
 */
interface MultipleType<in T> {
    fun getLayoutId(item: T, position: Int): Int
}