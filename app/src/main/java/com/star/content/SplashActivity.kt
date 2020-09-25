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
        btn_content.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }

        btn_game.setOnClickListener {
            StarMedia.openStarGame(this){
                if(!it){
                    Toast.makeText(this, "开发游戏失败", Toast.LENGTH_LONG).show()
                }
            }
        }

        btn_game_with_id.setOnClickListener {
            StarMedia.openStarGame(this, "{\"game_id\":10007, \"menu\":false}"){
                if(!it){
                    Toast.makeText(this, "开发游戏失败", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}