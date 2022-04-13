package com.cxe.nfcmonopoly20.logic

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.cxe.nfcmonopoly20.logic.player.CardId
import com.cxe.nfcmonopoly20.logic.player.Player
import com.cxe.nfcmonopoly20.ui.home.HomeFragment

const val STARTING_MONEY = 1500
const val STARTING_MONEY_MEGA = 2500

class AppViewModel : ViewModel() {

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

    fun handleNfcIntent(msg: String, currentFragment: Fragment): String? {
        val cardIds = CardId.values()

        if (currentFragment is HomeFragment) {
            // CardId
            if (isCardId(msg, cardIds)) {

            } else {
                return "Wrong Card!"
            }
        } else {
            Log.i(LOG_TAG, "NFC intent on Fragment with no action")
        }

        return null
    }

    private fun isCardId(msg: String, cardIds: Array<CardId>): Boolean {
        return cardIds.any {it.name == msg}
    }

    private fun isProperty(msg: String): Boolean {
        return false
    }


}