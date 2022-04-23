package com.cxe.nfcmonopoly20.logic.property

import android.util.Log
import com.cxe.nfcmonopoly20.logic.player.Player

private const val LOG_TAG = "ColorProperty"

class ColorProperty(
    id: PropertyId,
    name: String,
    price: Int,
    rent: IntArray,
    mortgagedValue: Int,
    val color: PropertyColors,
    val housePrice: Int,
    val colorSetPropertyAmount: Int
) : Property(id, name, price, rent, mortgagedValue) {

    val houseSellPrice = housePrice / 2

    var set = false

    // Mega Edition Only
    var megaSet = false

    enum class PropertyColors {
        BROWN,
        LIGHT_BLUE,
        PINK,
        ORANGE,
        RED,
        YELLOW,
        GREEN,
        BLUE
    }

    override fun getRent(level: Int): Int {
        // If Property is a Set
        return if (level == 0 && megaSet) {
            super.getRent(level) * 3
        } else if (level == 0 && set) {
            super.getRent(level) * 2
        } else
            super.getRent(level)
    }

    fun buyHouse(player: Player) {
        // Check if Property is Mortgaged
        if (mortgaged.value!!) {
            Log.e(LOG_TAG, "Tried to build house on a Mortgaged property, property = $name")
            return
        }

        // Check if the Property is not set
        if (!set) {
            Log.e(LOG_TAG, "Tried to build house on a Non-Set Property, property = $name")
            return
        }

        // Check if we can build any more houses
        if (canBuy()) {
            // Check if this player owns the property
            if (player.cardId != playerId) {
                Log.e(
                    LOG_TAG,
                    "Player does not own this property. property = $name, player = ${player.name}"
                )
                return
            }

            if (increaseRentLevel())
                player.pay(housePrice)

        } else {
            Log.e(LOG_TAG, "Cannot build any more houses, property = $name")
        }
    }

    fun sellHouse(player: Player) {
        // Check if we can sell any more houses
        if (canSell()) {
            // Check if this player owns the property
            if (player.cardId != playerId) {
                Log.e(
                    LOG_TAG,
                    "Player does not own this property. property = $name, player = ${player.name}"
                )
                return
            }

            if (decreaseRentLevel())
                player.collect(houseSellPrice)

        } else {
            Log.e(LOG_TAG, "Cannot sell any more houses, property = $name")
        }
    }

    fun canBuy(): Boolean {
        return set && !mortgaged.value!! && currentRentLevel.value!! < rent.size - 1
    }

    fun canSell(): Boolean = currentRentLevel.value!! > 0

    override fun setMortgageStatus(status: Boolean) {
        super.setMortgageStatus(status)
        set = false
        megaSet = false
    }

    override fun reset() {
        super.reset()
        set = false
        megaSet = false
    }

}