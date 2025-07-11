package com.example.finances.core.di.common

import com.example.finances.core.di.CoreComponent
import com.example.finances.core.utils.NetworkConnectionObserver
import com.example.finances.core.utils.usecases.ConvertAmountUseCase
import retrofit2.Retrofit
import java.lang.ref.WeakReference

object CoreComponentAdapter {
    private const val NO_CORE_COMPONENT_INSTANCE = "The instance of CoreComponent does not exist"

    private var coreComponentReference: WeakReference<CoreComponent>? = null

    fun networkConnectionObserver(dependencies: CoreDependencies): NetworkConnectionObserver {
        val coreComponent = CoreComponent.create(dependencies)
        coreComponentReference = WeakReference(coreComponent)
        coreComponent.splashScreenAnimator()
        return coreComponent.networkConnectionObserver()
    }

    fun retrofit(): Retrofit{
        return coreComponentReference?.get()?.retrofit() ?: throw NullPointerException(
            NO_CORE_COMPONENT_INSTANCE
        )
    }

    fun convertAmountUseCase(): ConvertAmountUseCase {
        return coreComponentReference?.get()?.convertAmountUseCase() ?: throw NullPointerException(
            NO_CORE_COMPONENT_INSTANCE
        )
    }
}
