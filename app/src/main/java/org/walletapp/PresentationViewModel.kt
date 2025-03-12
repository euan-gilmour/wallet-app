package org.walletapp

import android.content.Context
import android.util.Base64
import org.json.JSONObject
import org.walletapp.connection.ConnectionManager
import org.walletapp.credential.CredentialManager
import org.walletapp.preferences.PreferencesManager

class PresentationViewModel {

    fun initiatePresentationProcess(context: Context, scannedValue: String) {
        val vpRequestJson = JSONObject(scannedValue)

        val nonce = vpRequestJson.getString("nonce")
        val domain = vpRequestJson.getString("domain")
        val appName = vpRequestJson.getString("application")

        val vc = PreferencesManager.getValue("vc") ?: "No VC"

        val vp = CredentialManager.createVerifiablePresentationJwt(nonce, domain, appName, vc)
        println(vp)

        val signallingChannelUrl = vpRequestJson.getString("signallingChannelUrl")

        ConnectionManager.sendVp(context, vp, signallingChannelUrl)
    }

}