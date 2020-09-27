package com.star.content.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.star.content.R
import com.star.content.bean.Game
import com.star.content.viewHolder.GameHolder
import com.star.content.viewHolder.GameNativeHolder

class GameAdapter(private val games: List<Game>, private val native: Boolean = false, private val listener: ((Game) -> Unit)?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (native) {
            return GameNativeHolder( LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_game_native, parent, false))
        }
        return GameHolder( LayoutInflater.from(parent.context)
                .inflate(R.layout.item_game, parent, false))
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val game = games[position]

        if (viewHolder is GameHolder) {
            viewHolder.bind(game) {
                listener?.invoke(it)
            }
            return
        } else if (viewHolder is GameNativeHolder) {
            viewHolder.bind(game) {
                listener?.invoke(it)
            }
        }
    }

    override fun getItemCount(): Int {
       return games.size
    }
}