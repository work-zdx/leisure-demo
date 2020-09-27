package com.star.content

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.starmedia.tinysdk.StarMedia
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        bu_demo_flow_lock.setOnClickListener {
            startActivity(Intent(this, FlowLockPromptActivity::class.java))
        }

        bu_demo_flow_other.setOnClickListener {
            startActivity(Intent(this, FlowContentActivity::class.java))
        }

        bu_demo_flow_home.setOnClickListener {
            startActivity(Intent(this, FlowMainActivity::class.java))
        }

        bu_demo_game_center.setOnClickListener {
            startActivity(Intent(this, GameCenterActivity::class.java))
        }

        bu_demo_game_single.setOnClickListener {
            startActivity(Intent(this, GameSingleActivity::class.java))
        }

        bu_demo_game_native.setOnClickListener {
            startActivity(Intent(this, GameNativeActivity::class.java))
        }

        bu_demo_game_reward.setOnClickListener {
            startActivity(Intent(this, GameRewardActivity::class.java))
        }
    }
}