package org.walletapp.viewmodels

import android.content.Context
import org.json.JSONObject
import org.walletapp.connection.ConnectionManager
import org.walletapp.credential.CredentialManager
import org.walletapp.data.VerifiablePresentationRequest
import org.walletapp.preferences.PreferencesManager

class PresentationViewModel {

    fun extractVpRequest(scannedValue: String): VerifiablePresentationRequest {
        val vpRequestJson = JSONObject(scannedValue)

        val nonce = vpRequestJson.getString("nonce")
        val domain = vpRequestJson.getString("domain")
        val appName = vpRequestJson.getString("application")
        val signallingChannelUrl = vpRequestJson.getString("signallingChannelUrl")

        return VerifiablePresentationRequest(nonce, domain, appName, signallingChannelUrl)
    }

    fun createAndSendVp(context: Context, request: VerifiablePresentationRequest) {
        val vc = PreferencesManager.getValue("vc") ?: "No VC"
        val vp = CredentialManager.createVerifiablePresentationJwt(request.nonce, request.domain, request.appName, vc)

        ConnectionManager.sendVp(context, vp, request.signallingChannelUrl)
    }

}