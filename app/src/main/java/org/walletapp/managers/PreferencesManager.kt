package org.walletapp.managers

import android.content.Context
import android.content.SharedPreferences
import org.walletapp.exceptions.NoDIDDocumentException
import org.walletapp.exceptions.NoDIDException
import org.walletapp.exceptions.NoVerifiableCredentialException
import org.walletapp.exceptions.ValueNotFoundException

object PreferencesManager {

    private const val PREFERENCES_FILENAME = "wallet-app-preferences"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.applicationContext.getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE)
    }

    fun getValue(key: String): String = prefs.getString(key, null) ?: throw ValueNotFoundException("Not found")

    fun setValue(key: String, value: String) = prefs.edit().putString(key, value).apply()

    fun deleteValue(key: String) {
        prefs.edit().remove(key).apply()
    }

    object Keys {
        const val DID = "did"
        const val DID_DOCUMENT = "didDocument"
        const val VERIFIABLE_CREDENTIAL = "vc"
    }
}