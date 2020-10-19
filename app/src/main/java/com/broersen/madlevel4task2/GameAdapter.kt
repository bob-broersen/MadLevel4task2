package com.broersen.madlevel4task2

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.broersen.madlevel4task2.GameFragment.Companion.moves
import kotlinx.android.synthetic.main.fragment_game.view.*
import kotlinx.android.synthetic.main.item_game.view.*
import java.util.*

class GameAdapter (private val gameItems: List<Game>) : RecyclerView.Adapter<GameAdapter.ViewHolder>(){
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun dataBind(item: Game){
            itemView.tv_date.text = item.date
            itemView.tv_results.text = item.result.replace(':', ' ')

            var actionDrawable: Drawable? = getImageOfMove(item.computerMove)

            if(actionDrawable != null){
                itemView.iv_computer.setImageDrawable(actionDrawable)
            }

            actionDrawable = getImageOfMove(item.playerMove)
            if(actionDrawable != null)
                itemView.iv_player.setImageDrawable(actionDrawable)
        }


        fun getImageOfMove(move: String) : Drawable?{
            when (move) {
                moves[GameFragment.choiceRock]["move"] -> {
                    return ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.rock
                    )
                }
                moves[GameFragment.choiceScissor]["move"] -> {
                    return ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.scissors
                    )
                }
                moves[GameFragment.choicePaper]["move"] -> {
                    return ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.paper
                    )
                }
                else -> {
                    return null
                }
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return gameItems.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBind(gameItems[position])
    }


}