package com.cxe.nfcmonopoly20.logic.property

class ColorProperty(
    id: PropertyId,
    name: String,
    price: Int,
    rent: Array<Int>,
    mortgagedValue: Int,
    val color: PropertyColors,
    val housePrice: Int,
    val colorSetPropertyAmount: Int
) : Property(id, name, price, rent, mortgagedValue) {

    var set = false

    // Mega Edition Only
    var megaSet = false

    enum class PropertyColors {
        BROWN,
        LIGHT_BLUE,
        PINK,
        ORANGE,
        RED,
        YELLOW,
        GREEN,
        BLUE
    }

}