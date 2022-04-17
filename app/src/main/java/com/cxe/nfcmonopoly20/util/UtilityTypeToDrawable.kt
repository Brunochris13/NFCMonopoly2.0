package com.cxe.nfcmonopoly20.util

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import com.cxe.nfcmonopoly20.R
import com.cxe.nfcmonopoly20.logic.property.UtilityProperty

object UtilityTypeToDrawable {
    @JvmStatic
    fun convert(context: Context,  utilityType: UtilityProperty.UtilityType): Drawable? {
        return when(utilityType) {
            UtilityProperty.UtilityType.ELECTRICITY -> AppCompatResources.getDrawable(context, R.drawable.monopoly_light_bulb)
            UtilityProperty.UtilityType.WATER -> AppCompatResources.getDrawable(context, R.drawable.monopoly_water_tap)
            UtilityProperty.UtilityType.GAS -> AppCompatResources.getDrawable(context, R.drawable.monopoly_gas_icon)
        }
    }
}