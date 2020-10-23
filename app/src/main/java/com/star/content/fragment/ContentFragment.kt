package com.star.content.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.star.content.BaseActivity
import com.star.content.R
import com.star.content.adapter.ContentAdapter
import com.star.content.bean.DataWrapper
import com.starmedia.pojos.ContentItem
import com.starmedia.tinysdk.IContent
import com.starmedia.tinysdk.StarMedia
import kotlinx.android.synthetic.main.frag_content.view.*

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

    private val dataList = ArrayList<DataWrapper>()

    private val adapter: ContentAdapter by lazy {
        ContentAdapter(requireActivity(), dataList)
    }

    lateinit var layoutManager: LinearLayoutManager

    val params = HashMap<Long, List<Int>>()
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val key = arguments?.getInt(KEY) ?: 9999

        if (key == 1000) {
            params[0L] = arrayListOf(1001, 1002, 1004, 1005, 1006, 1007, 1008, 1009, 1011, 1012)
            params[1L] = arrayListOf(1001, 1002, 1004, 1005, 1006, 1007, 1008, 1009, 1011, 1012)
            params[2L] = arrayListOf(1001, 1002, 1004, 1005, 1006, 1007, 1008, 1009, 1011, 1012)
        } else {
            params[0L] = arrayListOf(key) //新闻
            params[1L] = arrayListOf(key) //图集
            params[2L] = arrayListOf(key) //视频
        }

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

        StarMedia.loadContent(requireActivity(), params, page, object : IContent.Listener {
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

            override fun onError(msg: String) {
                activity?.apply {
                    if (userVisibleHint) {
                        (this as BaseActivity).hideLoading()
                        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
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