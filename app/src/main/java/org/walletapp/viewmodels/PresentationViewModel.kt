package org.walletapp.viewmodels

import org.json.JSONException
import org.json.JSONObject
import org.walletapp.managers.ConnectionManager
import org.walletapp.managers.CredentialManager
import org.walletapp.data.VerifiablePresentationRequest
import org.walletapp.exceptions.InvalidVerifiablePresentationRequestException
import org.walletapp.exceptions.NoVerifiableCredentialException
import org.walletapp.exceptions.ValueNotFoundException
import org.walletapp.managers.PreferencesManager
import org.walletapp.ui.components.PresentationTab

class PresentationViewModel {

    fun extractVpRequest(scannedValue: String): VerifiablePresentationRequest {
        try {
            val vpRequestJson = JSONObject(scannedValue)

            val nonce = vpRequestJson.getString("nonce")
            val domain = vpRequestJson.getString("domain")
            val appName = vpRequestJson.getString("application")
            val signallingChannelUrl = vpRequestJson.getString("signallingChannelUrl")

            return VerifiablePresentationRequest(nonce, domain, appName, signallingChannelUrl)
        } catch (e: JSONException) {
            throw InvalidVerifiablePresentationRequestException("The scanned QR code is not a valid Verifiable Presentation request")
        }
    }

    fun createAndSendVp(request: VerifiablePresentationRequest) {
        val vc = try {
            PreferencesManager.getValue(PreferencesManager.Keys.VERIFIABLE_CREDENTIAL)
        } catch (e: ValueNotFoundException) {
            throw NoVerifiableCredentialException("You do not possess a verifiable credential")
        }
        val vp = CredentialManager.createVerifiablePresentationJwt(request, vc)

        ConnectionManager.sendVp(vp, request.signallingChannelUrl)
    }

}