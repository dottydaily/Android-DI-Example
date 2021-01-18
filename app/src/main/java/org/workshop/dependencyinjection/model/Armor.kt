package org.workshop.dependencyinjection.model

import android.util.Log
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

data class Armor @Inject constructor(val def: Double, val element: Element) {
    init {
        Log.d("DependencyInjection", "Armor has been created at ${Date()}\n\t>>$this")
    }
}