package com.cxe.nfcmonopoly20.logic.player

import com.cxe.nfcmonopoly20.logic.property.Property

class Player(val name: String, val cardId: CardId, val money: Int) {

    val properties = mutableListOf<Property>()
}