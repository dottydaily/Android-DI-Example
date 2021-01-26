package org.workshop.dependencyinjection.hilt

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ServiceComponent
import org.workshop.dependencyinjection.model.Armor
import org.workshop.dependencyinjection.model.Element
import org.workshop.dependencyinjection.model.Monster
import org.workshop.dependencyinjection.model.Weapon
import javax.inject.Qualifier
import kotlin.random.Random

@Module
@InstallIn(ServiceComponent::class)
object MonsterModule {
    @RandomWeapon
    @Provides
    fun provideWeapon(): Weapon {
        return Weapon(
            Random.nextDouble(500.0, 1000.0),
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

    @RandomArmor
    @Provides
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

    @MonsterPikachu
    @Provides
    fun provideMonsterPikachu(
        @RandomWeapon weapon: Weapon,
        @RandomArmor armor: Armor
    ): Monster {
        val randomHp = Random.nextInt(1200, 1500).toDouble()
        return Monster("Pikachu", 200.0, 50.0, randomHp, weapon, armor)
    }

    @MonsterLizardon
    @Provides
    fun provideMonsterLizardon(
        @RandomWeapon weapon: Weapon,
        @RandomArmor armor: Armor
    ): Monster {
        val randomHp = Random.nextInt(1200, 1500).toDouble()
        return Monster("Lizardon", 100.0, 100.0, randomHp, weapon, armor)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RandomWeapon

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RandomArmor

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MonsterPikachu

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MonsterLizardon