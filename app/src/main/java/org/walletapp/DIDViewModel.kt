package org.walletapp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.walletapp.did.DIDManager

class DIDViewModel : ViewModel() {

    private var _did = androidx.compose.runtime.mutableStateOf("No DID")
    val did = _did

    private var _domain = androidx.compose.runtime.mutableStateOf("")
    val domain = _domain

    fun createDid() {
        viewModelScope.launch(Dispatchers.IO) {
            _did.value = DIDManager.createDid(_domain.value)
        }
    }

    fun copyDID(context: Context) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        val clip = ClipData.newPlainText("DID", _did.value)

        clipboard.setPrimaryClip(clip)
    }

    fun domainChanged(newDomain: String) {
        _domain.value = newDomain
    }

}