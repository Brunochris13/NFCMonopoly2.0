package com.cxe.nfcmonopoly20.logic.property

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cxe.nfcmonopoly20.logic.player.CardId
import java.io.Serializable

private const val LOG_TAG = "Property"

abstract class Property(
    val id: PropertyId,
    val name: String,
    val price: Int,
    val rent: IntArray,
    val mortgagedValue: Int
) : Serializable {

    private val _mortgaged = MutableLiveData(false)
    val mortgaged: LiveData<Boolean>
        get() = _mortgaged

    var playerId: CardId? = null

    private var _currentRentLevel = MutableLiveData(0)
    val currentRentLevel: LiveData<Int>
        get() = _currentRentLevel

    // Mega Edition Only (It should be set at the start)
    var mega = false

    open fun getRent(level: Int): Int {
        // Check if level index is out of bounds
        return if ((level < 0) || (level >= rent.size)) {
            Log.e(LOG_TAG, "level is out of bounds, level = $level and rent.size = ${rent.size}")
            0
        } else
            rent[level]
    }

    fun getCurrentRent() = currentRentLevel.value?.let { getRent(it) }

    fun increaseRentLevel(): Boolean {
        return if (currentRentLevel.value!! < rent.size) {
            _currentRentLevel.value = _currentRentLevel.value?.plus(1)
            true
        } else {
            Log.e(
                LOG_TAG,
                "Tried to increase rent level, more than rent.size. currentRentLevel = ${currentRentLevel.value}, rent.size = ${rent.size}"
            )
            false
        }
    }

    fun decreaseRentLevel(): Boolean {
        return if (currentRentLevel.value!! > 0) {
            _currentRentLevel.value = _currentRentLevel.value?.minus(1)
            true
        } else {
            Log.e(
                LOG_TAG,
                "Tried to decrease rent level, less than 0. currentRentLevel = ${currentRentLevel.value}"
            )
            false
        }
    }

    fun setRentLevel(level: Int) {
        _currentRentLevel.value = level
    }

    open fun setMortgageStatus(status: Boolean) {
        _mortgaged.value = status
    }

    open fun reset() {
        _currentRentLevel.value = 0
        _mortgaged.value = false
        playerId = null
    }

}