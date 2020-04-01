package com.li.androidprojecttext.kotlin.view.recyclerview.adapter

/**
 * Adapter的长按事件
 */
interface OnItemLongClickListener {
    fun onItemLongClick(obj: Any?, position: Int): Boolean
}