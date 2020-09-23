package com.star.content

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.starmedia.pojos.ContentItem
import com.starmedia.tinysdk.IContent
import com.starmedia.tinysdk.StarMedia
import kotlinx.android.synthetic.main.frag_content.view.*
import kotlinx.android.synthetic.main.item_content_image.view.iv_content_image
import kotlinx.android.synthetic.main.item_content_image.view.tv_content_image_count
import kotlinx.android.synthetic.main.item_content_text.view.tv_content_read
import kotlinx.android.synthetic.main.item_content_text.view.tv_content_source
import kotlinx.android.synthetic.main.item_content_text.view.tv_content_time
import kotlinx.android.synthetic.main.item_content_text.view.tv_content_title
import kotlinx.android.synthetic.main.item_content_video.view.*
import java.text.MessageFormat

class ContentFragment : Fragment() {

    companion object {
        const val KEY = "key"
    }

    lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.frag_content, container, false)
        return rootView
    }

    var page = 1

    var isLoading = false

    data class DataWrapper(val data: ContentItem? = null, val isAd: Boolean = false)

    val dataList = ArrayList<DataWrapper>()

    inner class Holder(private val container: FrameLayout) :
        RecyclerView.ViewHolder(container) {
        fun bind(data: DataWrapper) {
            container.removeAllViews()

            if (data.isAd) {
                //加载广告
                layoutInflater.inflate(R.layout.item_ad, container, true)
                container.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            } else {
                val item = data.data!!
                //上报条目曝光
                item.onShow()

                when (item.type) {
                    ContentItem.Type.NEWS -> {
                        layoutInflater.inflate(R.layout.item_content_text, container, true)

                        val contentNews = item.getContentNews()!!

                        container.setOnClickListener {
                            item.onClick(requireActivity())
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
                            item.onClick(requireActivity())
                        }

                        container.tv_content_title.text = contentImage.title
                        container.tv_content_image_count.text =
                            contentImage.colImageCount?.toString()
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
                            item.onClick(requireActivity())
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

    val adapter = object : RecyclerView.Adapter<Holder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            return Holder(FrameLayout(requireContext()))
        }

        override fun getItemCount(): Int {
            return dataList.size
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.bind(dataList[position])
        }
    }

    lateinit var layoutManager: LinearLayoutManager

    val params = HashMap<Long, List<Int>>()
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val key = arguments!!.getInt(KEY)


        params[0] = arrayListOf(key) //新闻
        params[1] = arrayListOf(key) //图集
        params[2] = arrayListOf(key) //视频


        layoutManager = LinearLayoutManager(requireContext())
        rootView.recy_union_content.layoutManager = layoutManager
        rootView.recy_union_content.adapter = adapter

        rootView.recy_union_content.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
        rootView.btn_reload.visibility = View.GONE

        if (userVisibleHint && page == 1) {
            (requireActivity() as BaseActivity).showLoading()
        }

        StarMedia.loadContent(activity!!, params, page, object : IContent.Listener {
            override fun onSuccess(list: List<ContentItem>) {
                page++
                activity?.apply {
                    if (userVisibleHint) {
                        (this as BaseActivity).hideLoading()
                    }
                    fillItems(list)
                }
                isLoading = false
            }

            override fun onError(message: String) {
                activity?.apply {
                    if (userVisibleHint) {
                        (this as BaseActivity).hideLoading()
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    }

                    if (page == 1) {
                        rootView.btn_reload.visibility = View.VISIBLE
                        rootView.btn_reload.setOnClickListener {
                            runOnUiThread {
                                requestContentData()
                            }
                        }
                    }
                }
                isLoading = false
            }
        })
    }

    //每隔多少条插入广告
    private val AD_INTERVAL = 2;

    private var lastLeftItemCount = 0

    private fun fillItems(
        list: List<ContentItem>
    ) {
        val tempList = ArrayList(list.map { DataWrapper(data = it) })
            val firstInterval = 0.coerceAtLeast(AD_INTERVAL - lastLeftItemCount)

            val adCount = (tempList.size - firstInterval) / AD_INTERVAL + 1

            lastLeftItemCount = (tempList.size - firstInterval) % AD_INTERVAL

            tempList.add(firstInterval, DataWrapper(isAd = true))

            for (i in 2..adCount) {
                tempList.add(
                    i * (AD_INTERVAL + 1) - 1 - (AD_INTERVAL - firstInterval),
                    DataWrapper(isAd = true)
                )
            }

        val star = dataList.size
        dataList.addAll(tempList)
        adapter.notifyItemRangeInserted(star, dataList.size)
    }
}