package com.star.content.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.star.content.bean.Game
import kotlinx.android.synthetic.main.item_game_native.view.*

class GameNativeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(game: Game, listener: ((Game) -> Unit)?) = with(itemView) {

        cl_game_native_item.setOnClickListener {
            listener?.invoke(game)
        }

        iv_game_native_logo.setImageResource(game.logo)

        tv_game_native_name.text = game.name
    }
}