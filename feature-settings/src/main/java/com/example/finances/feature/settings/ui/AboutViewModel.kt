package com.example.finances.feature.settings.ui

import androidx.lifecycle.viewmodel.CreationExtras
import com.example.finances.core.utils.viewmodel.BaseViewModel
import com.example.finances.feature.settings.domain.repository.SettingsRepo
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class AboutViewModel @Inject constructor(settingsRepo: SettingsRepo) : BaseViewModel() {
    val appInfo = settingsRepo.loadAppInfo()

    override suspend fun loadData(scope: CoroutineScope) = resetLoadingAndError()

    override fun setViewModelParams(extras: CreationExtras) {}
}
