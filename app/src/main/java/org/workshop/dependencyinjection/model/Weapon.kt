package org.workshop.dependencyinjection.model

import javax.inject.Inject
import kotlin.random.Random

data class Weapon @Inject constructor(val atk: Double, val element: Element)