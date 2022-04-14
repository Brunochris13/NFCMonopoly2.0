package com.cxe.nfcmonopoly20.ui.game

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cxe.nfcmonopoly20.databinding.FragmentGamePlayerListItemBinding
import com.cxe.nfcmonopoly20.logic.player.Player

class GamePlayerListAdapter(private val playerList: List<Player>) :
    RecyclerView.Adapter<GamePlayerListAdapter.PlayerListViewHolder>() {

    class PlayerListViewHolder(
        private val binding: FragmentGamePlayerListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(player: Player) {
            binding.player = player

            // Money Button
            with(binding.playerMoney) {
                if (text == "") {
                    text = "€${player.money}"
                }
                setOnClickListener {
                    text = if (text == "") {
                        "€${player.money}"
                    } else {
                        ""
                    }
                }
            }
        }
    }

    private lateinit var binding: FragmentGamePlayerListItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerListViewHolder {
        binding = FragmentGamePlayerListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlayerListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayerListViewHolder, position: Int) {
        holder.bind(playerList[position])
    }

    override fun getItemCount(): Int = playerList.size
}