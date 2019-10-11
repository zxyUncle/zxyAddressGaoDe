package com.zxy.zxygaodemap.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.amap.api.services.core.PoiItem
import com.zxy.zxygaodemap.R
import kotlinx.android.synthetic.main.adapter_poi_search_data.view.*

/**
 * Created by zxy on 2019/8/2-8:53
 * Class functions
 * ******************************************
 * * 主页圈子Adapter
 * ******************************************
 */
class POISearchDataAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mContext: Context? = null
    private var onItemClickListener: OnItemClickListener? = null
    var pois: MutableList<PoiItem>? = null


    constructor(mContext: Context, pois: MutableList<PoiItem>) : this() {
        this.pois = pois
        this.mContext = mContext
    }

    interface OnItemClickListener {
        fun onClick(view: View, position: Int, poiItem: PoiItem)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.adapter_poi_search_data, parent, false)
        val itemHolder = ItemHolder(view)
        return itemHolder
    }

    override fun getItemCount(): Int {
        return pois!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val itemHolder = holder as ItemHolder
        itemHolder.initData(position)
    }

    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun initData(position: Int) {
            itemView.tv_adapter_search_data_name.text = pois!!.get(position).title
            itemView.tv_adapter_search_data_address.text =
                "${pois!!.get(position).provinceName}-${pois!!.get(position).adName}-${pois!!.get(position).snippet}"

            //点击
            itemView.setOnClickListener {
                initOnClick(position)
            }
        }

        //按钮点击事件
        fun initOnClick(position: Int) {
            onItemClickListener!!.onClick(itemView, position, pois!!.get(position))
        }
    }


}