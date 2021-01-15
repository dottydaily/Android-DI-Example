package org.workshop.dependencyinjection.model

import javax.inject.Inject
import kotlin.random.Random

data class Armor @Inject constructor(val def: Double, val element: Element)