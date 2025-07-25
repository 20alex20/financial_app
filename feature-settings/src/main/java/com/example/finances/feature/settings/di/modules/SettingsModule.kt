package com.example.finances.feature.settings.di.modules

import com.example.finances.core.utils.viewmodel.BaseViewModel
import com.example.finances.core.utils.viewmodel.ViewModelKey
import com.example.finances.feature.settings.data.SettingsRepoImpl
import com.example.finances.feature.settings.di.SettingsScope
import com.example.finances.feature.settings.domain.repository.ExternalSettingsRepo
import com.example.finances.feature.settings.domain.repository.SettingsRepo
import com.example.finances.feature.settings.ui.AboutViewModel
import com.example.finances.feature.settings.ui.PrimaryColorViewModel
import com.example.finances.feature.settings.ui.SettingsViewModel
import com.example.finances.feature.settings.ui.VibrationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface SettingsModule {
    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    fun bindsSettingsViewModel(categoriesViewModel: SettingsViewModel): BaseViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PrimaryColorViewModel::class)
    fun bindsPrimaryColorViewModel(categoriesViewModel: PrimaryColorViewModel): BaseViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VibrationViewModel::class)
    fun bindsVibrationViewModel(categoriesViewModel: VibrationViewModel): BaseViewModel

//    @Binds
//    @IntoMap
//    @ViewModelKey(UserPinViewModel::class)
//    fun bindsUserPinViewModel(categoriesViewModel: UserPinViewModel): BaseViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AboutViewModel::class)
    fun bindsAboutViewModel(categoriesViewModel: AboutViewModel): BaseViewModel

    @Binds
    @SettingsScope
    fun bindsSettingsRepo(categoriesRepoImpl: SettingsRepoImpl): SettingsRepo

    @Binds
    @SettingsScope
    fun bindsExternalSettingsRepo(categoriesRepoImpl: SettingsRepoImpl): ExternalSettingsRepo
}
