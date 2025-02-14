package org.walletapp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.walletapp.did.DIDManager
import org.walletapp.preferences.PreferencesManager

class DIDViewModel : ViewModel() {

    private var _didDocument = androidx.compose.runtime.mutableStateOf(PreferencesManager.getValue("didDocument") ?: "No DID Document")
    val didDocument = _didDocument

    private var _domain = androidx.compose.runtime.mutableStateOf("")
    val domain = _domain

    fun createDid() {
        viewModelScope.launch(Dispatchers.IO) {
            _didDocument.value = DIDManager.createDid(_domain.value)
            PreferencesManager.setValue("didDocument", _didDocument.value)
        }
    }

    fun copyDID(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            val clip = ClipData.newPlainText("DID", _didDocument.value)

            clipboard.setPrimaryClip(clip)
        }
    }

    fun domainChanged(newDomain: String) {
        _domain.value = newDomain
    }

}