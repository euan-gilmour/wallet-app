package org.walletapp.viewmodels

import androidx.lifecycle.ViewModel
import org.json.JSONException
import org.json.JSONObject
import org.walletapp.managers.ConnectionManager
import org.walletapp.managers.PresentationManager
import org.walletapp.data.VerifiablePresentationRequest
import org.walletapp.exceptions.InvalidVerifiablePresentationRequestException
import org.walletapp.exceptions.NoVerifiableCredentialException
import org.walletapp.exceptions.ValueNotFoundException
import org.walletapp.managers.PreferencesManager

/**
 * A ViewModel for the Presentation tab
 *
 * This ViewModel provides functionality for creating and sending Verifiable Presentations and
 * extracting a Verifiable Presentation Request from a scanned QR code.
 */
class PresentationViewModel() : ViewModel() {

    /**
     * Extract a Verifiable Presentation Request from a scanned QR code value
     *
     * @param scannedValue The scanned value from the QR code
     * @throws InvalidVerifiablePresentationRequestException if the scanned value is not a valid Verifiable Presentation request
     */
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

    /**
     * Create a Verifiable Presentation and initiate the transmission via the ConnectionManager
     *
     * @param request The Verifiable Presentation request
     * @throws NoVerifiableCredentialException if the user does not possess a Verifiable Credential
     */
    fun createAndSendVp(request: VerifiablePresentationRequest) {
        val vc = try {
            PreferencesManager.getValue(PreferencesManager.Keys.VERIFIABLE_CREDENTIAL)
        } catch (e: ValueNotFoundException) {
            throw NoVerifiableCredentialException("You do not possess a verifiable credential")
        }
        val vp = PresentationManager.createVerifiablePresentationJwt(request, vc)

        ConnectionManager.sendVp(vp, request)
    }

}