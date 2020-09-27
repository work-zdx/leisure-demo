package com.star.content.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.star.content.R
import com.star.content.bean.Banner
import com.star.content.bean.Game
import com.star.content.viewHolder.BannerHolder
import com.star.content.viewHolder.GameHolder
import com.star.content.viewHolder.GameNativeHolder

class BannerAdapter(private val banners: List<Banner>, private val listener: ((Banner) -> Unit)?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BannerHolder( LayoutInflater.from(parent.context)
                .inflate(R.layout.item_banner, parent, false))
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val banner = banners[position]

        if (viewHolder is BannerHolder) {
            viewHolder.bind(banner) {
                listener?.invoke(it)
            }
            return
        }
    }

    override fun getItemCount(): Int {
       return banners.size
    }
}