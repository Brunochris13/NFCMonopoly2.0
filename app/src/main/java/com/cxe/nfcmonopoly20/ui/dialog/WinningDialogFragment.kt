package com.cxe.nfcmonopoly20.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.cxe.nfcmonopoly20.databinding.WinningDialogBinding
import com.cxe.nfcmonopoly20.logic.AppViewModel

private const val LOG_TAG = "WinningDialogFragment"

class WinningDialogFragment(private val onExit: () -> Unit) : DialogFragment() {

    // Binding
    private var _binding: WinningDialogBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val viewModel: AppViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        // Fix corners
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        _binding = WinningDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check if there are more than 1 Players left
        if (viewModel.playerList.size > 1) {
            Log.e(LOG_TAG, "More than 1 Players left")
            return
        }

        // Get Winning Player
        val player = viewModel.playerList[0]

        // Bind the Player
        binding.player = player

        // Close Button
        binding.closeButton.setOnClickListener {
            dismiss()
            onExit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}