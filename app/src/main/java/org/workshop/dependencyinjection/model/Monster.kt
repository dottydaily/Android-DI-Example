package org.workshop.dependencyinjection.model

import android.util.Log

class Monster(
    val name: String,
    val baseAtk: Double,
    val baseDef: Double,
    var hp: Double,
    val weapon: Weapon,
    val armor: Armor
) {
    val totalAtk get() = baseAtk + weapon.atk
    val totalDef get() = baseDef + armor.def

    fun attack(target: Monster) {
        val elementMultiplier = getElementMultiplier(target)
        val damage = (totalAtk - (0.25 * totalDef)) * elementMultiplier
        target.hp -= damage
        Log.d("DependencyInjection", "${target.name} takes $damage damage by $name!")
        Log.d("DependencyInjection", "${target.name}'s current hp is ${target.hp}")
    }

    private fun getElementMultiplier(target: Monster): Double {
        return ElementHelper.getElementMultiplier(weapon.element, target.armor.element)
    }
}