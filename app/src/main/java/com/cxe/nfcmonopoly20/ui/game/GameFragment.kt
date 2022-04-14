package com.cxe.nfcmonopoly20.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cxe.nfcmonopoly20.databinding.FragmentGameBinding
import com.cxe.nfcmonopoly20.logic.AppViewModel

class GameFragment : Fragment() {

    // Binding
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val viewModel: AppViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recyclerview
        val recyclerView = binding.gamePlayerList
        val recyclerViewAdapter = GamePlayerListAdapter(viewModel.playerList)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(view.context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}