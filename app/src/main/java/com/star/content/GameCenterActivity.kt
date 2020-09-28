package com.star.content

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.star.content.fragment.GameCenterFragment
import com.star.content.fragment.TestFragment
import kotlinx.android.synthetic.main.activity_game_center.*

class GameCenterActivity : BaseActivity() {

    val tabs = mapOf(
            Pair("娱乐", 1001),
            Pair("体育", 1002),
            Pair("小游戏", 1003),
            Pair("IT", 1004),
            Pair("手机", 1005),
            Pair("财经", 1006),
            Pair("汽车", 1007),
            Pair("房产", 1008),
            Pair("时尚", 1009),
            Pair("文化", 1011),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_center)

        tabs.forEach {
            val newTab = tl_game_center.newTab()
            newTab.text = it.key
            newTab.tag = it.value
            tl_game_center.addTab(newTab)
        }

        tl_game_center.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                vp_game_center.currentItem = tab.position
            }
        })

        vp_game_center.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tl_game_center))

        vp_game_center.adapter = object : FragmentPagerAdapter(
                supportFragmentManager,
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {
            override fun getItem(position: Int): Fragment {

                return if (position == 2) {
                    GameCenterFragment()
                } else {
                    val fragment = TestFragment()

                    val bundle = Bundle()

                    bundle.putString("Title", tabs.entries.toList()[position].key)

                    fragment.arguments = bundle

                    fragment
                }
            }


            override fun getCount(): Int {
                return tabs.size
            }
        }

    }
}