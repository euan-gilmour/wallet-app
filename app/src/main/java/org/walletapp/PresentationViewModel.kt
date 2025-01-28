package org.walletapp

import android.content.Context
import org.json.JSONObject
import org.walletapp.connection.ConnectionManager
import org.walletapp.credential.CredentialManager

class PresentationViewModel {

    fun initiatePresentationProcess(context: Context, scannedValue: String) {
        val vpRequestJson = JSONObject(scannedValue)

        val nonce = vpRequestJson.getString("nonce")
        val domain = vpRequestJson.getString("domain")
        val appName = vpRequestJson.getString("application")

        val vp = CredentialManager.createVerifiablePresentationJwt(nonce, domain, appName)
        println(vp)

        ConnectionManager.presentVp(context, vp, vpRequestJson.getString("signallingChannelUrl"))

    }

}