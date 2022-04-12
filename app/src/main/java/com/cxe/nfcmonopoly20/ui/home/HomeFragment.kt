package com.cxe.nfcmonopoly20.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cxe.nfcmonopoly20.databinding.FragmentHomeBinding
import com.cxe.nfcmonopoly20.logic.ViewModel

class HomeFragment : Fragment() {

    // Binding
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val viewModel: ViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ViewModel Creation
        binding.viewModel = viewModel

        // RecyclerView
        val recyclerView = binding.recyclerviewHome
        val recyclerViewAdapter = PlayerListAdapter(viewModel.playerList)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(view.context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}