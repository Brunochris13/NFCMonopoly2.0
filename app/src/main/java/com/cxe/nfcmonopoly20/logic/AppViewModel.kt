package com.cxe.nfcmonopoly20.logic

import android.util.Log
import androidx.lifecycle.ViewModel
import com.cxe.nfcmonopoly20.logic.player.CardId
import com.cxe.nfcmonopoly20.logic.player.Player
import com.cxe.nfcmonopoly20.logic.property.*

// Constants
const val DEBUG_MODE = false

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
const val PROPERTY_DIALOG_TAG = "property_dialog_tag"
const val AUCTION_DIALOG_TAG = "auction_dialog_tag"
const val WINNING_DIALOG_TAG = "winning_dialog_tag"

private const val LOG_TAG = "AppViewModel"

class AppViewModel : ViewModel() {

    // Mega Edition
    var mega: Boolean = false

    // Player
    val playerList = mutableListOf<Player>()
    val playerMap = mutableMapOf<CardId, Player>()

    // Free Parking
    var freeParking: Int = 0

    // Properties
    // All Properties
    private var _properties = mutableMapOf<PropertyId, Property>()
    val properties = _properties

    // Color Properties
    private var _colorProperties =
        mutableMapOf<ColorProperty.PropertyColors, MutableList<ColorProperty>>()
    val colorProperties = _colorProperties

    // Station Properties
    private var _stationProperties = mutableListOf<StationProperty>()
    val stationProperties = _stationProperties

    // Utility Properties
    private var _utilityProperties = mutableListOf<UtilityProperty>()
    val utilityProperties = _utilityProperties

    // Functions
    // =========

    // Player Functions
    fun addPlayer(player: Player) {
        if (!playerMap.containsKey(player.cardId)) {
            playerMap[player.cardId] = player
            playerList.add(player)
        } else {
            Log.w(LOG_TAG, "Player already exists")
        }
    }

    fun deletePlayer(player: Player) {
        if (playerMap.containsKey(player.cardId)) {
            player.reset()
            playerMap.remove(player.cardId)
            playerList.remove(player)
        } else {
            Log.e(LOG_TAG, "Player did not exist, so it could not be deleted")
        }
    }

    fun playerPay(cardId: CardId, amount: Int) = playerMap[cardId]?.pay(amount)

    fun playerCollect(cardId: CardId, amount: Int) = playerMap[cardId]?.collect(amount)

    fun playerBuyProperty(cardId: CardId, property: Property, amount: Int = property.price) {
        // Check if Player exists
        if (!playerMap.containsKey(cardId)) {
            Log.e(LOG_TAG, "Player with cardId = ${cardId.name} does not exist")
            return
        }

        if (property.playerId == null) {
            playerPay(cardId, amount)
            property.playerId = cardId
            playerMap[cardId]?.addProperty(property)
        } else {
            Log.e(
                LOG_TAG,
                "Player tried to buy property that is already owned by a Player, cardId = $cardId, property = ${property.name}"
            )
        }
    }

    fun playerPaysRent(cardId: CardId, property: Property, diceValue: Int? = null): Int? {
        val rent = if (diceValue == null) {
            property.getCurrentRent()
        } else {
            // Utility Property
            property.getCurrentRent()?.times(diceValue)
        }
        if (rent != null) {
            playerPay(cardId, rent)
            playerCollect(property.playerId!!, rent)
        } else {
            Log.e(LOG_TAG, "Rent equals null, property = ${property.name}")
        }
        return rent
    }

    fun playerPaysPlayer(player1: Player, player2: Player, amount: Int): Boolean {
        // Check if Player 1 exists
        if (!playerMap.containsKey(player1.cardId)) {
            Log.e(LOG_TAG, "Player = ${player1.name} does not exist")
            return false
        }

        // Check if Player 2 exists
        if (!playerMap.containsKey(player2.cardId)) {
            Log.e(LOG_TAG, "Player = ${player2.name} does not exist")
            return false
        }

        player1.pay(amount)
        player2.collect(amount)
        return true
    }

    fun playersTradeProperties(
        player1: Player,
        player1Properties: List<Property>,
        player2: Player,
        player2Properties: List<Property>
    ): Boolean {
        // Check if Player 1 exists
        if (!playerMap.containsKey(player1.cardId)) {
            Log.e(LOG_TAG, "Player = ${player1.name} does not exist")
            return false
        }

        // Check if Player 2 exists
        if (!playerMap.containsKey(player2.cardId)) {
            Log.e(LOG_TAG, "Player = ${player2.name} does not exist")
            return false
        }

        for (property in player1Properties) {
            val result = player1.removeProperty(property)
            // If Property could not be removed from Player 1
            if (!result) {
                Log.e(LOG_TAG, "property = ${property.name} could not be removed from player = ${player1.name}")
                return false
            }
            player2.addProperty(property)
            property.playerId = player2.cardId
        }

        for (property in player2Properties) {
            val result = player2.removeProperty(property)
            // If Property could not be removed from Player 2
            if (!result) {
                Log.e(LOG_TAG, "property = ${property.name} could not be removed from player = ${player2.name}")
                return false
            }
            player1.addProperty(property)
            property.playerId = player1.cardId
        }

        return true
    }

    // Free Parking Functions
    fun payFreeParking(card: CardId, amount: Int) {
        playerPay(card, amount)
        freeParking += amount
    }

    fun collectFreeParking(cardId: CardId) {
        playerCollect(cardId, freeParking)
        freeParking = 0
    }

    // Property Functions
    fun addProperty(property: Property) {
        // Check if Property is already added
        if (properties.containsKey(property.id)) {
            Log.e(LOG_TAG, "Property ${property.name} already exists")
            return
        }

        // Add Property to the Property List
        properties[property.id] = property

        when (property) {
            is ColorProperty -> { // ColorProperty
                if (!colorProperties.containsKey(property.color))
                    colorProperties[property.color] = mutableListOf()
                colorProperties[property.color]?.add(property)
            }
            is StationProperty -> { // Station Property
                stationProperties.add(property)
            }
            is UtilityProperty -> { // Utility Property
                utilityProperties.add(property)
            }
        }
    }

    fun mortgageProperty(property: Property) {
        // Check if Property is already Mortgaged
        if (property.mortgaged.value == true) {
            // Player has to pay to Unmortgage the Property
            playerPay(property.playerId!!, property.mortgagedValue)
        } else {
            // Player receives money
            playerCollect(property.playerId!!, property.mortgagedValue)
        }
        property.setMortgageStatus(!property.mortgaged.value!!)

        // Update Similar Property Status ONLY of the same Player
        val player = playerMap[property.playerId]
        player?.updatePropertyStatus(property)
    }

    // General Functions
    fun isCardId(msg: String): Boolean {
        val cardIds = CardId.values()
        return cardIds.any { it.name == msg }
    }

    fun isProperty(msg: String): Boolean {
        val propertyIds = PropertyId.values()
        return propertyIds.any { it.name == msg }
    }

    fun resetGame() {
        // Players
        for (player in playerList)
            player.reset()
        playerList.clear()
        playerMap.clear()

        // Free Parking
        freeParking = 0

        // Properties
        for (property in properties.values)
            property.reset()
        properties.clear()
        colorProperties.clear()
        stationProperties.clear()
        utilityProperties.clear()
    }

}