package com.cxe.nfcmonopoly20.ui.game.trade

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cxe.nfcmonopoly20.databinding.FragmentTradeBinding
import com.cxe.nfcmonopoly20.logic.AppViewModel
import com.cxe.nfcmonopoly20.logic.player.CardId
import com.cxe.nfcmonopoly20.logic.player.Player
import com.cxe.nfcmonopoly20.logic.property.ColorProperty
import com.cxe.nfcmonopoly20.logic.property.Property
import com.cxe.nfcmonopoly20.logic.property.PropertyId
import com.cxe.nfcmonopoly20.logic.property.StationProperty
import com.cxe.nfcmonopoly20.ui.game.PlayerPropertyListAdapter

private const val LOG_TAG = "TradeFragment"

class TradeFragment : Fragment() {

    // Binding
    private var _binding: FragmentTradeBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val viewModel: AppViewModel by activityViewModels()

    // Up Down Button Status
    private var player1Pays = MutableLiveData(true)

    // Property List Adapters
    private var player1Properties = mutableListOf<Property>()
    private var player2Properties = mutableListOf<Property>()
    private lateinit var player1RecyclerViewAdapter: PlayerPropertyListAdapter
    private lateinit var player2RecyclerViewAdapter: PlayerPropertyListAdapter

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

        // RecyclerViews
        buildRecyclerView(player1 = true)
        buildRecyclerView(player1 = false)

        // Up and Down Button
        binding.upDownBtn.setOnClickListener {
            player1Pays.value = !player1Pays.value!!
        }

        // Player 1 Delete Button
        binding.player1DeleteBtn.setOnClickListener {
            binding.player1 = null
            clearRecyclerView(player1 = true)
        }

        // Player 2 Delete Button
        binding.player2DeleteBtn.setOnClickListener {
            binding.player2 = null
            clearRecyclerView(player1 = false)
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
                var tradeMoneyResult = true
                // Check if Input Text is Empty
                if (text != "") {
                    // Trade Money
                    tradeMoneyResult = if (!player1Pays.value!!)
                        viewModel.playerPaysPlayer(player1, player2, text.toInt())
                    else
                        viewModel.playerPaysPlayer(player2, player1, text.toInt())
                }

                // Trade Properties
                val tradePropertiesResult = viewModel.playersTradeProperties(
                    player1,
                    player1Properties,
                    player2,
                    player2Properties
                )

                // Check if Trade was Successful
                if (tradeMoneyResult && tradePropertiesResult) {
                    binding.tradeAmountTextInput.editText?.setText("")
                    swapRecyclerView()
                    Toast.makeText(context, "Trade Successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Trade NOT Successful", Toast.LENGTH_SHORT).show()
                    Log.e(
                        LOG_TAG,
                        "Trade NOT Successful, player1 = ${player1.name}, player2 = ${player2.name}, text = $text)"
                    )
                }
            }
        }
    }

    private fun clearRecyclerView(player1: Boolean) {
        if (player1) {
            val size = player1Properties.size
            player1Properties.clear()
            player1RecyclerViewAdapter.notifyItemRangeRemoved(0, size)
        } else {
            val size = player2Properties.size
            player2Properties.clear()
            player2RecyclerViewAdapter.notifyItemRangeRemoved(0, size)
        }
    }

    private fun swapRecyclerView() {
        val tempPropertyList = player1Properties
        player1Properties = player2Properties
        player2Properties = tempPropertyList

        // Update RecyclerViews
        player1RecyclerViewAdapter.updateList(player1Properties)
        player2RecyclerViewAdapter.updateList(player2Properties)
    }

    private fun buildRecyclerView(player1: Boolean) {
        val recyclerView: RecyclerView
        val adapter: PlayerPropertyListAdapter

        if (player1) {
            recyclerView = binding.player1Recyclerview
            player1RecyclerViewAdapter = PlayerPropertyListAdapter(player1Properties) { property ->
                removeProperty(property, player1 = true)
            }
            adapter = player1RecyclerViewAdapter
        } else {
            recyclerView = binding.player2Recyclerview
            player2RecyclerViewAdapter = PlayerPropertyListAdapter(player2Properties) { property ->
                removeProperty(property, player1 = false)
            }
            adapter = player2RecyclerViewAdapter
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context, 3)
    }

    private fun removeProperty(property: Property, player1: Boolean) {
        val propertyList: MutableList<Property>
        val adapter: PlayerPropertyListAdapter

        if (player1) {
            propertyList = player1Properties
            adapter = player1RecyclerViewAdapter
        } else {
            propertyList = player2Properties
            adapter = player2RecyclerViewAdapter
        }

        // Remove it from the Property List
        val position = propertyList.indexOf(property)
        propertyList.removeAt(position)

        // Update RecyclerView
        adapter.notifyItemRemoved(position)
        adapter.notifyItemRangeChanged(position, propertyList.size)
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
        // Check if it is a Property Card
        if (viewModel.isProperty(msg)) {
            //Check if Both Players are Null
            if (binding.player1 == null && binding.player2 == null) {
                Toast.makeText(context, "Need 2 players to Trade", Toast.LENGTH_SHORT).show()
                return
            }

            val propertyId = PropertyId.valueOf(msg)

            // Check if Property exists in the ViewModel
            if (!viewModel.properties.containsKey(propertyId)) {
                Log.e(LOG_TAG, "Property with propertyId = $propertyId does not exist")
                return
            }

            val property = viewModel.properties[propertyId]
            val player1 = binding.player1
            val player2 = binding.player2

            // Check if any of the Players contain the Property
            when {
                player1 != null && player1.properties.contains(property) -> {
                    // Check Property
                    if (!checkProperty(property!!, player1Properties))
                        return
                    player1Properties.add(property)
                    player1RecyclerViewAdapter.notifyItemInserted(player1Properties.size - 1)
                }
                player2 != null && player2.properties.contains(property) -> {
                    // Check Property
                    if (!checkProperty(property!!, player2Properties))
                        return
                    player2Properties.add(property)
                    player2RecyclerViewAdapter.notifyItemInserted(player2Properties.size - 1)
                }
                else -> {
                    Toast.makeText(
                        context,
                        "None of the Players own this Property",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            return
        }


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

    private fun checkProperty(property: Property, propertyList: List<Property>) : Boolean {
        // Check if Property has already been added
        if (propertyList.contains(property)) {
            Toast.makeText(context, "Property is already added", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        // Color Property
        if (property is ColorProperty) {
            val player = viewModel.playerMap[property.playerId]
            val sameColorProperties = player?.getSameColorProperties(property)
            for (sameColorProperty in sameColorProperties!!) {
                if (sameColorProperty.currentRentLevel.value!! > 0) {
                    Toast.makeText(context, "Cannot Trade Property with Houses", Toast.LENGTH_SHORT)
                        .show()
                    return false
                }
            }
        }
        // Station has Depot
        if (viewModel.mega && property is StationProperty && property.depot.value!!) {
            Toast.makeText(context, "Cannot Trade Station with a Depot", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        return true
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