package org.workshop.dependencyinjection.dagger

import dagger.Subcomponent
import org.workshop.dependencyinjection.MainActivity

@Subcomponent
interface GameStatComponent {

    // Factory that uses for create this sub-component.
    @Subcomponent.Factory
    interface Factory {
        fun create(): GameStatComponent
    }

    fun inject(mainActivity: MainActivity)
}