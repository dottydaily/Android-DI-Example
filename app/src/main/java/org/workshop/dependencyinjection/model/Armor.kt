package org.workshop.dependencyinjection.model

import org.workshop.dependencyinjection.dagger.EquipmentScope
import javax.inject.Inject
import kotlin.random.Random

@EquipmentScope
data class Armor (val def: Double, val element: Element) {
    @Inject constructor() : this(
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