package com.cxe.nfcmonopoly20.util

import android.content.Context
import com.cxe.nfcmonopoly20.R
import com.cxe.nfcmonopoly20.logic.property.ColorProperty

object PropertyColorToResourcesConverter {

    @JvmStatic
    fun convert(context: Context, propertyColor: ColorProperty.PropertyColors): Int {
        return when(propertyColor) {
            ColorProperty.PropertyColors.BROWN -> context.getColor(R.color.property_brown)
            ColorProperty.PropertyColors.LIGHT_BLUE -> context.getColor(R.color.property_light_blue)
            ColorProperty.PropertyColors.PINK -> context.getColor(R.color.property_pink)
            ColorProperty.PropertyColors.ORANGE -> context.getColor(R.color.property_orange)
            ColorProperty.PropertyColors.RED -> context.getColor(R.color.property_red)
            ColorProperty.PropertyColors.YELLOW -> context.getColor(R.color.property_yellow)
            ColorProperty.PropertyColors.GREEN -> context.getColor(R.color.property_green)
            ColorProperty.PropertyColors.BLUE -> context.getColor(R.color.property_blue)
        }
    }
}