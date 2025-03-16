package org.walletapp.managers

import android.content.Context
import android.content.SharedPreferences
import org.walletapp.exceptions.ValueNotFoundException

/**
 * A singleton object that provides a helpful interface for interacting with the shared preferences of the application
 *
 * Provides methods to store and retrieve values from shared preferences
 */
object PreferencesManager {

    private const val PREFERENCES_FILENAME = "wallet-app-preferences"

    private lateinit var prefs: SharedPreferences

    /**
     * Initialize the shared preferences file.
     * To be called in the onCreate() method of the .
     */
    fun init(context: Context) {
        prefs = context.applicationContext.getSharedPreferences(
            PREFERENCES_FILENAME,
            Context.MODE_PRIVATE
        )
    }

    /**
     * Get a value from the shared preferences
     *
     * @param key The key of the entry to get the value of
     * @return the value of the entry
     * @throws ValueNotFoundException if the value is not found
     */
    fun getValue(key: String): String =
        prefs.getString(key, null) ?: throw ValueNotFoundException("Value was not found")

    /**
     * Set a value in the shared preferences
     *
     * @param key The key of the entry to set the value of
     * @param value The value to set
     */
    fun setValue(key: String, value: String) = prefs.edit().putString(key, value).apply()

    /**
     * Delete an entry from the shared preferences
     *
     * @param key The key of the entry to delete
     */
    fun deleteValue(key: String) {
        prefs.edit().remove(key).apply()
    }

    /**
     * Provides constants for keys used to
     * retrieve and set values in the shared preferences
     */
    object Keys {
        const val DID = "did"
        const val DID_DOCUMENT = "didDocument"
        const val VERIFIABLE_CREDENTIAL = "vc"
    }
}