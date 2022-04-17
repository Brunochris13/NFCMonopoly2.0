package com.cxe.nfcmonopoly20.ui.game

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cxe.nfcmonopoly20.R
import com.cxe.nfcmonopoly20.databinding.FragmentGameBinding
import com.cxe.nfcmonopoly20.logic.*
import com.cxe.nfcmonopoly20.logic.player.CardId
import com.cxe.nfcmonopoly20.logic.property.PropertyId
import com.cxe.nfcmonopoly20.ui.dialog.BankOrFreeParkingDialogFragment
import com.cxe.nfcmonopoly20.ui.dialog.NfcTapCardDialogFragment
import com.cxe.nfcmonopoly20.ui.dialog.PropertyDialogFragment

private const val LOG_TAG = "GameFragment"

class GameFragment : Fragment() {

    // Binding
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val viewModel: AppViewModel by activityViewModels()

    // RecyclerViewAdapter
    private lateinit var recyclerViewAdapter: GamePlayerListAdapter

    // Dialogs
    private lateinit var nfcTapCardDialog: NfcTapCardDialogFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Binding
        _binding = FragmentGameBinding.inflate(inflater, container, false)

        // On Back pressed
        setHasOptionsMenu(true)
        val onBackPressedCallback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    exitDialog()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recyclerview
        val recyclerView = binding.gamePlayerList
        this.recyclerViewAdapter = GamePlayerListAdapter(viewModel.playerList)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        // Pay Prison Button
        binding.payPrisonBtn.setOnClickListener {
            nfcTapCardDialog = NfcTapCardDialogFragment(
                resources.getString(R.string.pay_prison),
                PRISON_AMOUNT,
                true
            )
            nfcTapCardDialog.show(parentFragmentManager, NFC_TAP_DIALOG_TAG)
        }

        // Luxury Tax Button
        binding.luxuryTaxBtn.setOnClickListener {
            nfcTapCardDialog = NfcTapCardDialogFragment(
                resources.getString(R.string.luxury_tax),
                LUXURY_TAX_AMOUNT,
                true
            )
            nfcTapCardDialog.show(parentFragmentManager, NFC_TAP_DIALOG_TAG)
        }

        // Income Tax Button
        binding.incomeTaxBtn.setOnClickListener {
            nfcTapCardDialog = NfcTapCardDialogFragment(
                resources.getString(R.string.income_tax),
                INCOME_TAX_AMOUNT,
                true
            )
            nfcTapCardDialog.show(parentFragmentManager, NFC_TAP_DIALOG_TAG)
        }

        // Pay Button
        binding.payBtn.setOnClickListener {
            val bankOrFreeParkingDialog = BankOrFreeParkingDialogFragment(true)
            bankOrFreeParkingDialog.show(parentFragmentManager, BANK_OR_FREE_PARKING_DIALOG_TAG)
        }

        // Trade Button

        // Collect Button
        binding.collectBtn.setOnClickListener {
            val bankOrFreeParkingDialog = BankOrFreeParkingDialogFragment(false)
            bankOrFreeParkingDialog.show(parentFragmentManager, BANK_OR_FREE_PARKING_DIALOG_TAG)
        }

        // Go Button
        binding.goBtn.setOnClickListener {
            nfcTapCardDialog =
                NfcTapCardDialogFragment(resources.getString(R.string.go_btn), GO_AMOUNT, false)
            nfcTapCardDialog.show(parentFragmentManager, NFC_TAP_DIALOG_TAG)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                exitDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun exitDialog() {
        val builder = AlertDialog.Builder(requireActivity())
        with(builder) {
            setTitle(resources.getString(R.string.exit_dialog_title))
            setMessage(resources.getString(R.string.exit_dialog_message))
            setPositiveButton(resources.getString(R.string.exit_dialog_positive)) { _, _ ->
                viewModel.resetGame()
                findNavController().popBackStack()
            }
            setNegativeButton(
                resources.getString(R.string.exit_dialog_negative),
                null
            )
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun onNewIntent(msg: String) {

        val fragments = parentFragmentManager.fragments

        // Check if NfcTapCardDialog is active
        val nfcTapCardDialogFragment = fragments.firstOrNull {fragment -> fragment is NfcTapCardDialogFragment }
        if (nfcTapCardDialogFragment != null) {
            // Cast fragment
            nfcTapCardDialogFragment as NfcTapCardDialogFragment

            // Check if it is a Debit card
            if (viewModel.isCardId(msg)) {
                val cardId = CardId.valueOf(msg)

//                nfcTapCardDialog.onNewIntent(cardId)
                nfcTapCardDialogFragment.onNewIntent(cardId)

                // Update RecyclerView
                val player = viewModel.playerMap[cardId]
                if (player!!.moneyShown)
                    recyclerViewAdapter.notifyItemChanged(viewModel.playerList.indexOf(player))
            } else {
                Toast.makeText(context, "Wrong Card", Toast.LENGTH_SHORT).show()
                Log.i(LOG_TAG, "Wrong Card")
            }
            return
        }

        // Check if it is a Property Tag
        if (viewModel.isProperty(msg)) {
            // Get Property
            val propertyId = PropertyId.valueOf(msg)
            val property = viewModel.properties[propertyId]

            // Check if Property is null
            if (property != null) {
                // Create the Property Dialog
                val propertyDialog = PropertyDialogFragment(property)
                propertyDialog.show(parentFragmentManager, PROPERTY_DIALOG_TAG)
            } else {
                Log.e(LOG_TAG, "Property is null, propertyId = $propertyId")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
