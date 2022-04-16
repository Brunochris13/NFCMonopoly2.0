package com.cxe.nfcmonopoly20.logic.property

class UtilityProperty(
    id: PropertyId,
    name: String,
    price: Int,
    rent: IntArray,
    mortgagedValue: Int,
    val utilityType: UtilityType
    ) : Property(id, name, price, rent, mortgagedValue) {

    enum class UtilityType {
        ELECTRICITY,
        WATER,
        GAS
    }
}