package com.cxe.nfcmonopoly20.logic.player

import com.cxe.nfcmonopoly20.logic.property.Property

class Player(var name: String, val cardId: CardId) {

    var money: Int = 0
    val properties = mutableListOf<Property>()

    fun pay(amount: Int) {
        money -= amount
    }

    fun collect(amount: Int) {
        money += amount
    }
}