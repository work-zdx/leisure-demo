package com.star.content

import android.os.Bundle
import android.widget.Toast
import com.starmedia.tinysdk.StarMedia
import kotlinx.android.synthetic.main.activity_game_reward.*

class GameRewardActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_game_reward)

        cl_game_info.setOnClickListener {
            StarMedia.openStarGame(this, "{\"game_id\":10004, \"menu\":false}") {
                if (!it) {
                    Toast.makeText(this, "开发游戏失败", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}