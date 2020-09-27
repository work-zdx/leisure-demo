package com.star.content.adapter

import android.app.Activity
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.star.content.bean.DataWrapper
import com.star.content.viewHolder.ContentHolder

class ContentAdapter(private val activity: Activity, private val dataList: ArrayList<DataWrapper>) : RecyclerView.Adapter<ContentHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentHolder {
        return ContentHolder(FrameLayout(activity), activity)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(viewHolder: ContentHolder, position: Int) {
        viewHolder.bind(dataList[position])
    }
}