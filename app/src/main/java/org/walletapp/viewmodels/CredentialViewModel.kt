package org.walletapp.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.walletapp.connection.ConnectionManager
import org.walletapp.credential.CredentialManager
import org.walletapp.data.VerifiableCredentialInvitation
import org.walletapp.preferences.PreferencesManager

class CredentialViewModel : ViewModel() {

    private val _vc = mutableStateOf(PreferencesManager.getValue("vc") ?: "No VC")
    val vc = _vc

    fun createVc() {
        viewModelScope.launch(Dispatchers.IO) {
            val userDid = PreferencesManager.getValue("did") ?: "No DID"

            _vc.value = CredentialManager.createVerifiableCredentialJwt(userDid)
            PreferencesManager.setValue("vc", _vc.value)
            println(_vc.value)
        }
    }

    fun deleteVc() {
        viewModelScope.launch(Dispatchers.IO) {
            _vc.value = "No VC"
            PreferencesManager.setValue("vc", _vc.value)
        }
    }

    fun extractVcInvitation(scannedValue: String): VerifiableCredentialInvitation {
        val invitation = JSONObject(scannedValue)

        val issuer = invitation.getString("issuer")
        val type = invitation.getString("type")
        val webSocketsUrl = invitation.getString("webSocketsUrl")

        return VerifiableCredentialInvitation(issuer, type, webSocketsUrl)
    }

    fun initiateCredentialTransfer(invitation: VerifiableCredentialInvitation) {
        viewModelScope.launch(Dispatchers.IO) {
            ConnectionManager.initiateCredentialTransfer(invitation, this@CredentialViewModel)
        }
    }

    fun credentialReceived(vc: String) {
        _vc.value = vc
        PreferencesManager.setValue("vc", _vc.value)
    }

}