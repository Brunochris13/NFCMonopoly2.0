package com.cxe.nfcmonopoly20.logic.property

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cxe.nfcmonopoly20.logic.player.Player

// Mega Edition Only
private const val DEPOT_PRICE = 100
private const val LOG_TAG = "StationProperty"

class StationProperty(
    id: PropertyId,
    name: String,
    price: Int,
    rent: IntArray,
    mortgagedValue: Int
) : Property(id, name, price, rent, mortgagedValue) {

    // Mega Edition Only
    private val _depot = MutableLiveData(false)
    val depot: LiveData<Boolean>
        get() = _depot

    val depotBuyPrice = DEPOT_PRICE
    val depotSellPrice = DEPOT_PRICE / 2

    override fun getRent(level: Int): Int {
        val rent = super.getRent(level)
        // If Station Property has a depot, then the rent is Double
        return if (depot.value!!) rent * 2 else rent
    }

    fun buyDepot(player: Player) {
        // Check if Property is Mortgaged
        if (mortgaged.value!!) {
            Log.e(
                LOG_TAG,
                "Tried to build a Depot on a Mortgaged Property, stationProperty = $name"
            )
            return
        }

        // Check if we already have a Depot
        if (depot.value!!) {
            Log.e(
                LOG_TAG,
                "Tried to build a Depot, when there is already one on there, stationProperty = $name"
            )
        }

        // Check if this player owns the property
        if (player.cardId != playerId) {
            Log.e(
                LOG_TAG,
                "Player does not own this station. station = $name, player = ${player.name}"
            )
            return
        }

        player.pay(depotBuyPrice)
        _depot.value = true
    }

    fun sellDepot(player: Player) {
        // Check if Property is Mortgaged
        if (mortgaged.value!!) {
            Log.e(
                LOG_TAG,
                "Tried to sell a Depot on a Mortgaged Property, stationProperty = $name"
            )
            return
        }

        // Check if the Station does not have a Depot
        if (depot.value!!) {
            Log.e(
                LOG_TAG,
                "Tried to sell a Depot, when there is not a Depot there, stationProperty = $name"
            )
        }

        // Check if this player owns the property
        if (player.cardId != playerId) {
            Log.e(
                LOG_TAG,
                "Player does not own this station. station = $name, player = ${player.name}"
            )
            return
        }

        player.collect(depotSellPrice)
        _depot.value = false
    }

    override fun reset() {
        super.reset()
        _depot.value = false
    }
}