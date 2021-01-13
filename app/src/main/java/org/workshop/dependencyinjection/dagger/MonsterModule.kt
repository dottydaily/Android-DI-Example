package org.workshop.dependencyinjection.dagger

import dagger.Module
import dagger.Provides
import org.workshop.dependencyinjection.model.Armor
import org.workshop.dependencyinjection.model.Weapon

@Module
class MonsterModule {
    @Provides @EquipmentScope
    fun provideWeapon(): Weapon = Weapon()

    @Provides @EquipmentScope
    fun provideArmor(): Armor = Armor()
}