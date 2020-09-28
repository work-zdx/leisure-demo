package com.star.content.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.star.content.bean.Banner
import kotlinx.android.synthetic.main.item_banner.view.*

class BannerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(banner: Banner, listener: ((Banner) -> Unit)?) = with(itemView) {

        cl_banner_item.setOnClickListener {
            listener?.invoke(banner)
        }

        iv_banner_logo.setImageResource(banner.logo)

        tv_banner_title.text = banner.name
    }
}