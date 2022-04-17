package com.cxe.nfcmonopoly20.logic.property

class ColorProperty(
    id: PropertyId,
    name: String,
    price: Int,
    rent: IntArray,
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

    override fun getRent(level: Int): Int {
        // If Property is a Set
        return if (level == 0 && set) {
            super.getRent(level) * 2
        } else
            super.getRent(level)
    }

    fun getColorResource() {
        
    }

    override fun reset() {
        super.reset()
        set = false
        megaSet = false
    }

}