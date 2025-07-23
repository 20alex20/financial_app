package com.example.finances.feature.transactions.ui

import androidx.lifecycle.viewmodel.CreationExtras
import com.example.finances.feature.transactions.navigation.ScreenType

sealed interface ViewModelParams {
    data object Screen : CreationExtras.Key<ScreenType>, ViewModelParams
    data object TransactionId : CreationExtras.Key<Int?>, ViewModelParams
}
