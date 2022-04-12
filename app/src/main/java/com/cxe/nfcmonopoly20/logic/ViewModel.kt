package com.cxe.nfcmonopoly20.logic

import androidx.lifecycle.ViewModel
import com.cxe.nfcmonopoly20.logic.player.CardId
import com.cxe.nfcmonopoly20.logic.player.Player

class ViewModel: ViewModel() {

    object ViewModel {
        const val STARTING_MONEY = 1500
        const val STARTING_MONEY_MEGA = 2500
    }

    var mega: Boolean = false

    val playerList = mutableListOf<Player>()
    val playerMap = mutableMapOf<CardId, Player>()

    var freeParking: Int = 0

}