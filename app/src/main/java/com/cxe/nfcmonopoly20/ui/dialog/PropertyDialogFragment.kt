package com.cxe.nfcmonopoly20.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.cxe.nfcmonopoly20.R
import com.cxe.nfcmonopoly20.databinding.PropertyDialogBinding
import com.cxe.nfcmonopoly20.logic.AUCTION_DIALOG_TAG
import com.cxe.nfcmonopoly20.logic.AppViewModel
import com.cxe.nfcmonopoly20.logic.player.CardId
import com.cxe.nfcmonopoly20.logic.player.Player
import com.cxe.nfcmonopoly20.logic.property.*
import com.cxe.nfcmonopoly20.ui.game.PlayerPropertyListAdapter

private const val LOG_TAG = "PropertyDialogFragment"

class PropertyDialogFragment(
    private val property: Property,
    private val playerView: Boolean = false
) : DialogFragment() {

    // Binding
    private var _binding: PropertyDialogBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val viewModel: AppViewModel by activityViewModels()

    // PropertyList Adapter
    private lateinit var propertyListAdapter: PlayerPropertyListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        // Binding
        _binding = PropertyDialogBinding.inflate(inflater, container, false)

        // Fix corners
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bind Property and LifecycleOwner
        binding.property = property
        binding.lifecycleOwner = viewLifecycleOwner

        // Default Values
        binding.colorProperty = viewModel.properties[PropertyId.COLOR_BROWN_1] as ColorProperty
        binding.stationProperty = viewModel.stationProperties[0]
        binding.utilityProperty = viewModel.utilityProperties[0]

        // Auction Button
        if (property.playerId == null) {
            binding.auctionBtn.visibility = View.VISIBLE

            binding.auctionBtn.setOnClickListener {
                dismiss()

                // Go to the Auction Layout
                val auctionDialog = AuctionDialogFragment(property)
                auctionDialog.show(parentFragmentManager, AUCTION_DIALOG_TAG)
            }
        }

        when (property) {
            // Color Property
            is ColorProperty -> {
                // Set colorProperty of binding
                binding.colorProperty = property

                // Make the Color Property layout visible
                binding.colorPropertyLayout.root.visibility = View.VISIBLE
            }
            // Station Property
            is StationProperty -> {
                // Set stationProperty of binding
                binding.stationProperty = property

                // Make the Station Property layout visible
                binding.stationPropertyLayout.root.visibility = View.VISIBLE
            }
            // Utility Property
            is UtilityProperty -> {
                // Set utilityProperty of binding
                binding.utilityProperty = property

                // Make the Utility Property layout visible
                binding.utilityPropertyLayout.root.visibility = View.VISIBLE

                // Check if the Property is owned by a Player
                if (property.playerId != null && !playerView && !property.mortgaged.value!!) {
                    binding.utilityDiceMenuCard.visibility = View.VISIBLE

                    // Dice Menu
                    val diceItems = if (viewModel.mega) (2..15).toList() else (2..12).toList()
                    val adapter = ArrayAdapter(
                        requireContext(),
                        R.layout.fragment_game_dice_list_item,
                        diceItems
                    )
                    (binding.utilityDiceMenu.editText as? AutoCompleteTextView)?.setAdapter(adapter)
                }
            }
        }

        // If Player View
        if (playerView)
            setButtons()
    }

    fun onNewIntent(cardId: CardId): List<Player> {
        val players = mutableListOf<Player>()

        // Check if Property belongs to a Player
        if (property.playerId == null) {
            // Player buys Property
            viewModel.playerBuyProperty(cardId, property)

            // Add affected Players
            players.add(viewModel.playerMap[cardId]!!)

            // Toast
            val player = viewModel.playerMap[cardId]
            Toast.makeText(
                context,
                "${player?.name} bought ${property.name} for €${property.price}",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            // Player pays Rent to another Player

            // Check if this player owns the Property
            if (cardId == property.playerId) {
                Toast.makeText(context, "You own it!", Toast.LENGTH_SHORT).show()
                Log.i(LOG_TAG, "Player tried to pay rent to themselves")
                return emptyList()
            }

            // Check if Property is Mortgaged
            if (property.mortgaged.value!!) {
                Toast.makeText(context, "Property is Mortgaged!", Toast.LENGTH_SHORT).show()
                dismiss()
                return emptyList()
            }

            // Check if Property is a Utility Property
            val rent = if (property is UtilityProperty) {
                val text = binding.utilityDiceMenu.editText?.text.toString()
                // Check if Nothing is Selected
                if (text == "") {
                    Toast.makeText(context, "Please select a value", Toast.LENGTH_SHORT).show()
                    return emptyList()
                }
                val diceValue = Integer.parseInt(text)
                viewModel.playerPaysRent(cardId, property, diceValue = diceValue)
            } else {
                viewModel.playerPaysRent(cardId, property)
            }

            // Add affected Players
            val player = viewModel.playerMap[cardId]
            val otherPlayer = viewModel.playerMap[property.playerId]
            players.add(player!!)
            players.add(otherPlayer!!)

            // Toast
            Toast.makeText(
                context,
                "${player.name} paid rent of €${rent} to ${otherPlayer.name}",
                Toast.LENGTH_SHORT
            ).show()
        }

        dismiss()

        return players
    }

    private fun setButtons() {
        // Hide all Buttons
        hideAllButtons()

        // Color Property
        if (property is ColorProperty) {
            // Check if Color Property is Set
            if (property.set) {
                val player = viewModel.playerMap[property.playerId]
                val sameColorProperties = player?.getSameColorProperties(property)
                // Buy?
                if (canBuy(sameColorProperties!!))
                    setBuyHouseButton()
                // Sell?
                if (canSell(sameColorProperties))
                    setSellHouseButton()
                // Mortgage?
                if (canMortgage(sameColorProperties))
                    setMortgageButton()
            } else {
                // Mortgage Button
                setMortgageButton()
            }
        } else if (viewModel.mega && property is StationProperty) {
            // Mega Station Properties

            // Mortgage?
            if (property.mortgaged.value!!) {
                setMortgageButton()
            } else {
                // Depot?
                if (property.depot.value!!) {
                    setSellDepotButton()
                } else {
                    setBuyDepotButton()
                    setMortgageButton()
                }
            }
        } else {
            // Station and Utility Properties
            // Mortgage Button
            setMortgageButton()
        }
    }

    private fun setMortgageButton() {
        // Make the Mortgage Button Visible
        binding.mortgageButtonLayout.visibility = View.VISIBLE

        binding.mortgageButton.setOnClickListener {
            viewModel.mortgageProperty(property)

            // Update Player Property RecyclerView
            if (this::propertyListAdapter.isInitialized) {
                // Color Property
                if (property is ColorProperty) {
                    // Update all the Same Color Properties
                    val player = viewModel.playerMap[property.playerId]
                    val sameColorProperties = player?.getSameColorProperties(property)
                    for (sameColorProperty in sameColorProperties!!) {
                        propertyListAdapter.updateItem(sameColorProperty)
                    }
                } else
                    propertyListAdapter.updateItem(property)
            }

            // Update Buttons
            setButtons()
        }
    }

    private fun setBuyDepotButton() {
        // Make the Buy House Button Visible
        binding.buyHouseButtonLayout.visibility = View.VISIBLE

        binding.buyHouseButton.setOnClickListener {
            property as StationProperty
            val player = viewModel.playerMap[property.playerId]
            property.buyDepot(player!!)

            // Update Player Property RecyclerView
            if (this::propertyListAdapter.isInitialized)
                propertyListAdapter.updateItem(property)

            // Update Buttons
            setButtons()
        }
    }

    private fun setSellDepotButton() {
        // Make the Sell House Button Visible
        binding.sellHouseButtonLayout.visibility = View.VISIBLE

        binding.sellHouseButton.setOnClickListener {
            property as StationProperty
            val player = viewModel.playerMap[property.playerId]
            property.sellDepot(player!!)

            // Update Player Property RecyclerView
            if (this::propertyListAdapter.isInitialized)
                propertyListAdapter.updateItem(property)

            // Update Buttons
            setButtons()
        }
    }

    private fun setBuyHouseButton() {
        // Make the Buy House Button Visible
        binding.buyHouseButtonLayout.visibility = View.VISIBLE

        binding.buyHouseButton.setOnClickListener {
            property as ColorProperty
            val player = viewModel.playerMap[property.playerId]
            property.buyHouse(player!!)
            // Update Buttons
            setButtons()
        }
    }

    private fun setSellHouseButton() {
        // Make the Sell House Button Visible
        binding.sellHouseButtonLayout.visibility = View.VISIBLE

        binding.sellHouseButton.setOnClickListener {
            property as ColorProperty
            val player = viewModel.playerMap[property.playerId]
            property.sellHouse(player!!)
            // Update Buttons
            setButtons()
        }
    }

    private fun canBuy(sameColorProperties: List<ColorProperty>): Boolean {
        // Cast property to ColorProperty
        property as ColorProperty
        // First check if property canBuy a house
        return if (property.canBuy()) {

            // Check if any of the other properties have a lower rentLevel than this property
            for (sameColorProperty in sameColorProperties) {

                // Mega Edition
                if (viewModel.mega) {
                    // If any of the other Properties are Mortgaged or a Property has a Skyscraper
                    if (sameColorProperty.mortgaged.value!! ||
                        sameColorProperty.currentRentLevel.value!! == property.rent.size - 1
                    )
                        return false
                }

                if (sameColorProperty.currentRentLevel.value!! < property.currentRentLevel.value!!)
                    return false
            }
            true
        } else {
            false
        }
    }

    private fun canSell(sameColorProperties: List<ColorProperty>): Boolean {
        // Cast property to ColorProperty
        property as ColorProperty
        // First check if property canSell a house
        return if (property.canSell()) {
            // Check if any of the other properties have a bigger rentLevel than this property
            for (sameColorProperty in sameColorProperties) {
                if (sameColorProperty.currentRentLevel.value!! > property.currentRentLevel.value!!)
                    return false
            }
            true
        } else {
            false
        }
    }

    private fun canMortgage(sameColorProperties: List<ColorProperty>): Boolean {
        // First check if the currentRentLevel is equal to 0
        return if (property.currentRentLevel.value == 0) {
            // Check if any of the other properties have a house
            for (sameColorProperty in sameColorProperties) {
                if (sameColorProperty.currentRentLevel.value!! > property.currentRentLevel.value!!)
                    return false
            }
            true
        } else {
            false
        }
    }

    private fun hideAllButtons() {
        binding.buyHouseButtonLayout.visibility = View.GONE
        binding.mortgageButtonLayout.visibility = View.GONE
        binding.sellHouseButtonLayout.visibility = View.GONE
    }

    fun setRecyclerViewAdapter(recyclerViewAdapter: PlayerPropertyListAdapter) {
        this.propertyListAdapter = recyclerViewAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}