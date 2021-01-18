package org.workshop.dependencyinjection.model

import android.util.Log
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

data class Weapon @Inject constructor(val atk: Double, val element: Element) {
    init {
        Log.d("DependencyInjection", "Weapon has been created at ${Date()}\n\t>>$this")
    }
}