package org.workshop.dependencyinjection.model

enum class Element { FIRE, WATER, EARTH, WIND, LIGHT, DARK }
abstract class ElementHelper {
    companion object {
        fun getElementMultiplier(atkElement: Element, defElement: Element): Double {
            return when (atkElement) {
                Element.FIRE -> {
                    when (defElement) {
                        Element.WATER -> 0.8
                        Element.EARTH -> 1.2
                        else -> 1.0
                    }
                }
                Element.WATER -> {
                    when (defElement) {
                        Element.WIND -> 0.8
                        Element.FIRE -> 1.2
                        else -> 1.0
                    }
                }
                Element.EARTH -> {
                    when (defElement) {
                        Element.FIRE -> 0.8
                        Element.WIND -> 1.2
                        else -> 1.0
                    }
                }
                Element.WIND -> {
                    when (defElement) {
                        Element.EARTH -> 0.8
                        Element.WATER -> 1.2
                        else -> 1.0
                    }
                }
                Element.LIGHT -> {
                    when (defElement) {
                        Element.DARK -> 1.2
                        else -> 1.0
                    }
                }
                Element.DARK -> {
                    when (defElement) {
                        Element.LIGHT -> 1.2
                        else -> 1.0
                    }
                }
            }
        }
    }
}