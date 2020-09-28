package com.star.content.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.star.content.R
import com.star.content.adapter.BannerAdapter
import com.star.content.bean.Banner
import com.starmedia.tinysdk.StarMedia
import kotlinx.android.synthetic.main.frag_game_center.view.*

class GameCenterFragment : Fragment() {

    private val banners = ArrayList<Banner>().apply {
        add(Banner(R.drawable.icon_banner_first, "这是Banner"))
        add(Banner(R.drawable.icon_game_10001_banner, "小游戏中心"))
        add(Banner(R.drawable.icon_banner_second, "这是Banner"))
        add(Banner(R.drawable.icon_banner_third, "这是Banner"))
        add(Banner(R.drawable.icon_banner_fourth, "这是Banner"))
    }

    val adapter: BannerAdapter by lazy {
        BannerAdapter(banners) { banner ->
            if (banner.name == "小游戏中心") {
                StarMedia.openStarGame(requireActivity()) {
                    if (!it) {
                        Toast.makeText(requireActivity(), "开发游戏失败", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), banner.name, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private var current = 0

    private val handler = Handler()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_game_center, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.cl_game_center.setOnClickListener {
            StarMedia.openStarGame(requireActivity()) {
                if (!it) {
                    Toast.makeText(requireActivity(), "开发游戏失败", Toast.LENGTH_LONG).show()
                }
            }
        }

        view.tv_game_action.setOnClickListener {
            StarMedia.openStarGame(requireActivity()) {
                if (!it) {
                    Toast.makeText(requireActivity(), "开发游戏失败", Toast.LENGTH_LONG).show()
                }
            }
        }

        view.rv_information_banner.adapter = adapter

        current = view.rv_information_banner.currentItem

        handler.postDelayed({
            current += 1
            view.rv_information_banner.setCurrentItem(current, true)
        }, 1000)
    }
}