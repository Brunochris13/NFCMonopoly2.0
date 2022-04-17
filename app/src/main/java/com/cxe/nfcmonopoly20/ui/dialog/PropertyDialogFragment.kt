package com.cxe.nfcmonopoly20.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.cxe.nfcmonopoly20.databinding.FragmentGamePropertyDialogBinding
import com.cxe.nfcmonopoly20.logic.AppViewModel
import com.cxe.nfcmonopoly20.logic.property.*

class PropertyDialogFragment(private val property: Property) : DialogFragment() {

    // Binding
    private var _binding: FragmentGamePropertyDialogBinding? = null
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
        //_binding = FragmentGamePropertyDialogBinding.inflate(inflater, container, false)
        _binding = FragmentGamePropertyDialogBinding.inflate(layoutInflater)

        // Fix corners
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.colorProperty = viewModel.properties[PropertyId.COLOR_BROWN_1] as ColorProperty
        binding.stationProperty = viewModel.stationProperties[0]
        binding.utilityProperty = viewModel.utilityProperties[0]

        when (property) {
            // Color Property
            is ColorProperty -> {
                // Set colorProperty of binding
                binding.colorProperty = property

                // Make the Color Property layout visible
                binding.colorPropertyLayout.root.visibility = View.VISIBLE
            }
            // Station Property
            is StationProperty -> {
                // Set stationProperty of binding
                binding.stationProperty = property

                // Make the Station Property layout visible
                binding.stationPropertyLayout.root.visibility = View.VISIBLE
            }
            // Utility Property
            is UtilityProperty -> {
                // Set stationProperty of binding
                binding.utilityProperty = property

                // Make the Station Property layout visible
                binding.utilityPropertyLayout.root.visibility = View.VISIBLE
            }
        }

    }
}