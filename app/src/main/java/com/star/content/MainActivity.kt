package com.star.content

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    val tabs = mapOf(
        Pair("娱乐", 1001),
        Pair("体育", 1002),
        Pair("IT", 1004),
        Pair("手机", 1005),
        Pair("财经", 1006),
        Pair("汽车", 1007),
        Pair("房产", 1008),
        Pair("时尚", 1009),
        Pair("文化", 1011),
        Pair("军事", 1012),
        Pair("科技", 1013),
        Pair("健康", 1014),
        Pair("母婴", 1015),
        Pair("社会", 1016),
        Pair("美食", 1017),
        Pair("家居", 1018),
        Pair("游戏", 1019),
        Pair("历史", 1020),
        Pair("时政", 1021),
        Pair("美女", 1024),
        Pair("搞笑", 1025),
        Pair("猎奇", 1026),
        Pair("旅游", 1027),
        Pair("动物", 1028),
        Pair("摄影", 1030),
        Pair("动漫", 1031),
        Pair("女人", 1032),
        Pair("生活", 1033),
        Pair("表演", 1034),
        Pair("音乐", 1036),
        Pair("影视周边", 1037),
        Pair("相声小品", 1039),
        Pair("舞蹈", 1040),
        Pair("安全出行", 1041),
        Pair("大自然", 1042),
        Pair("其他", 9999)
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        tabs.forEach {
            val newTab = tl_content_header.newTab()
            newTab.text = it.key
            newTab.tag = it.value
            tl_content_header.addTab(newTab)
        }

        tl_content_header.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                vp_content_result.currentItem = tab.position
            }
        })

        vp_content_result.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(
                tl_content_header
            )
        )

        vp_content_result.adapter = object : FragmentPagerAdapter(
            supportFragmentManager,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {
            override fun getItem(position: Int): Fragment {
                val fragment = ContentFragment()

                val bundle = Bundle()
                bundle.putInt(
                    ContentFragment.KEY,
                    tabs.entries.toList()[position].value
                )

                fragment.arguments = bundle

                return fragment
            }


            override fun getCount(): Int {
                return tabs.size
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}