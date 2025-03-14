package org.walletapp.viewmodels

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.walletapp.exceptions.ValueNotFoundException
import org.walletapp.managers.DIDManager
import org.walletapp.managers.PreferencesManager

class DIDViewModel : ViewModel() {

    private var _did = mutableStateOf(try { PreferencesManager.getValue(PreferencesManager.Keys.DID) } catch (e: ValueNotFoundException) { "No DID" })
    val did = _did

    private var _didDocument = mutableStateOf(try { PreferencesManager.getValue(PreferencesManager.Keys.DID_DOCUMENT) } catch (e: ValueNotFoundException) { "No DID Document" })
    val didDocument = _didDocument

    private var _domain = mutableStateOf("")
    val domain = _domain

    fun createDid() {
        try {
            _didDocument.value = DIDManager.createDidDocument(_domain.value)
        } catch (e: Exception) {
            throw e
        }
        PreferencesManager.setValue("didDocument", _didDocument.value)
        _did.value = "did:web:${_domain.value}"
        PreferencesManager.setValue(PreferencesManager.Keys.DID, _did.value)
    }

    fun copyDID(context: Context) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("DID", _didDocument.value)
        clipboard.setPrimaryClip(clip)
    }

    fun deleteDid() {
        _did.value = "No DID"
        _didDocument.value = "No DID Document"
        PreferencesManager.deleteValue(PreferencesManager.Keys.DID)
        PreferencesManager.deleteValue(PreferencesManager.Keys.DID_DOCUMENT)
        PreferencesManager.deleteValue(PreferencesManager.Keys.VERIFIABLE_CREDENTIAL)
    }

    fun domainChanged(newDomain: String) {
        _domain.value = newDomain
    }

}