package com.example.finances.features.transactions.di

import com.example.finances.core.di.ActivityScope
import com.example.finances.core.utils.viewmodel.BaseViewModel
import com.example.finances.core.utils.viewmodel.ViewModelKey
import com.example.finances.features.transactions.data.database.TransactionsApi
import com.example.finances.features.transactions.data.TransactionsRepoImpl
import com.example.finances.features.transactions.domain.repository.TransactionsRepo
import com.example.finances.features.transactions.ui.CreateUpdateViewModel
import com.example.finances.features.transactions.ui.ExpensesIncomeViewModel
import com.example.finances.features.transactions.ui.HistoryViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import retrofit2.Retrofit

@Module
interface TransactionsModule {
    @Binds
    @IntoMap
    @ViewModelKey(ExpensesIncomeViewModel::class)
    fun bindsExpensesIncomeViewModel(
        expensesIncomeViewModel: ExpensesIncomeViewModel
    ): BaseViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HistoryViewModel::class)
    fun bindsHistoryViewModel(historyViewModel: HistoryViewModel): BaseViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateUpdateViewModel::class)
    fun bindsCreateUpdateViewModel(createUpdateViewModel: CreateUpdateViewModel): BaseViewModel

    @Binds
    @ActivityScope
    fun bindsTransactionsRepo(transactionsRepoImpl: TransactionsRepoImpl): TransactionsRepo

    companion object {
        @Provides
        @ActivityScope
        fun providesTransactionsApi(retrofit: Retrofit): TransactionsApi {
            return retrofit.create(TransactionsApi::class.java)
        }
    }
}
