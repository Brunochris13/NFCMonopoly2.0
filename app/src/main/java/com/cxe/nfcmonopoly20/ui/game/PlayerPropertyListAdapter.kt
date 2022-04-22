package com.cxe.nfcmonopoly20.ui.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.cxe.nfcmonopoly20.databinding.ColorPropertySmallBinding
import com.cxe.nfcmonopoly20.databinding.StationPropertySmallBinding
import com.cxe.nfcmonopoly20.databinding.UtilityPropertySmallBinding
import com.cxe.nfcmonopoly20.logic.property.ColorProperty
import com.cxe.nfcmonopoly20.logic.property.Property
import com.cxe.nfcmonopoly20.logic.property.StationProperty
import com.cxe.nfcmonopoly20.logic.property.UtilityProperty

private const val COLOR_PROPERTY_VIEW_TYPE = 0
private const val STATION_PROPERTY_VIEW_TYPE = 1
private const val UTILITY_PROPERTY_VIEW_TYPE = 2

class PlayerPropertyListAdapter(
    private var playerPropertyList: List<Property>,
    private val onClickListener: (Property) -> Unit
) : RecyclerView.Adapter<PlayerPropertyListAdapter.PlayerPropertyListViewHolder>() {

    inner class PlayerPropertyListViewHolder(
        private val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(colorProperty: ColorProperty) {
            binding as ColorPropertySmallBinding
            binding.colorProperty = colorProperty
        }

        fun bind(stationProperty: StationProperty) {
            binding as StationPropertySmallBinding
            binding.stationProperty = stationProperty
        }

        fun bind(utilityProperty: UtilityProperty) {
            binding as UtilityPropertySmallBinding
            binding.utilityProperty = utilityProperty
        }
    }

    private lateinit var binding: ViewDataBinding

    override fun getItemViewType(position: Int): Int {
        return when (playerPropertyList[position]) {
            is ColorProperty -> COLOR_PROPERTY_VIEW_TYPE
            is StationProperty -> STATION_PROPERTY_VIEW_TYPE
            is UtilityProperty -> UTILITY_PROPERTY_VIEW_TYPE
            else -> -1
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlayerPropertyListViewHolder {
        binding = when (viewType) {
            COLOR_PROPERTY_VIEW_TYPE -> ColorPropertySmallBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            STATION_PROPERTY_VIEW_TYPE -> StationPropertySmallBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            UTILITY_PROPERTY_VIEW_TYPE -> UtilityPropertySmallBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            else -> ColorPropertySmallBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        }
        return PlayerPropertyListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayerPropertyListViewHolder, position: Int) {
        val property = playerPropertyList[position]

        // Set on Property Click Listener
        holder.itemView.setOnClickListener {
            onClickListener(property)
        }

        // Bind Property
        when (property) {
            is ColorProperty -> holder.bind(property)
            is StationProperty -> holder.bind(property)
            is UtilityProperty -> holder.bind(property)
        }
    }

    fun updateItem(property: Property) {
        val position = playerPropertyList.indexOf(property)
        notifyItemChanged(position)
    }

    fun updateList(newProperties: List<Property>) {
        val size = playerPropertyList.size
        notifyItemRangeRemoved(0, size)
        playerPropertyList = newProperties
        notifyItemRangeInserted(0, playerPropertyList.size)
    }

    override fun getItemCount(): Int = playerPropertyList.size
}