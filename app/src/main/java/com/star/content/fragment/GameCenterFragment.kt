package com.star.content.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.star.content.R
import com.starmedia.tinysdk.StarMedia
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.frag_game_center.view.*

class GameCenterFragment : Fragment() {

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

        view.rv_information_banner.setOnClickListener{
            StarMedia.openStarGame(requireActivity(), "{\"game_id\":10007, \"menu\":false}") {
                if (!it) {
                    Toast.makeText(requireActivity(), "开发游戏失败", Toast.LENGTH_LONG).show()
                }
            }
        }

        view.img_float.setOnClickListener {
            StarMedia.openStarGame(requireActivity()) {
                if (!it) {
                    Toast.makeText(requireActivity(), "开发游戏失败", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}