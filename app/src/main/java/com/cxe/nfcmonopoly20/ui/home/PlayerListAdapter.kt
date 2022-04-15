package com.cxe.nfcmonopoly20.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cxe.nfcmonopoly20.databinding.FragmentHomePlayerListItemBinding
import com.cxe.nfcmonopoly20.logic.player.CardId
import com.cxe.nfcmonopoly20.logic.player.Player

class PlayerListAdapter(
    private val playerList: MutableList<Player>,
    private val playerMap: MutableMap<CardId, Player>,
    ) : RecyclerView.Adapter<PlayerListAdapter.PlayerListViewHolder>() {

    inner class PlayerListViewHolder(
        private val binding: FragmentHomePlayerListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(player: Player) {
            binding.player = player
        }
    }

    private lateinit var binding: FragmentHomePlayerListItemBinding

    // Edit Button
    lateinit var onEdit: (Int) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerListViewHolder {
        binding = FragmentHomePlayerListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayerListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayerListViewHolder, position: Int) {
        holder.bind(playerList[position])

        // Player Delete Button
        binding.playerDeleteBtn.setOnClickListener {
            val player = playerList[position]
            playerMap.remove(player.cardId)
            playerList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, playerList.size)
        }

        // Player Edit Button
        binding.playerEditBtn.setOnClickListener {
            onEdit(position)
        }
    }

    override fun getItemCount(): Int = playerList.size
}