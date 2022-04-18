package com.cxe.nfcmonopoly20.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.cxe.nfcmonopoly20.databinding.FragmentGameAuctionLayoutBinding
import com.cxe.nfcmonopoly20.logic.AppViewModel
import com.cxe.nfcmonopoly20.logic.player.CardId
import com.cxe.nfcmonopoly20.logic.property.Property

private const val LOG_TAG = "AuctionDialogFragment"

class AuctionDialogFragment(private val property: Property) : DialogFragment() {

    // Binding
    private var _binding: FragmentGameAuctionLayoutBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val viewModel: AppViewModel by activityViewModels()

    private var bid = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        // Binding
        _binding = FragmentGameAuctionLayoutBinding.inflate(inflater, container, false)

        // Fix corners
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bidButton.setOnClickListener {
            bid += 10

            binding.bidButton.text = bid.toString()
        }
    }

    fun onNewIntent(cardId: CardId) {

        viewModel.playerBuyProperty(cardId, property, amount = bid)

        dismiss()
    }
}