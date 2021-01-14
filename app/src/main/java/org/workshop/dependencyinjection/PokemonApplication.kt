package org.workshop.dependencyinjection

import android.app.Application
import android.util.Log
import org.workshop.dependencyinjection.dagger.ApplicationComponent
import org.workshop.dependencyinjection.dagger.DaggerApplicationComponent
import org.workshop.dependencyinjection.dagger.MonsterModule

class PokemonApplication: Application() {
    companion object {
        lateinit var appComponent: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()

        Log.d("DEPENDENCY-INJECTION", "PokemonApplication has been created.")
        appComponent = DaggerApplicationComponent.builder().monsterModule(MonsterModule()).build()
        appComponent.inject(this)
    }
}