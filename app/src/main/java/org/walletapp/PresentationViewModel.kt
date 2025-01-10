package org.walletapp

import org.json.JSONObject
import org.walletapp.credential.CredentialManager

class PresentationViewModel {

    fun initiatePresentationProcess(scannedValue: String) {
        val vpRequestJson = JSONObject(scannedValue)

        val nonce = vpRequestJson.getString("nonce")
        val domain = vpRequestJson.getString("domain")
        val appName = vpRequestJson.getString("application")

        val vp = CredentialManager.createVerifiablePresentationJwt(nonce, domain, appName)
        println(vp)
    }

}