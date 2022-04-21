package com.cxe.nfcmonopoly20.logic.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cxe.nfcmonopoly20.logic.property.ColorProperty
import com.cxe.nfcmonopoly20.logic.property.Property
import java.io.Serializable

class Player(var name: String, val cardId: CardId) : Serializable {

    private val _money = MutableLiveData<Int>()
    val money: LiveData<Int>
        get() = _money
    var moneyShown = true

    var properties = mutableListOf<Property>()

    fun pay(amount: Int) {
        _money.value = _money.value?.minus(amount)
    }

    fun collect(amount: Int) {
        _money.value = _money.value?.plus(amount)
    }

    fun setStartingMoney(startingMoney: Int) {
        _money.value = startingMoney
    }

    fun addProperty(property: Property) {
        properties.add(property)

        // Update Property Status
        updatePropertyStatus(property)
    }

    fun updatePropertyStatus(property: Property) {
        // Check how many Properties of this Type the Player has
        when (property) {
            is ColorProperty -> {
                val sameColorProperties = properties.filterIsInstance<ColorProperty>()
                    .filter { it.color == property.color && !it.mortgaged.value!! }

                property.set = property.colorSetPropertyAmount == sameColorProperties.size

                // Set same color Properties set value
                for (sameColorProperty in sameColorProperties) {
                    sameColorProperty.set = property.set
                }
            }
        }
    }

    fun sortProperties() {
        properties = properties.sortedBy { it.id }.toMutableList()
    }

    fun getSameColorProperties(colorProperty: ColorProperty): List<ColorProperty> {
        return properties.filterIsInstance<ColorProperty>()
            .filter { it.color == colorProperty.color }
    }

    fun reset() {
        for (property in properties)
            property.reset()
        properties.clear()
    }
}