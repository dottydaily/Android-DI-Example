package org.workshop.dependencyinjection.dagger

import dagger.Module

@Module(subcomponents = [GameStatComponent::class])
class SubComponentsModule { }