package com.example.finances.core.di

/**
 * Base interface for all feature components.
 * Each feature module should implement this interface in their component.
 */
interface FeatureComponent {
    interface Factory<T : FeatureComponent> {
        fun create(activityComponent: ActivityComponent): T
    }
} 