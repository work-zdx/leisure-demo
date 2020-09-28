package com.star.content.viewHolder

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.star.content.R
import com.star.content.bean.DataWrapper
import com.star.content.formatDate
import com.star.content.formatDuration
import com.starmedia.pojos.ContentItem
import kotlinx.android.synthetic.main.item_content_image.view.*
import kotlinx.android.synthetic.main.item_content_text.view.*
import kotlinx.android.synthetic.main.item_content_video.view.*
import java.text.MessageFormat

class ContentHolder(private val container: FrameLayout, private val activity: Activity) :
    RecyclerView.ViewHolder(container) {
    fun bind(data: DataWrapper) {
        container.removeAllViews()

        if (data.isAd) {
            //加载广告
            LayoutInflater.from(activity).inflate(R.layout.item_ad, container, true)
            container.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            )
        } else {
            data.data?.let { item ->
                //上报条目曝光
                item.onShow()

                when (item.type) {
                    ContentItem.Type.NEWS -> {
                        LayoutInflater.from(activity).inflate(R.layout.item_content_text, container, true)

                        val contentNews = item.getContentNews()

                        container.setOnClickListener {
                            item.onClick(activity)
                        }

                        container.tv_content_text_title.text = contentNews?.title
                        container.tv_content_text_source.text = contentNews?.source
                        container.tv_content_text_read.text =
                                MessageFormat.format("{0}阅读", contentNews?.readCounts ?: 0)
                        container.tv_content_text_time.text = formatDate(contentNews?.updateTime ?: "")
                    }
                    ContentItem.Type.IMAGE -> {
                        LayoutInflater.from(activity).inflate(R.layout.item_content_image, container, true)

                        val contentImage = item.getContentImage()

                        container.setOnClickListener {
                            item.onClick(activity)
                        }

                        container.tv_content_image_title.text = contentImage?.title
                        container.tv_content_image_count.text = contentImage?.colImageCount?.toString()
                        container.tv_content_image_source.text = contentImage?.source?.name
                        container.tv_content_image_read.text =
                                MessageFormat.format("{0}阅读", contentImage?.readCounts ?: 0)
                        container.tv_content_image_time.text = formatDate(contentImage?.updateTime
                                ?: "")

                        Glide.with(container).load(contentImage?.loadImage() ?: "")
                                .into(container.iv_content_image)
                    }
                    ContentItem.Type.VIDEO -> {
                        LayoutInflater.from(activity).inflate(R.layout.item_content_video, container, true)

                        val contentVideo = item.getContentVideo()

                        container.setOnClickListener {
                            item.onClick(activity)
                        }

                        container.tv_content_video_title?.text = contentVideo?.title
                        container.tv_video_duration?.text =
                                formatDuration(contentVideo?.duration ?: 0)
                        container.tv_content_video_source?.text = contentVideo?.source?.name
                        container.tv_content_video_time?.text = formatDate(contentVideo?.updateTime
                                ?: "")
                        container.tv_content_video_play_count?.text =
                                MessageFormat.format("{0}次播放", contentVideo?.playCounts ?: 0)

                        Glide.with(container).load(contentVideo?.loadImage() ?: "")
                                .into(container.iv_content_video_picture)
                    }
                    else -> {

                    }
                }
            }
        }
    }
}