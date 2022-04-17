package com.cxe.nfcmonopoly20.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.cxe.nfcmonopoly20.databinding.FragmentGamePropertyDialogBinding
import com.cxe.nfcmonopoly20.logic.AppViewModel
import com.cxe.nfcmonopoly20.logic.player.CardId
import com.cxe.nfcmonopoly20.logic.player.Player
import com.cxe.nfcmonopoly20.logic.property.*

private const val LOG_TAG = "PropertyDialogFragment"

class PropertyDialogFragment(private val property: Property) : DialogFragment() {

    // Binding
    private var _binding: FragmentGamePropertyDialogBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val viewModel: AppViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        // Binding
        //_binding = FragmentGamePropertyDialogBinding.inflate(inflater, container, false)
        _binding = FragmentGamePropertyDialogBinding.inflate(layoutInflater)

        // Fix corners
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.colorProperty = viewModel.properties[PropertyId.COLOR_BROWN_1] as ColorProperty
        binding.stationProperty = viewModel.stationProperties[0]
        binding.utilityProperty = viewModel.utilityProperties[0]

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
                // Set stationProperty of binding
                binding.utilityProperty = property

                // Make the Station Property layout visible
                binding.utilityPropertyLayout.root.visibility = View.VISIBLE
            }
        }
    }

    fun onNewIntent(cardId: CardId): List<Player> {
        val players = mutableListOf<Player>()

        // Check if Property belongs to a Player
        if (property.playerId == null) {
            // Player buys Property
            viewModel.playerBuyProperty(cardId, property)

            // Add affected Players
            players.add(viewModel.playerMap[cardId]!!)
        } else {
            // Player pays Rent to another Player

            // Check if this player owns the Property
            if (cardId == property.playerId) {
                Toast.makeText(context, "You own it!", Toast.LENGTH_SHORT).show()
                Log.i(LOG_TAG, "Player tried to pay rent to themselves")
                return listOf()
            }

            viewModel.playerPaysRent(cardId, property)

            // Add affected Players
            players.add(viewModel.playerMap[cardId]!!)
            players.add(viewModel.playerMap[property.playerId]!!)
        }

        dismiss()

        return players
    }
}