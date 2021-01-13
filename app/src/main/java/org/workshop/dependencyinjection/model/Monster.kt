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

    enum class MonsterState { NORMAL, GUARD }
    var state: MonsterState = MonsterState.NORMAL

    fun attack(target: Monster) {
        val elementMultiplier = getElementMultiplier(target)
        val damage = (totalAtk - (0.25 * totalDef)) * elementMultiplier
        target.hp -= (damage * if (state == MonsterState.GUARD) 0.5 else 1.0)
        Log.d("DependencyInjection", "${target.name} takes $damage damage by $name!")
        Log.d("DependencyInjection", "${target.name}'s current hp is ${target.hp}")
    }

    private fun getElementMultiplier(target: Monster): Double {
        return ElementHelper.getElementMultiplier(weapon.element, target.armor.element)
    }
}