package org.walletapp.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.json.JSONException
import org.json.JSONObject
import org.walletapp.managers.ConnectionManager
import org.walletapp.data.VerifiableCredentialInvitation
import org.walletapp.exceptions.InvalidVerifiableCredentialInvitationException
import org.walletapp.exceptions.ValueNotFoundException
import org.walletapp.managers.PreferencesManager

class CredentialViewModel : ViewModel() {

    private val _vc = mutableStateOf(try { PreferencesManager.getValue(PreferencesManager.Keys.VERIFIABLE_CREDENTIAL) } catch (e: ValueNotFoundException) { "No VC" })
    val vc = _vc

    fun deleteVc() {
        _vc.value = "No VC"
        PreferencesManager.deleteValue("vc")
    }

    fun extractVcInvitation(scannedValue: String): VerifiableCredentialInvitation {
        try {
            val invitation = JSONObject(scannedValue)

            val issuer = invitation.getString("issuer")
            val recipient = invitation.getString("recipient")
            val type = invitation.getString("type")
            val webSocketsUrl = invitation.getString("webSocketsUrl")

            return VerifiableCredentialInvitation(issuer, recipient, type, webSocketsUrl)
        } catch (e: JSONException) {
            throw InvalidVerifiableCredentialInvitationException("The scanned QR code is not a valid Verifiable Credential invitation")
        }
    }

    fun initiateCredentialTransfer(invitation: VerifiableCredentialInvitation) {
        ConnectionManager.initiateCredentialTransfer(invitation, this)
    }

    fun credentialReceived(vc: String) {
        _vc.value = vc
        PreferencesManager.setValue("vc", _vc.value)
    }

}