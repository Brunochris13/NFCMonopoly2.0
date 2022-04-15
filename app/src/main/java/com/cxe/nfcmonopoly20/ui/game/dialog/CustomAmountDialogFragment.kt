package com.cxe.nfcmonopoly20.ui.game.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.cxe.nfcmonopoly20.R
import com.cxe.nfcmonopoly20.databinding.FragmentGameCustomAmountBinding
import com.cxe.nfcmonopoly20.logic.AppViewModel
import com.cxe.nfcmonopoly20.logic.NFC_TAP_DIALOG_TAG

class CustomAmountDialogFragment(
    private val title: String,
    private val pay: Boolean,
    private val freeParking: Boolean = false
) :
    DialogFragment() {

    // Binding
    private var _binding: FragmentGameCustomAmountBinding? = null
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
        _binding = FragmentGameCustomAmountBinding.inflate(inflater, container, false)

        // Fix corners
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set Title Text
        binding.customAmountTitle.text = title

        // Set Button Text
        binding.customAmountBtn.text =
            if (pay) resources.getString(R.string.pay) else resources.getString(R.string.collect)

        // Button
        binding.customAmountBtn.setOnClickListener {
            dismiss()
            val amount: Int = binding.amountEditText.text.toString().toInt()
            val nfcTapCardDialog = NfcTapCardDialogFragment(
                title,
                amount,
                pay,
                freeParking
            )
            nfcTapCardDialog.show(parentFragmentManager, NFC_TAP_DIALOG_TAG)
        }
    }

    override fun onResume() {
        super.onResume()

        // Set Layout width
        val layoutParams = dialog?.window?.attributes
        layoutParams?.width = 800 //500dp
        dialog?.window?.attributes = layoutParams
    }
}