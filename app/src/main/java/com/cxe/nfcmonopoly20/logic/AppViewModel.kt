package com.cxe.nfcmonopoly20.logic

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.cxe.nfcmonopoly20.logic.player.CardId
import com.cxe.nfcmonopoly20.logic.player.Player

const val STARTING_MONEY = 1500
const val STARTING_MONEY_MEGA = 2500
class AppViewModel: ViewModel() {

    val LOG_TAG = "AppViewModel"

    var mega: Boolean = false

    val playerList = mutableListOf<Player>()
    val playerMap = mutableMapOf<CardId, Player>()

    var freeParking: Int = 0

    fun addPlayer(player: Player) {
        if (!playerMap.containsKey(player.cardId)) {
            playerMap[player.cardId] = player
            playerList.add(player)
        } else {
            Log.w(LOG_TAG, "Player already exists")
        }
    }

    fun handleNfcIntent(msg: String, currentFragment: Fragment) {

    }

}