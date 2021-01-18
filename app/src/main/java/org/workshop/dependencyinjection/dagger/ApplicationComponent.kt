package org.workshop.dependencyinjection.dagger

import dagger.Component
import org.workshop.dependencyinjection.MainViewModel
import org.workshop.dependencyinjection.PokemonApplication
import javax.inject.Singleton

// Define of the Application Graph
@Singleton
@Component(modules = [MonsterModule::class, SubComponentsModule::class])
interface ApplicationComponent {
    fun inject(mainViewModel: MainViewModel)
    fun inject(pokemonApplication: PokemonApplication)

    // for creating the instance of GameStat
    fun gameStatComponent(): GameStatComponent.Factory
}