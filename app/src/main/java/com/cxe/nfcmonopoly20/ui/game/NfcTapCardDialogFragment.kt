package com.cxe.nfcmonopoly20.ui.game

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.cxe.nfcmonopoly20.R
import com.cxe.nfcmonopoly20.databinding.FragmentGameNfcCardTapBinding
import com.cxe.nfcmonopoly20.logic.AppViewModel
import com.cxe.nfcmonopoly20.logic.player.CardId


private const val LOG_TAG = "NfcTapCardDialogFragment"

class NfcTapCardDialogFragment(
    private val message: String,
    private val amount: Int,
    private val pay: Boolean
) :
    DialogFragment() {

    // Binding
    private var _binding: FragmentGameNfcCardTapBinding? = null
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
        _binding = FragmentGameNfcCardTapBinding.inflate(inflater, container, false)

        // Fix corners
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nfcCardTapTitle.text = message

        with(binding.nfcCardTapAmount) {
            text = "€$amount"
            if (pay)
                setTextColor(resources.getColor(R.color.pay_text_color, null))
            else
                setTextColor(resources.getColor(R.color.receive_text_color, null))
        }
    }

    override fun onResume() {
        super.onResume()

        // Set Layout width
        val layoutParams = dialog?.window?.attributes
        layoutParams?.width = 800 //400dp
        dialog?.window?.attributes = layoutParams
    }

    fun onNewIntent(msg: String) {
        // Check if it is a Debit card
        if (viewModel.isCardId(msg)) {
            val cardId = CardId.valueOf(msg)

            // Check if player exists with this cardId
            if (!viewModel.playerMap.containsKey(cardId)) {
                Log.e(LOG_TAG, "Player does not exist with this cardId")
                return
            }

            // Player Pays or Collects
            if (pay) viewModel.playerPay(cardId, amount) else viewModel.playerCollect(cardId, amount)

            val player = viewModel.playerMap[cardId]
            Toast.makeText(context, "${player?.name}: ${player?.money}", Toast.LENGTH_SHORT).show()

            dismiss()

        } else {
            Toast.makeText(context, "Wrong Card", Toast.LENGTH_SHORT).show()
            Log.i(LOG_TAG, "Wrong Card")
        }
    }
}