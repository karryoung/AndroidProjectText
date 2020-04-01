package com.li.androidprojecttext.kotlin.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import cn.bingoogolapple.bgabanner.BGABanner
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.hazz.kotlinmvp.glide.GlideApp
import com.li.androidprojecttext.R
import com.li.androidprojecttext.kotlin.mvp.model.bean.HomeBean
import com.li.androidprojecttext.kotlin.view.recyclerview.ViewHolder
import com.li.androidprojecttext.kotlin.view.recyclerview.adapter.CommonAdapter
import io.reactivex.Observable

/**
 * 首页精选的Adapter
 */
class HomeAdapter(context: Context, data: ArrayList<HomeBean.Issue.Item>)
    : CommonAdapter<HomeBean.Issue.Item>(context, data, -1) {

    //banner作为RecyclerView的第一项
    var bannerItemSize = 0

    companion object {
        private const val ITEM_TYPE_BANNER = 1 //Banner类型
        private const val ITEM_TYPE_TEXT_HEADER = 2//textHeader
        private const val ITEM_TYPE_CONTENT = 3//item
    }

    /**
     * 设置Banner大小
     */
    fun setBannerSize(count: Int) {
        bannerItemSize = count
    }

    /**
     * 添加更多数据
     */
    fun addItemData(itemList: ArrayList<HomeBean.Issue.Item>) {
        this.mData.addAll(itemList)
        notifyDataSetChanged()
    }

    /**
     * 得到item的类型
     */
    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 ->
                ITEM_TYPE_BANNER
            mData[position + bannerItemSize - 1].type == "textHeader" ->
                ITEM_TYPE_TEXT_HEADER
            else ->
                ITEM_TYPE_CONTENT
        }
    }

    /**
     *  得到 RecyclerView Item 数量（Banner 作为一个 item）
     */
    override fun getItemCount(): Int {
        return when {
            mData.size > bannerItemSize -> mData.size - bannerItemSize + 1
            mData.isEmpty() -> 0
            else -> 1
        }
    }

    /**
     * 绑定布局
     */
    override fun binData(holder: ViewHolder, data: HomeBean.Issue.Item, position: Int) {
        when(getItemViewType(position)){
            ITEM_TYPE_BANNER -> {
                val bannerItemData: ArrayList<HomeBean.Issue.Item> = mData.take(bannerItemSize).toCollection(ArrayList())
                val bannerFeedList = ArrayList<String>()
                val bannerTitleList = ArrayList<String>()
                //取出banner显示的img和Title
                Observable.fromIterable(bannerItemData)
                        .subscribe { list ->
                            bannerFeedList.add(list.data?.cover?.feed?: "")
                            bannerTitleList.add(list.data?.title?: "")
                        }
                //设置banner
                with(holder) {
                    getView<BGABanner>(R.id.banner).run {
                        setAutoPlayAble(bannerFeedList.size > 1)
                        setData(bannerFeedList, bannerTitleList)
                        setAdapter { banner, _, feedImageUrl, position ->
                            GlideApp.with(mContext)
                                    .load(feedImageUrl)
                                    .transition(DrawableTransitionOptions().crossFade())
                                    .placeholder(R.drawable.placeholder_banner)
                                    .into(banner.getItemImageView(position))
                        }

                    }
                }
            }
        }
    }

    /**
     * 创建布局
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType){
            ITEM_TYPE_BANNER ->
                ViewHolder(inflaterView(R.layout.item_home_banner, parent))
            ITEM_TYPE_TEXT_HEADER ->
                ViewHolder(inflaterView(R.layout.item_home_header, parent))
            else ->
                ViewHolder(inflaterView(R.layout.item_home_content, parent))
        }
    }

    /**
     * 加载布局
     */
    private fun inflaterView(mLayoutId: Int, parent: ViewGroup): View{
        //创建view
        val view = mInflater?.inflate(mLayoutId, parent, false)
        return view?: View(parent.context)
    }
}