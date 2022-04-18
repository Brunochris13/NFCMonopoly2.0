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
import com.cxe.nfcmonopoly20.getJsonDataFromAsset
import com.cxe.nfcmonopoly20.logic.*
import com.cxe.nfcmonopoly20.logic.player.CardId
import com.cxe.nfcmonopoly20.logic.player.Player
import com.cxe.nfcmonopoly20.logic.property.ColorProperty
import com.cxe.nfcmonopoly20.logic.property.PropertyId
import com.cxe.nfcmonopoly20.logic.property.StationProperty
import com.cxe.nfcmonopoly20.logic.property.UtilityProperty
import com.cxe.nfcmonopoly20.ui.dialog.EditPlayerDialogFragment
import org.json.JSONObject

// Constants
private const val LOG_TAG = "HomeFragment"
private const val EDIT_PLAYER_DIALOG_TAG = "edit_player_dialog_tag"

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

            // Check if we have 2 or more players
            if (viewModel.playerList.size < 2) {
                Toast.makeText(
                    context,
                    resources.getString(R.string.not_enough_players),
                    Toast.LENGTH_SHORT
                ).show()
                Log.i(LOG_TAG, resources.getString(R.string.not_enough_players))
            } else {
                // Set Mega Status
                viewModel.mega = binding.megaSwitch.isChecked

                // Set the starting money for all Players
                val startingMoney = if (viewModel.mega) STARTING_MONEY_MEGA else STARTING_MONEY
                for (player in viewModel.playerList) {
                    player.money = startingMoney
                }

                // Read Properties from JSON
                createProperties()

                // Navigate to GameFragment
                findNavController().navigate(R.id.action_HomeFragment_to_gameFragment)
            }
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

        dialog.show(parentFragmentManager, EDIT_PLAYER_DIALOG_TAG)
    }

    private fun createProperties() {
        // Read JSON file from Assets
        val jsonFile = if (viewModel.mega) "mega_properties.json" else "properties.json"
        val jsonFileString = getJsonDataFromAsset(requireContext(), jsonFile)

        // Check if file was read from Assets
        if (jsonFileString != null) {
            val jsonArrayObject = JSONObject(jsonFileString)
            val jsonArray = jsonArrayObject.getJSONArray("Properties")

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)

                // General Property Attributes
                val propertyId = PropertyId.valueOf(jsonObject.getString("PropertyID"))
                val name = jsonObject.getString("Name")
                val price = jsonObject.getInt("Price")
                val rentJsonArray = jsonObject.getJSONArray("Rent")
                val rent = IntArray(rentJsonArray.length())
                for (j in 0 until rentJsonArray.length()) {
                    rent[j] = rentJsonArray.getInt(j)
                }
                val mortgagedValue = jsonObject.getInt("MortgagedValue")

                val property = when (jsonObject.getString("PropertyType")) {
                    "ColorProperty" -> {
                        val color =
                            ColorProperty.PropertyColors.valueOf(jsonObject.getString("Color"))
                        val housePrice = jsonObject.getInt("HousePurchasePrice")
                        val colorSetPropertyAmount = jsonObject.getInt("ColorSetPropertyAmount")
                        // Create ColorProperty
                        ColorProperty(
                            propertyId,
                            name,
                            price,
                            rent,
                            mortgagedValue,
                            color,
                            housePrice,
                            colorSetPropertyAmount
                        )
                    }
                    "StationProperty" -> {
                        // Create StationProperty
                        StationProperty(
                            propertyId,
                            name,
                            price,
                            rent,
                            mortgagedValue
                        )
                    }
                    "UtilityProperty" -> {
                        val utilityType = UtilityProperty.UtilityType.valueOf(jsonObject.getString("UtilityType"))
                        // Create Utility Property
                        UtilityProperty(
                            propertyId,
                            name,
                            price,
                            rent,
                            mortgagedValue,
                            utilityType
                        )
                    }
                    else -> null
                }

                // Check if property is not null
                if (property != null) {
                    viewModel.addProperty(property)
                } else {
                    Log.e(LOG_TAG, "$name property returned null")
                }
            }

        } else {
            Log.e(LOG_TAG, "Could not read $jsonFile from assets")
        }

        Toast.makeText(context, "Properties added", Toast.LENGTH_SHORT).show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}