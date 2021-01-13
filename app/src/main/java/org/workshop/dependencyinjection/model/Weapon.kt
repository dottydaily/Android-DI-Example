package org.workshop.dependencyinjection.model

import kotlin.random.Random

data class Weapon(val atk: Double, val element: Element) {
    constructor() : this(
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