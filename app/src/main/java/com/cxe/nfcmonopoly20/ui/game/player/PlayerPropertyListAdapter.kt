package com.cxe.nfcmonopoly20.ui.game.player

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cxe.nfcmonopoly20.databinding.FragmentPlayerPropertyListItemBinding
import com.cxe.nfcmonopoly20.logic.property.ColorProperty
import com.cxe.nfcmonopoly20.logic.property.Property
import com.cxe.nfcmonopoly20.logic.property.StationProperty
import com.cxe.nfcmonopoly20.logic.property.UtilityProperty

class PlayerPropertyListAdapter(
    private val playerPropertyList: List<Property>,
    private val defaultColorProperty: ColorProperty,
    private val defaultStationProperty: StationProperty,
    private val defaultUtilityProperty: UtilityProperty,
    private val onClickListener: (Property) -> Unit
) : RecyclerView.Adapter<PlayerPropertyListAdapter.PlayerPropertyListViewHolder>() {

    inner class PlayerPropertyListViewHolder(
        private val binding: FragmentPlayerPropertyListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            colorProperty: ColorProperty = defaultColorProperty,
            stationProperty: StationProperty = defaultStationProperty,
            utilityProperty: UtilityProperty = defaultUtilityProperty
        ) {
            binding.colorProperty = colorProperty
            binding.stationProperty = stationProperty
            binding.utilityProperty = utilityProperty
        }
    }

    private lateinit var binding: FragmentPlayerPropertyListItemBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlayerPropertyListViewHolder {
        binding = FragmentPlayerPropertyListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayerPropertyListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayerPropertyListViewHolder, position: Int) {
        val property = playerPropertyList[position]

        binding.root.setOnClickListener {
            onClickListener(property)
        }

        // Bind Property
        when (property) {
            is ColorProperty -> {
                // Make the Color Property Visible
                binding.colorPropertyLayout.root.visibility = View.VISIBLE

                holder.bind(colorProperty = property)
            }
            is StationProperty -> {
                // Make the Station Property Visible
                binding.stationPropertyLayout.root.visibility = View.VISIBLE

                holder.bind(stationProperty = property)
            }
            is UtilityProperty -> {
                // Make the Utility Property Visible
                binding.utilityPropertyLayout.root.visibility = View.VISIBLE

                holder.bind(utilityProperty = property)
            }
        }
    }

    override fun getItemCount(): Int = playerPropertyList.size
}