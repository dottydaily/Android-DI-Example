package org.workshop.dependencyinjection.model

import kotlin.random.Random

data class Armor(val def: Double, val element: Element) {
    constructor() : this(
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