package com.cxe.nfcmonopoly20.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cxe.nfcmonopoly20.R
import com.cxe.nfcmonopoly20.databinding.FragmentHomeBinding
import com.cxe.nfcmonopoly20.logic.*
import com.cxe.nfcmonopoly20.logic.player.CardId
import com.cxe.nfcmonopoly20.logic.player.Player

class HomeFragment : Fragment() {

    // Constants
    private val LOG_TAG = "HomeFragment"
    private val DIALOG_TAG = "dialog_tag"

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

        // Fill up the player list
        viewModel.addPlayer(Player("Christos", CardId.YELLOW_CARD))
        viewModel.addPlayer(Player("Fakontis", CardId.GREEN_CARD))
        viewModel.addPlayer(Player("Panagiotis", CardId.BLUE_CARD))

        // RecyclerView
        val recyclerView = binding.recyclerviewHome
        val recyclerViewAdapter = PlayerListAdapter(viewModel.playerList, viewModel.playerMap)

        recyclerViewAdapter.onEdit = { position ->
            val player = viewModel.playerList[position]

            // Open the EditPlayer dialog
            editPlayerNameDialog(player.cardId, player.name, recyclerViewAdapter, position)

            recyclerViewAdapter.notifyItemChanged(position)
        }
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        // Start Button
        binding.startBtn.setOnClickListener {
            // Set Mega Status
            viewModel.mega = binding.megaSwitch.isChecked

            // Set the starting money for all Players
            val startingMoney = if (viewModel.mega) STARTING_MONEY_MEGA else STARTING_MONEY
            for (player in viewModel.playerList) {
                player.money = startingMoney
            }

            // Navigate to GameFragment
            findNavController().navigate(R.id.action_HomeFragment_to_gameFragment)
        }
    }

    fun onNewIntent(msg: String) {
        // Check if it is a Debit Card
        if (viewModel.isCardId(msg)) {
            val cardId = CardId.valueOf(msg)

            // Check if player with this card exists
            if (viewModel.playerMap.containsKey(cardId)) {
                Toast.makeText(context, "Card already added", Toast.LENGTH_SHORT).show()
                Log.i(LOG_TAG, "Card already added")
                return
            }

            // Open the EditPlayer dialog
            editPlayerNameDialog(cardId)

        } else {
            Toast.makeText(context, "Wrong Card", Toast.LENGTH_SHORT).show()
            Log.i(LOG_TAG, "Wrong Card")
        }
    }

    private fun editPlayerNameDialog(
        cardId: CardId,
        name: String? = null,
        adapter: PlayerListAdapter? = null,
        position: Int? = null
    ) {
        // Arguments
        val bundle = Bundle()
        bundle.putSerializable(CARD_ID_TAG, cardId)
        if (name != null) {
            bundle.putString(PLAYER_NAME_TAG, name)
        }

        // Dialog
        val dialog = EditPlayerDialogFragment()
        dialog.arguments = bundle

        if ((adapter != null) and (position != null)) {
            dialog.onDismissListener = {
                adapter?.notifyItemChanged(position!!)
            }
        }

        dialog.show(parentFragmentManager, DIALOG_TAG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}