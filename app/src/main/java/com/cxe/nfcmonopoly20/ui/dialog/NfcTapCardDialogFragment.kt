package com.cxe.nfcmonopoly20.ui.dialog

import android.annotation.SuppressLint
import android.os.Bundle
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
    private val pay: Boolean,
    private val freeParking: Boolean = false
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

    fun onNewIntent(cardId: CardId) {
        // Free Parking
        if (freeParking) {
            if (pay) viewModel.payFreeParking(cardId, amount) else viewModel.collectFreeParking(cardId)
        } else {
            // Player Pays or Collects (Bank)
            if (pay) viewModel.playerPay(cardId, amount) else viewModel.playerCollect(cardId, amount)
        }

        // Toast
        val player = viewModel.playerMap[cardId]
        val paysOrCollects = if (pay) "paid" else "collected"
        Toast.makeText(context, "${player?.name} $paysOrCollects €$amount", Toast.LENGTH_SHORT).show()

        dismiss()
    }
}