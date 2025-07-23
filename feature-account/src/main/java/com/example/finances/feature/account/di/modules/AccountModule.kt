package com.example.finances.feature.account.di.modules

import com.example.finances.core.utils.viewmodel.BaseViewModel
import com.example.finances.core.utils.viewmodel.ViewModelKey
import com.example.finances.feature.account.data.AccountRepoImpl
import com.example.finances.feature.account.data.database.AccountApi
import com.example.finances.feature.account.di.AccountScope
import com.example.finances.feature.account.domain.repository.AccountRepo
import com.example.finances.feature.account.domain.repository.ExternalAccountRepo
import com.example.finances.feature.account.ui.AccountViewModel
import com.example.finances.feature.account.ui.EditAccountViewModel
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
    @AccountScope
    fun bindsAccountRepo(accountRepoImpl: AccountRepoImpl): AccountRepo

    @Binds
    @AccountScope
    fun bindsExternalAccountRepo(accountRepoImpl: AccountRepoImpl): ExternalAccountRepo

    companion object {
        @Provides
        @AccountScope
        fun providesAccountApi(retrofit: Retrofit): AccountApi {
            return retrofit.create(AccountApi::class.java)
        }
    }
}
