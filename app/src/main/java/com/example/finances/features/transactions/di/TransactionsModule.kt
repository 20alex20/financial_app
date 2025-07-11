package com.example.finances.features.transactions.di

import androidx.lifecycle.ViewModel
import com.example.finances.core.di.ActivityScope
import com.example.finances.core.utils.viewmodel.ViewModelKey
import com.example.finances.features.transactions.data.TransactionsApi
import com.example.finances.features.transactions.data.TransactionsRepoImpl
import com.example.finances.features.transactions.domain.repository.TransactionsRepo
import com.example.finances.features.transactions.ui.models.ExpensesCreateUpdateViewModel
import com.example.finances.features.transactions.ui.models.ExpensesHistoryViewModel
import com.example.finances.features.transactions.ui.models.ExpensesViewModel
import com.example.finances.features.transactions.ui.models.IncomeCreateUpdateViewModel
import com.example.finances.features.transactions.ui.models.IncomeHistoryViewModel
import com.example.finances.features.transactions.ui.models.IncomeViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import retrofit2.Retrofit

@Module
interface TransactionsModule {
    @Binds
    @IntoMap
    @ViewModelKey(ExpensesViewModel::class)
    fun bindsExpensesViewModel(expensesViewModel: ExpensesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(IncomeViewModel::class)
    fun bindsIncomeViewModel(incomeViewModel: IncomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExpensesHistoryViewModel::class)
    fun bindsExpensesHistoryViewModel(expensesHistoryViewModel: ExpensesHistoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(IncomeHistoryViewModel::class)
    fun bindsIncomeHistoryViewModel(incomeHistoryViewModel: IncomeHistoryViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(ExpensesCreateUpdateViewModel::class)
    fun bindsExpensesCreateUpdateViewModel(
        expensesCreateUpdateViewModel: ExpensesCreateUpdateViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(IncomeCreateUpdateViewModel::class)
    fun bindsIncomeCreateUpdateViewModel(
        incomeCreateUpdateViewModel: IncomeCreateUpdateViewModel
    ): ViewModel

    @Binds
    @ActivityScope
    fun bindsTransactionsRepoImpl(transactionsRepoImpl: TransactionsRepoImpl): TransactionsRepo

    companion object {
        @Provides
        @ActivityScope
        fun providesTransactionsApi(retrofit: Retrofit): TransactionsApi {
            return retrofit.create(TransactionsApi::class.java)
        }
    }
}
