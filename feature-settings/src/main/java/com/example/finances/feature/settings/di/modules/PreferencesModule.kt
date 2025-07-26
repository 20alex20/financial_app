package com.example.finances.feature.settings.di.modules

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.finances.core.di.ActivityContext
import com.example.finances.feature.settings.di.EncryptedPreferences
import com.example.finances.feature.settings.di.Preferences
import com.example.finances.feature.settings.di.SettingsScope
import com.example.finances.feature.settings.domain.models.AppInfo
import dagger.Module
import dagger.Provides
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Module
class PreferencesModule {
    @Provides
    @SettingsScope
    @Preferences
    fun providessSharedPreferences(@ActivityContext context: Context): SharedPreferences {
        return context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    }

    @Provides
    @SettingsScope
    @EncryptedPreferences
    fun providesEncryptedSharedPreferences(@ActivityContext context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        return EncryptedSharedPreferences.create(
            context,
            "encryptedPreferences",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @Provides
    @SettingsScope
    fun providesAppInfo(@ActivityContext context: Context, appVersion: String): AppInfo {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val lastUpdateTime = packageInfo.lastUpdateTime
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return AppInfo(appVersion, dateFormat.format(Date(lastUpdateTime)))
    }
}
