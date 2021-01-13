package org.workshop.dependencyinjection.model

import android.util.Log
import org.workshop.dependencyinjection.dagger.EquipmentScope
import javax.inject.Inject
import javax.inject.Named
import kotlin.random.Random

class Monster @Inject constructor(
    val name: String,
    val baseAtk: Double,
    val baseDef: Double,
    var hp: Double,
    val weapon: Weapon,
    val armor: Armor
) {
    val totalAtk get() = baseAtk + weapon.atk
    val totalDef get() = baseDef + armor.def
    val isDead get() = state == MonsterState.DEAD

    enum class MonsterState(name: String) {
        NORMAL("NORMAL"), GUARD("GUARD"), DEAD("DEAD") }
    var state: MonsterState = MonsterState.NORMAL

    fun attack(target: Monster?): String {
        target?.let {
            val elementMultiplier = getElementMultiplier(target)
            val damage = (totalAtk - (0.25 * totalDef)) * elementMultiplier
            val reduction = if (target.state == MonsterState.GUARD) 0.5 else 1.0
            val diversion = Random.nextDouble(0.7, 1.0)
            val totalDamage = damage * reduction * diversion
            target.hp -= totalDamage

            var description = "${target.name} takes ${totalDamage.toInt()} damage by $name!"
            description += if (reduction == 0.5) "\n with 50% damage reduction." else ""

            if (target.hp <= 0) {
                target.hp = 0.0
                target.state = MonsterState.DEAD
                description += "\n${target.name}'s dead!"
            } else {
                description += "\n${target.name}'s current hp is ${target.hp.toInt()}"
            }

            Log.d("DependencyInjection", description)
            return description
        }

        return ""
    }

    private fun getElementMultiplier(target: Monster): Double {
        return ElementHelper.getElementMultiplier(weapon.element, target.armor.element)
    }
}