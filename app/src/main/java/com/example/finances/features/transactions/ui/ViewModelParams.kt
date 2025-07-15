package com.example.finances.features.transactions.ui

import androidx.lifecycle.viewmodel.CreationExtras
import com.example.finances.features.transactions.domain.ScreenType

sealed interface ViewModelParams {
    data object Screen : CreationExtras.Key<ScreenType>, ViewModelParams
    data object TransactionId : CreationExtras.Key<Int?>, ViewModelParams
}
