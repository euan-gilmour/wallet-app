package org.walletapp.viewmodels

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.walletapp.managers.DIDManager
import org.walletapp.managers.PreferencesManager

class DIDViewModel : ViewModel() {

    private var _didDocument = mutableStateOf(PreferencesManager.getValue("didDocument") ?: "No DID Document")
    val didDocument = _didDocument

    private var _domain = mutableStateOf("")
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