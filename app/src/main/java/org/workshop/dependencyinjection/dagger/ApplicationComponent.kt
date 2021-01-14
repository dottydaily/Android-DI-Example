package org.workshop.dependencyinjection.dagger

import dagger.Component
import org.workshop.dependencyinjection.MainActivity
import org.workshop.dependencyinjection.MainViewModel
import org.workshop.dependencyinjection.PokemonApplication
import org.workshop.dependencyinjection.model.Armor
import org.workshop.dependencyinjection.model.Monster
import org.workshop.dependencyinjection.model.Weapon
import javax.inject.Singleton

// Define of the Application Graph
@Singleton
@Component(modules = [MonsterModule::class])
interface ApplicationComponent {
    fun inject(monster: Monster)
    fun inject(pokemonApplication: PokemonApplication)
}