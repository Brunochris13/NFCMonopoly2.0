package com.cxe.nfcmonopoly20.ui.game

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cxe.nfcmonopoly20.R
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
        // Binding
        _binding = FragmentGameBinding.inflate(inflater, container, false)

        // On Back pressed
        setHasOptionsMenu(true)
        val onBackPressedCallback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    exitDialog()
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                exitDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun exitDialog() {
        val builder = AlertDialog.Builder(requireActivity())
        with(builder) {
            setTitle(resources.getString(R.string.exit_dialog_title))
            setMessage(resources.getString(R.string.exit_dialog_message))
            setPositiveButton(resources.getString(R.string.exit_dialog_positive)) { _, _ ->
                viewModel.resetGame()
                findNavController().popBackStack()
            }
            setNegativeButton(
                resources.getString(R.string.exit_dialog_negative),
                null
            )
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
