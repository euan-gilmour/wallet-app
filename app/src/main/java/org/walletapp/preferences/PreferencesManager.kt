package org.walletapp.preferences

import android.content.Context
import android.content.SharedPreferences

object PreferencesManager {

    private const val PREFERENCES_FILENAME = "wallet-app-preferences"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.applicationContext.getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE)
    }

    fun getValue(key: String): String? = prefs.getString(key, null)

    fun setValue(key: String, value: String) = prefs.edit().putString(key, value).apply()

    fun exists(key: String): Boolean = prefs.contains(key)

}