package com.cxe.nfcmonopoly20.logic.property

import android.util.Log
import com.cxe.nfcmonopoly20.logic.player.CardId

private const val LOG_TAG = "Property"
abstract class Property(
    val id: PropertyId,
    val name: String,
    val price: Int,
    val rent: Array<Int>,
    val mortgagedValue: Int
) {

    var mortgaged = false

    var playerId: CardId? = null

    private var _currentRentLevel = 0
    val currentRentLevel = _currentRentLevel

    fun increaseRentLevel() {
        if (currentRentLevel < rent.size)
            _currentRentLevel++
        else
            Log.e(LOG_TAG, "Tried to increase rent level, more than rent.size. currentRentLevel = $currentRentLevel, rent.size = ${rent.size}")
    }

    fun decreaseRentLevel() {
        if (currentRentLevel > 0)
            _currentRentLevel--
        else
            Log.e(LOG_TAG, "Tried to decrease rent level, less than 0. currentRentLevel = $currentRentLevel")
    }

    fun reset() {
        _currentRentLevel = 0
        mortgaged = false
        playerId = null
    }


}