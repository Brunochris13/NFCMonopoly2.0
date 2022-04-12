package com.cxe.nfcmonopoly20.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cxe.nfcmonopoly20.databinding.FragmentHomeBinding
import com.cxe.nfcmonopoly20.logic.AppViewModel
import com.cxe.nfcmonopoly20.logic.STARTING_MONEY
import com.cxe.nfcmonopoly20.logic.STARTING_MONEY_MEGA
import com.cxe.nfcmonopoly20.logic.player.CardId
import com.cxe.nfcmonopoly20.logic.player.Player

class HomeFragment : Fragment() {

    // Binding
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val viewModel: AppViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ViewModel Creation
        binding.viewModel = viewModel

        val startingMoney = if (viewModel.mega) STARTING_MONEY_MEGA else STARTING_MONEY

        // Fill up the player list
        viewModel.addPlayer(Player("Christos", CardId.YELLOW_CARD, startingMoney))
        viewModel.addPlayer(Player("Fakontis", CardId.GREEN_CARD, startingMoney))
        viewModel.addPlayer(Player("Panagiotis", CardId.BLUE_CARD, startingMoney))

        // RecyclerView
        val recyclerView = binding.recyclerviewHome
        val recyclerViewAdapter = PlayerListAdapter(viewModel.playerList) {

        }
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        binding.startBtn.setOnClickListener {
            Toast.makeText(
                context,
                "PlayerList size: ${viewModel.playerList.size}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}