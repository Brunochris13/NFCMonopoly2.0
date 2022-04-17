package com.cxe.nfcmonopoly20.logic.property

// Mega Edition Only
private const val DEPOT_PRICE = 100

class StationProperty(
    id: PropertyId,
    name: String,
    price: Int,
    rent: IntArray,
    mortgagedValue: Int
) : Property(id, name, price, rent, mortgagedValue) {

    // Mega Edition Only
    var depot = false

    override fun getRent(level: Int): Int {
        val rent = super.getRent(level)
        // If Station Property has a depot, then the rent is Double
        return if (depot) rent * 2 else rent
    }

    override fun reset() {
        super.reset()
        depot = false
    }
}