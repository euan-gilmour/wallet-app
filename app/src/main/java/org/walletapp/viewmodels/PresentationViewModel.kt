package org.walletapp.viewmodels

import org.json.JSONObject
import org.walletapp.managers.ConnectionManager
import org.walletapp.managers.CredentialManager
import org.walletapp.data.VerifiablePresentationRequest
import org.walletapp.managers.PreferencesManager

class PresentationViewModel {

    fun extractVpRequest(scannedValue: String): VerifiablePresentationRequest {
        val vpRequestJson = JSONObject(scannedValue)

        val nonce = vpRequestJson.getString("nonce")
        val domain = vpRequestJson.getString("domain")
        val appName = vpRequestJson.getString("application")
        val signallingChannelUrl = vpRequestJson.getString("signallingChannelUrl")

        return VerifiablePresentationRequest(nonce, domain, appName, signallingChannelUrl)
    }

    fun createAndSendVp(request: VerifiablePresentationRequest) {
        val vc = PreferencesManager.getValue("vc") ?: "No VC"
        val vp = CredentialManager.createVerifiablePresentationJwt(request, vc)

        ConnectionManager.sendVp(vp, request.signallingChannelUrl)
    }

}