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