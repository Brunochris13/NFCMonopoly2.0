package com.cxe.nfcmonopoly20.ui.game

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cxe.nfcmonopoly20.databinding.FragmentGamePlayerListItemBinding
import com.cxe.nfcmonopoly20.logic.player.Player

class GamePlayerListAdapter(
    private val playerList: MutableList<Player>,
    private val onClickListener: (Int) -> Unit
) :
    RecyclerView.Adapter<GamePlayerListAdapter.PlayerListViewHolder>() {

    inner class PlayerListViewHolder(
        private val binding: FragmentGamePlayerListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(player: Player) {
            binding.player = player

            // Money Button
            with(binding.playerMoney) {
                if (player.moneyShown) {
                    text = "€${player.money}"
                }
                setOnClickListener {
                    text = if (!player.moneyShown) {
                        "€${player.money}"
                    } else {
                        ""
                    }
                    player.moneyShown = !player.moneyShown
                }
            }

            binding.executePendingBindings()
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

        binding.playerName.setOnClickListener {
            onClickListener(position)
        }
    }

    override fun getItemCount(): Int = playerList.size
}