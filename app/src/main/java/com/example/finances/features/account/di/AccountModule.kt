package com.example.finances.features.account.di

import com.example.finances.core.di.ActivityScope
import com.example.finances.core.utils.viewmodel.BaseViewModel
import com.example.finances.core.utils.viewmodel.ViewModelKey
import com.example.finances.features.account.data.database.AccountApi
import com.example.finances.features.account.data.AccountRepoImpl
import com.example.finances.features.account.domain.repository.AccountRepo
import com.example.finances.features.account.domain.repository.ExternalAccountRepo
import com.example.finances.features.account.ui.AccountViewModel
import com.example.finances.features.account.ui.EditAccountViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import retrofit2.Retrofit

@Module
interface AccountModule {
    @Binds
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    fun bindsAccountViewModel(accountViewModel: AccountViewModel): BaseViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditAccountViewModel::class)
    fun bindsEditAccountViewModel(editAccountViewModel: EditAccountViewModel): BaseViewModel

    @Binds
    @ActivityScope
    fun bindsAccountRepo(accountRepoImpl: AccountRepoImpl): AccountRepo

    @Binds
    @ActivityScope
    fun bindsExternalAccountRepo(accountRepoImpl: AccountRepoImpl): ExternalAccountRepo

    companion object {
        @Provides
        @ActivityScope
        fun providesAccountApi(retrofit: Retrofit): AccountApi {
            return retrofit.create(AccountApi::class.java)
        }
    }
}
