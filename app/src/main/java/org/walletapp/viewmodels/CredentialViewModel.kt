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

/**
 * A ViewModel for the Credential tab
 *
 * This ViewModel manages the stored Verifiable Credential and functionality for
 * extracting a Verifiable Credential invitation from a scanned QR code value,
 * initiating a credential transfer, updating upon reception of a Verifiable Credential,
 * and deleting the stored Verifiable Credential
 */
class CredentialViewModel : ViewModel() {

    private val _vc = mutableStateOf(try { PreferencesManager.getValue(PreferencesManager.Keys.VERIFIABLE_CREDENTIAL) } catch (_: ValueNotFoundException) { "No VC" })
    val vc = _vc

    /**
     * Delete the stored Verifiable Credential
     */
    fun deleteVc() {
        _vc.value = "No VC"
        PreferencesManager.deleteValue("vc")
    }

    /**
     * Extract a Verifiable Credential invitation from a scanned QR code value
     *
     * @param scannedValue The scanned value from the QR code
     * @throws InvalidVerifiableCredentialInvitationException if the scanned value is not a valid Verifiable Credential invitation
     */
    fun extractVcInvitation(scannedValue: String): VerifiableCredentialInvitation {
        try {
            val invitation = JSONObject(scannedValue)

            val issuer = invitation.getString("issuer")
            val recipient = invitation.getString("recipient")
            val type = invitation.getString("type")
            val webSocketsUrl = invitation.getString("webSocketsUrl")

            return VerifiableCredentialInvitation(issuer, recipient, type, webSocketsUrl)
        } catch (_: JSONException) {
            throw InvalidVerifiableCredentialInvitationException("The scanned QR code is not a valid Verifiable Credential invitation")
        }
    }

    /**
     * Initiate a Verifiable Credential transfer via the Connection Manager,
     * passing this instance of the viewModel for the use of its callback function
     *
     * @param invitation The Verifiable Credential invitation
     */
    fun initiateCredentialTransfer(invitation: VerifiableCredentialInvitation) {
        ConnectionManager.initiateCredentialTransfer(invitation, this)
    }

    /**
     * Update the UI and stored Verifiable Credential upon receipt of a Verifiable Credential
     *
     * @param vc The Verifiable Credential
     */
    fun credentialReceived(vc: String) {
        _vc.value = vc
        PreferencesManager.setValue("vc", _vc.value)
    }

}