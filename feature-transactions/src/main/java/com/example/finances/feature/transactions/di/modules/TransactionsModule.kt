package com.example.finances.feature.transactions.di.modules

import com.example.finances.core.utils.viewmodel.BaseViewModel
import com.example.finances.core.utils.viewmodel.ViewModelKey
import com.example.finances.feature.transactions.data.TransactionsRepoImpl
import com.example.finances.feature.transactions.data.database.TransactionsApi
import com.example.finances.feature.transactions.di.TransactionsScope
import com.example.finances.feature.transactions.domain.repository.TransactionsRepo
import com.example.finances.feature.transactions.ui.AnalysisViewModel
import com.example.finances.feature.transactions.ui.CreateUpdateViewModel
import com.example.finances.feature.transactions.ui.ExpensesIncomeViewModel
import com.example.finances.feature.transactions.ui.HistoryViewModel
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
    @ViewModelKey(AnalysisViewModel::class)
    fun bindsAnalysisViewModel(analysisViewModel: AnalysisViewModel): BaseViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateUpdateViewModel::class)
    fun bindsCreateUpdateViewModel(createUpdateViewModel: CreateUpdateViewModel): BaseViewModel

    @Binds
    @TransactionsScope
    fun bindsTransactionsRepo(transactionsRepoImpl: TransactionsRepoImpl): TransactionsRepo

    companion object {
        @Provides
        @TransactionsScope
        fun providesTransactionsApi(retrofit: Retrofit): TransactionsApi {
            return retrofit.create(TransactionsApi::class.java)
        }
    }
}
