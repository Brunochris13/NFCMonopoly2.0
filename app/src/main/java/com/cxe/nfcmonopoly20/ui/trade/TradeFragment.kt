package com.cxe.nfcmonopoly20.ui.trade

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.cxe.nfcmonopoly20.databinding.FragmentTradeBinding
import com.cxe.nfcmonopoly20.logic.AppViewModel
import com.cxe.nfcmonopoly20.logic.player.CardId
import com.cxe.nfcmonopoly20.logic.player.Player

private const val LOG_TAG = "TradeFragment"

class TradeFragment : Fragment() {

    // Binding
    private var _binding: FragmentTradeBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val viewModel: AppViewModel by activityViewModels()

    // Up Down Button Status
    private var player1Pays = MutableLiveData(true)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Binding
        _binding = FragmentTradeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bind lifecycle Owner
        binding.lifecycleOwner = viewLifecycleOwner

        // Bind player1Pays variable
        binding.player1Pays = player1Pays

        // Up and Down Button
        binding.upDownBtn.setOnClickListener {
            player1Pays.value = !player1Pays.value!!
        }

        // Player 1 Delete Button
        binding.player1DeleteBtn.setOnClickListener {
            binding.player1 = null
        }

        // Player 2 Delete Button
        binding.player2DeleteBtn.setOnClickListener {
            binding.player2 = null
        }

        // Trade Button
        binding.tradeBtn.setOnClickListener {
            val player1 = binding.player1
            val player2 = binding.player2

            // Check if we have 2 Players ready to Trade
            if (player1 == null && player2 == null) {
                Toast.makeText(context, "Need 2 players to Trade", Toast.LENGTH_SHORT).show()
            } else if (player1 == null || player2 == null) {
                Toast.makeText(context, "Need 1 more player to Trade", Toast.LENGTH_SHORT).show()
            } else {
                val text = binding.tradeAmountTextInput.editText?.text.toString()
                // Check if Input Text is Empty
                if (text != "") {
                    // Trade Money
                    val result = if (!player1Pays.value!!)
                        viewModel.playerPaysPlayer(player1, player2, text.toInt())
                    else
                        viewModel.playerPaysPlayer(player2, player1, text.toInt())

                    // Check if Trade was successful
                    if (result) {
                        binding.tradeAmountTextInput.editText?.setText("")
                        Toast.makeText(context, "Trade Successful", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Trade NOT Successful", Toast.LENGTH_SHORT).show()
                        Log.e(LOG_TAG, "Trade NOT Successful, player1 = ${player1.name}, player2 = ${player2.name}, text = $text)")
                    }
                }
            }
        }
    }

    private fun bindPlayer(player: Player) {
        when {
            binding.player1 == player || binding.player2 == player -> {
                Toast.makeText(context, "Player is already added", Toast.LENGTH_SHORT).show()
                Log.i(LOG_TAG, "Player is already added, player = ${player.name}")
            }
            binding.player1 == null -> binding.player1 = player
            binding.player2 == null -> binding.player2 = player
            else -> {
                Toast.makeText(context, "Cannot add a 3rd Player", Toast.LENGTH_SHORT).show()
                Log.i(LOG_TAG, "Cannot add a 3rd Player, player = ${player.name}")
            }
        }
    }

    fun onNewIntent(msg: String) {
        // Check if it is a Debit Card
        if (viewModel.isCardId(msg)) {
            val cardId = CardId.valueOf(msg)

            // Check if Player exists
            if (!playerExists(cardId))
                return

            // Bind Player
            bindPlayer(viewModel.playerMap[cardId]!!)

        } else {
            Toast.makeText(context, "Wrong Card", Toast.LENGTH_SHORT).show()
            Log.i(LOG_TAG, "Wrong Card")
        }
    }

    private fun playerExists(cardId: CardId): Boolean {
        // Check if player exists with this cardId
        return if (!viewModel.playerMap.containsKey(cardId)) {
            Toast.makeText(context, "Player does not exist with this card", Toast.LENGTH_SHORT)
                .show()
            Log.e(LOG_TAG, "Player does not exist with this cardId, cardId = $cardId")
            false
        } else
            true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}