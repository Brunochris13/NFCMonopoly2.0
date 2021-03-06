package com.cxe.nfcmonopoly20.ui.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.cxe.nfcmonopoly20.databinding.PlayerEditDialogBinding
import com.cxe.nfcmonopoly20.logic.AppViewModel
import com.cxe.nfcmonopoly20.logic.CARD_ID_TAG
import com.cxe.nfcmonopoly20.logic.PLAYER_NAME_TAG
import com.cxe.nfcmonopoly20.logic.player.CardId
import com.cxe.nfcmonopoly20.logic.player.Player

private const val LOG_TAG = "EditPlayerDialogFragment"
class EditPlayerDialogFragment : DialogFragment() {

    // Binding
    private var _binding: PlayerEditDialogBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val viewModel: AppViewModel by activityViewModels()

    // On Dismiss
    lateinit var onDismissListener: () -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        // Fix corners
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        _binding = PlayerEditDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var newPlayer = true

        // Get args from Bundle
        val cardId = arguments?.getSerializable(CARD_ID_TAG)
        val prevName = arguments?.getString(PLAYER_NAME_TAG)

        // Check if cardId is null
        if (cardId == null) {
            Log.e(LOG_TAG, "cardId = null")
            return
        }

        // Set binding's cardId
        binding.cardId = cardId as CardId?

            if (prevName != null) {
            // Show Previous Player Name
            binding.editTextPlayerName.editText?.setText(prevName)

            newPlayer = false
        }

        // Save button
        binding.saveNameButton.setOnClickListener {
            val playerName = binding.editTextPlayerName.editText?.text.toString()
            if (playerName == "") {
                Toast.makeText(context, "Please enter name", Toast.LENGTH_SHORT).show()
            } else {
                // New Player
                if (newPlayer) {
                    val player = Player(playerName, cardId)
                    viewModel.addPlayer(player)
                } else { // Existing Player
                    val player = viewModel.playerMap[cardId]
                    player?.name = playerName
                }
                dismiss()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        if (this::onDismissListener.isInitialized)
            onDismissListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}