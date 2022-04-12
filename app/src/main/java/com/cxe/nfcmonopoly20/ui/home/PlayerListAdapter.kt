package com.cxe.nfcmonopoly20.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cxe.nfcmonopoly20.databinding.FragmentHomePlayerListItemBinding
import com.cxe.nfcmonopoly20.logic.player.Player

class PlayerListAdapter(private val playerList: List<Player>) : RecyclerView.Adapter<PlayerListAdapter.PlayerListViewHolder>() {

    class PlayerListViewHolder(
        private val binding: FragmentHomePlayerListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(player: Player) {
            binding.player = player
        }
    }

    private lateinit var binding: FragmentHomePlayerListItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerListViewHolder {
        binding = FragmentHomePlayerListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayerListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayerListViewHolder, position: Int) {
        holder.bind(playerList[position])
    }

    override fun getItemCount(): Int = playerList.size
}