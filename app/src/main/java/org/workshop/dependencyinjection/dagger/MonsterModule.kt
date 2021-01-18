package org.workshop.dependencyinjection.dagger

import dagger.Module
import dagger.Provides
import org.workshop.dependencyinjection.model.Armor
import org.workshop.dependencyinjection.model.Element
import org.workshop.dependencyinjection.model.Monster
import org.workshop.dependencyinjection.model.Weapon
import javax.inject.Named
import kotlin.random.Random

@Module
class MonsterModule {
    companion object {
        const val MONSTER_PIKACHU = "MONSTER_PIKACHU"
        const val MONSTER_LIZARDON = "MONSTER_LIZARDON"
        const val RANDOM_WEAPON = "RANDOM_WEAPON"
        const val RANDOM_ARMOR = "RANDOM_ARMOR"
    }

    // @Provides tell Dagger how to create instances of the type that this function
    // returns (i.e. Weapon).
    // Function parameters are the dependencies of this type.
    @Provides
    @Named(RANDOM_WEAPON)
    fun provideWeapon(): Weapon {
        return Weapon(Random.nextDouble(500.0, 1000.0),
            when (Random.nextInt(1, 6)) {
                1 -> Element.FIRE
                2 -> Element.WATER
                3 -> Element.EARTH
                4 -> Element.WIND
                5 -> Element.LIGHT
                else -> Element.DARK
            }
        )
    }

    @Provides
    @Named(RANDOM_ARMOR)
    fun provideArmor(): Armor {
        return Armor(
            Random.nextDouble(1.0, 100.0),
            when (Random.nextInt(1, 6)) {
                1 -> Element.FIRE
                2 -> Element.WATER
                3 -> Element.EARTH
                4 -> Element.WIND
                5 -> Element.LIGHT
                else -> Element.DARK
            }
        )
    }

    @Provides
    @Named(MONSTER_PIKACHU)
    fun provideMonsterPikachu(
        @Named(RANDOM_WEAPON) weapon: Weapon,
        @Named(RANDOM_ARMOR) armor: Armor
    ): Monster {
        val randomHp = Random.nextInt(1200, 1500).toDouble()
        return Monster("Pikachu", 200.0, 50.0, randomHp, weapon, armor)
    }

    @Provides
    @Named(MONSTER_LIZARDON)
    fun provideMonsterLizardon(
        @Named(RANDOM_WEAPON) weapon: Weapon,
        @Named(RANDOM_ARMOR) armor: Armor
    ): Monster {
        val randomHp = Random.nextInt(1200, 1500).toDouble()
        return Monster("Lizardon", 100.0, 100.0, randomHp, weapon, armor)
    }
}