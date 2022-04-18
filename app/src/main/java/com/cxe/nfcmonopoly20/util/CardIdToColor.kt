package com.cxe.nfcmonopoly20.util

import android.content.Context
import com.cxe.nfcmonopoly20.R
import com.cxe.nfcmonopoly20.logic.player.CardId

object CardIdToColor {
    @JvmStatic
    fun convert(context: Context, cardId: CardId): Int {
        return when(cardId) {
            CardId.RED_CARD -> context.getColor(R.color.player_red)
            CardId.BLUE_CARD -> context.getColor(R.color.player_blue)
            CardId.YELLOW_CARD -> context.getColor(R.color.player_yellow)
            CardId.GREEN_CARD -> context.getColor(R.color.player_green)
            CardId.PINK_CARD -> context.getColor(R.color.player_pink)
            CardId.ORANGE_CARD -> context.getColor(R.color.player_orange)
            CardId.BROWN_CARD -> context.getColor(R.color.player_brown)
            CardId.PURPLE_CARD -> context.getColor(R.color.player_purple)
        }
    }
}