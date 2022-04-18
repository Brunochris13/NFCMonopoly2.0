package com.cxe.nfcmonopoly20.logic.player

import com.cxe.nfcmonopoly20.logic.property.Property
import java.io.Serializable

class Player(var name: String, val cardId: CardId) : Serializable {

    var money: Int = 0
    var moneyShown = true

    val properties = mutableListOf<Property>()

    fun pay(amount: Int) {
        money -= amount
    }

    fun collect(amount: Int) {
        money += amount
    }

    fun reset() {
        for (property in properties)
            property.reset()
        properties.clear()
    }
}