package com.star.content

import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.starmedia.pojos.ContentItem
import com.starmedia.tinysdk.IContent
import com.starmedia.tinysdk.StarMedia
import kotlinx.android.synthetic.main.activity_lock.*
import kotlinx.android.synthetic.main.item_content_image.view.iv_content_image
import kotlinx.android.synthetic.main.item_content_image.view.tv_content_image_count
import kotlinx.android.synthetic.main.item_content_text.view.tv_content_read
import kotlinx.android.synthetic.main.item_content_text.view.tv_content_source
import kotlinx.android.synthetic.main.item_content_text.view.tv_content_time
import kotlinx.android.synthetic.main.item_content_text.view.tv_content_title
import kotlinx.android.synthetic.main.item_content_video.view.*
import java.text.MessageFormat


class LockScreenActivity : BaseActivity() {

    var page = 1

    var isLoading = false

    val dataList = ArrayList<ContentItem>()

    lateinit var layoutManager: LinearLayoutManager

    private var params = HashMap<Long, List<Int>>().apply {
        put(0, arrayListOf(1001, 1002, 1004, 1005, 1006, 1007, 1008, 1009, 1011, 1012))
        put(1, arrayListOf(1001, 1002, 1004, 1005, 1006, 1007, 1008, 1009, 1011, 1012))
        put(2, arrayListOf(1001, 1002, 1004, 1005, 1006, 1007, 1008, 1009, 1011, 1012))
    }

    val adapter = object : RecyclerView.Adapter<Holder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            return Holder(FrameLayout(this@LockScreenActivity))
        }

        override fun getItemCount(): Int {
            return dataList.size
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.bind(dataList[position])
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                    or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
        }

        setContentView(R.layout.activity_lock)

        layoutManager = LinearLayoutManager(this)
        rv_union_content.layoutManager = layoutManager
        rv_union_content.adapter = adapter

        rv_union_content.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
                if (visibleItemCount > 0 && !isLoading && visibleItemCount + firstVisiblePosition >= totalItemCount - 5) {
                    requestContentData()
                }
            }
        })

        if (dataList.isEmpty()) {
            requestContentData()
        }
    }

    private fun requestContentData() {
        isLoading = true

        if (page == 1) {
            showLoading()
        }

        StarMedia.loadContent(this, params, page, object : IContent.Listener {
            override fun onSuccess(list: List<ContentItem>) {
                page++
                this@LockScreenActivity.apply {
                    hideLoading()
                    val star = dataList.size
                    dataList.addAll(list)
                    adapter.notifyItemRangeInserted(star, dataList.size)
                }
                isLoading = false
            }

            override fun onError(message: String) {
                this@LockScreenActivity.apply {
                    hideLoading()
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
                isLoading = false
            }
        })
    }

    inner class Holder(private val container: FrameLayout) : RecyclerView.ViewHolder(container) {
        fun bind(item: ContentItem) {
            container.removeAllViews()

            item.onShow()

            when (item.type) {
                ContentItem.Type.NEWS -> {
                    layoutInflater.inflate(R.layout.item_content_text, container, true)

                    val contentNews = item.getContentNews()!!

                    container.setOnClickListener {
                        item.onClick(this@LockScreenActivity)
                    }

                    container.tv_content_title.text = contentNews.title
                    container.tv_content_source.text = contentNews.source
                    container.tv_content_read.text =
                        MessageFormat.format("{0}阅读", contentNews.readCounts ?: 0)
                    container.tv_content_time.text = formatDate(contentNews.updateTime ?: "")
                }
                ContentItem.Type.IMAGE -> {
                    layoutInflater.inflate(R.layout.item_content_image, container, true)

                    val contentImage = item.getContentImage()!!

                    container.setOnClickListener {
                        item.onClick(this@LockScreenActivity)
                    }

                    container.tv_content_title.text = contentImage.title
                    container.tv_content_image_count.text = contentImage.colImageCount?.toString()
                    container.tv_content_source.text = contentImage.source?.name
                    container.tv_content_read.text =
                        MessageFormat.format("{0}阅读", contentImage.readCounts ?: 0)
                    container.tv_content_time.text = formatDate(contentImage.updateTime ?: "")

                    Glide.with(container).load(contentImage.loadImage())
                        .into(container.iv_content_image)
                }
                ContentItem.Type.VIDEO -> {
                    layoutInflater.inflate(R.layout.item_content_video, container, true)

                    val contentVideo = item.getContentVideo()!!

                    container.setOnClickListener {
                        item.onClick(this@LockScreenActivity)
                    }

                    container.tv_content_title?.text = contentVideo.title
                    container.tv_video_duration?.text =
                        formatDuration(contentVideo.duration ?: 0)
                    container.tv_content_source?.text = contentVideo.source?.name
                    container.tv_content_time?.text = formatDate(contentVideo.updateTime ?: "")
                    container.tv_content_video_play_count?.text =
                        MessageFormat.format("{0}次播放", contentVideo.playCounts ?: 0)

                    Glide.with(container).load(contentVideo.loadImage())
                        .into(container.iv_content_video_picture)
                }
            }
        }
    }
}