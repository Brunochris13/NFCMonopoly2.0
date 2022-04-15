package com.cxe.nfcmonopoly20.ui.game.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.cxe.nfcmonopoly20.R
import com.cxe.nfcmonopoly20.databinding.FragmentGameBankOrFreeParkingBinding
import com.cxe.nfcmonopoly20.logic.AppViewModel
import com.cxe.nfcmonopoly20.logic.CUSTOM_AMOUNT_DIALOG_TAG

class BankOrFreeParkingDialogFragment(private val pay: Boolean) : DialogFragment() {

    // Binding
    private var _binding: FragmentGameBankOrFreeParkingBinding? = null
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
        _binding = FragmentGameBankOrFreeParkingBinding.inflate(inflater, container, false)

        // Fix corners
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Dialog Title
        binding.bankOrFreeParkingTextview.text =
            if (pay) resources.getString(R.string.pay) else resources.getString(R.string.collect)

        // Bank Button
        binding.bankButton.setOnClickListener {
            dismiss()
            val title = resources.getString(R.string.bank_icon)
            val customAmountDialog = CustomAmountDialogFragment(title, pay)
            customAmountDialog.show(parentFragmentManager, CUSTOM_AMOUNT_DIALOG_TAG)
        }

        // Free Parking Button
        binding.freeParkingButton.setOnClickListener {

        }
    }

    override fun onResume() {
        super.onResume()

        // Set Layout width
        val layoutParams = dialog?.window?.attributes
        layoutParams?.width = 800 //400dp
        dialog?.window?.attributes = layoutParams
    }
}