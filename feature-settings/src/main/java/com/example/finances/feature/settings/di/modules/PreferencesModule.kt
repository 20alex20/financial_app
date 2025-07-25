package com.example.finances.feature.settings.di.modules

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.finances.core.di.ActivityContext
import com.example.finances.feature.settings.di.EncryptedPreferences
import com.example.finances.feature.settings.di.Preferences
import com.example.finances.feature.settings.di.SettingsScope
import dagger.Module
import dagger.Provides

@Module
class PreferencesModule {
    @Provides
    @SettingsScope
    @Preferences
    fun bindsSharedPreferences(@ActivityContext context: Context): SharedPreferences {
        return context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    }

    @Provides
    @SettingsScope
    @EncryptedPreferences
    fun bindsEncryptedSharedPreferences(@ActivityContext context: Context): SharedPreferences {
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
}
