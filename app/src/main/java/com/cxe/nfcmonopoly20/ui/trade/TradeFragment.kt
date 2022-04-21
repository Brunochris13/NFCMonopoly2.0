package com.cxe.nfcmonopoly20.ui.trade

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.cxe.nfcmonopoly20.databinding.FragmentTradeBinding
import com.cxe.nfcmonopoly20.logic.AppViewModel


class TradeFragment : Fragment() {

    // Binding
    private var _binding: FragmentTradeBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val viewModel: AppViewModel by activityViewModels()

    // Up Down Button Status
    private var player1Pays = MutableLiveData(true)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Binding
        _binding = FragmentTradeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bind lifecycle Owner
        binding.lifecycleOwner = viewLifecycleOwner

        // Bind player1Pays variable
        binding.player1Pays = player1Pays

        // Up and Down Button
        binding.upDownBtn.setOnClickListener {
            player1Pays.value = !player1Pays.value!!
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}