package org.workshop.dependencyinjection.dagger

import dagger.Component
import org.workshop.dependencyinjection.model.Monster

// Define of the Application Graph
@Component(modules = [MonsterModule::class])
interface ApplicationComponent {
    fun monster(): Monster
}