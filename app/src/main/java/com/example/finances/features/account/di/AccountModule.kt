package com.example.finances.features.account.di

import androidx.lifecycle.ViewModel
import com.example.finances.core.di.ActivityScope
import com.example.finances.core.utils.viewmodel.ViewModelKey
import com.example.finances.features.account.data.AccountApi
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
    fun bindsAccountViewModel(accountViewModel: AccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditAccountViewModel::class)
    fun bindsEditAccountViewModel(editAccountViewModel: EditAccountViewModel): ViewModel

    @Binds
    @ActivityScope
    fun bindsAccountRepoImpl(accountRepoImpl: AccountRepoImpl): AccountRepo

    @Binds
    @ActivityScope
    fun bindsExternalAccountRepoImpl(accountRepoImpl: AccountRepoImpl): ExternalAccountRepo

    companion object {
        @Provides
        @ActivityScope
        fun providesAccountApi(retrofit: Retrofit): AccountApi {
            return retrofit.create(AccountApi::class.java)
        }
    }
}
