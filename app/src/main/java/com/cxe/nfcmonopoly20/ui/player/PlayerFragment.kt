package com.cxe.nfcmonopoly20.ui.player

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.cxe.nfcmonopoly20.R
import com.cxe.nfcmonopoly20.databinding.FragmentPlayerBinding
import com.cxe.nfcmonopoly20.logic.AppViewModel
import com.cxe.nfcmonopoly20.logic.player.Player
import com.cxe.nfcmonopoly20.util.CardIdToColor

const val PLAYER_TAG = "player_tag"

private const val LOG_TAG = "PlayerFragment"

class PlayerFragment : Fragment() {

    // Binding
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val viewModel: AppViewModel by activityViewModels()

    private lateinit var player: Player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bind player
        binding.player = player
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Check arguments
        if (arguments == null) {
            Log.e(LOG_TAG, "PlayerFragment arguments are null")
        } else {
            // Get Player
            player = requireArguments()[PLAYER_TAG] as Player
        }

        // Get Color
        val color = CardIdToColor.convert(context, player.cardId)
        val colorDrawable = ColorDrawable(color)

        // Set ActionBar and StatusBar color the same as the Player's Debit card color
        context as AppCompatActivity
        context.supportActionBar?.setBackgroundDrawable(colorDrawable)
        context.window.statusBarColor = color
    }

    override fun onDetach() {
        super.onDetach()

        // Get Color
        val color = resources.getColor(R.color.game_button_bg_color_primary, null)
        val colorDrawable = ColorDrawable(color)

        // Set ActionBar and StatusBar color the same as the Player's Debit card color
        val contextTemp = context as AppCompatActivity
        contextTemp.supportActionBar?.setBackgroundDrawable(colorDrawable)
        contextTemp.window.statusBarColor = color
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}