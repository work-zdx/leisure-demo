package com.star.content

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.star.content.adapter.ContentAdapter
import com.star.content.bean.DataWrapper
import com.starmedia.pojos.ContentItem
import com.starmedia.tinysdk.IContent
import com.starmedia.tinysdk.StarMedia
import kotlinx.android.synthetic.main.activity_flow_content.*

class FlowContentActivity: BaseActivity() {

    var page = 1

    var isLoading = false

    val dataList = ArrayList<DataWrapper>()

    lateinit var layoutManager: LinearLayoutManager

    private var params = HashMap<Long, List<Int>>().apply {
        put(0, arrayListOf(1001, 1002, 1004, 1005, 1006, 1007, 1008, 1009, 1011, 1012))
        put(1, arrayListOf(1001, 1002, 1004, 1005, 1006, 1007, 1008, 1009, 1011, 1012))
        put(2, arrayListOf(1001, 1002, 1004, 1005, 1006, 1007, 1008, 1009, 1011, 1012))
    }

    private val adapter : ContentAdapter by lazy {
        ContentAdapter(this, dataList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow_content)

        layoutManager = LinearLayoutManager(this)
        rv_flow_content.layoutManager = layoutManager
        rv_flow_content.adapter = adapter

        rv_flow_content.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                this@FlowContentActivity.apply {
                    hideLoading()
                    fillItems(list)
                }
                isLoading = false
            }

            override fun onError(msg: String) {
                this@FlowContentActivity.apply {
                    hideLoading()
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                }
                isLoading = false
            }
        })
    }

    //每隔多少条插入广告
    private val interval = 2

    private var lastLeftItemCount = 0

    private fun fillItems(
            list: List<ContentItem>
    ) {
        val tempList = ArrayList(list.map { DataWrapper(data = it) })
        val firstInterval = 0.coerceAtLeast(interval - lastLeftItemCount)

        val adCount = (tempList.size - firstInterval) / interval + 1

        lastLeftItemCount = (tempList.size - firstInterval) % interval

        tempList.add(firstInterval, DataWrapper(isAd = true))

        for (i in 2..adCount) {
            tempList.add(
                    i * (interval + 1) - 1 - (interval - firstInterval),
                    DataWrapper(isAd = true)
            )
        }

        val star = dataList.size
        dataList.addAll(tempList)
        adapter.notifyItemRangeInserted(star, dataList.size)
    }
}