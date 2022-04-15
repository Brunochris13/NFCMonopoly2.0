package com.cxe.nfcmonopoly20.logic

import android.util.Log
import androidx.lifecycle.ViewModel
import com.cxe.nfcmonopoly20.logic.player.CardId
import com.cxe.nfcmonopoly20.logic.player.Player

// Constants
const val STARTING_MONEY = 1500
const val STARTING_MONEY_MEGA = 2500
const val GO_AMOUNT = 200
const val PRISON_AMOUNT = 50
const val LUXURY_TAX_AMOUNT = 100
const val INCOME_TAX_AMOUNT = 200

const val CARD_ID_TAG = "cardIdTag"
const val PLAYER_NAME_TAG = "playerNameTag"
const val NFC_TAP_DIALOG_TAG = "nfc_tap_dialog_tag"
const val CUSTOM_AMOUNT_DIALOG_TAG = "custom_amount_dialog_tag"
const val BANK_OR_FREE_PARKING_DIALOG_TAG = "bank_or_free_parking_dialog_tag"

private const val LOG_TAG = "AppViewModel"

class AppViewModel : ViewModel() {

    var mega: Boolean = false

    val playerList = mutableListOf<Player>()
    val playerMap = mutableMapOf<CardId, Player>()

    var freeParking: Int = 0

    private val cardIds = CardId.values()

    fun addPlayer(player: Player) {
        if (!playerMap.containsKey(player.cardId)) {
            playerMap[player.cardId] = player
            playerList.add(player)
        } else {
            Log.w(LOG_TAG, "Player already exists")
        }
    }

    fun deletePlayer(player: Player) {
        if (!playerMap.containsKey(player.cardId)) {
            playerMap.remove(player.cardId)
            playerList.remove(player)
        } else {
            Log.e(LOG_TAG, "Player did not exist, so it could not be deleted")
        }
    }

    fun playerPay(cardId: CardId, amount: Int) = playerMap[cardId]?.pay(amount)

    fun playerCollect(cardId: CardId, amount: Int) = playerMap[cardId]?.collect(amount)

    fun isCardId(msg: String): Boolean {
        return cardIds.any { it.name == msg }
    }

    fun isProperty(msg: String): Boolean {
        return false
    }

    fun resetGame() {
        playerList.clear()
        playerMap.clear()
        freeParking = 0
        // TODO: Clear Properties
    }

}