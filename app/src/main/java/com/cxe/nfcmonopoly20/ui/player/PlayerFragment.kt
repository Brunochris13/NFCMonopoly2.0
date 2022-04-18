package com.cxe.nfcmonopoly20.ui.player

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.cxe.nfcmonopoly20.databinding.FragmentPlayerBinding
import com.cxe.nfcmonopoly20.logic.AppViewModel
import com.cxe.nfcmonopoly20.logic.player.Player

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
        // Check arguments
        if (arguments == null) {
            Log.e(LOG_TAG, "PlayerFragment arguments are null")
        } else {
            player = requireArguments()[PLAYER_TAG] as Player
        }
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

}