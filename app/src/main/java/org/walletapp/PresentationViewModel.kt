package org.walletapp

import android.content.Context
import android.util.Base64
import org.json.JSONObject
import org.walletapp.connection.ConnectionManager
import org.walletapp.credential.CredentialManager

class PresentationViewModel {

    private val _vp = androidx.compose.runtime.mutableStateOf("No Presentation")
    val vp = _vp

    private val _vpHeaderPlain = androidx.compose.runtime.mutableStateOf("No Presentation")
    val vpHeaderPlain = _vpHeaderPlain

    private val _vpPayloadPlain = androidx.compose.runtime.mutableStateOf("No Presentation")
    val vpPayloadPlain = _vpPayloadPlain

    private val _verificationStatus = androidx.compose.runtime.mutableStateOf("Unverified")
    val verificationStatus = _verificationStatus

    fun initiatePresentationProcess(context: Context, scannedValue: String) {
        val vpRequestJson = JSONObject(scannedValue)

        val nonce = vpRequestJson.getString("nonce")
        val domain = vpRequestJson.getString("domain")
        val appName = vpRequestJson.getString("application")

        val vp = CredentialManager.createVerifiablePresentationJwt(nonce, domain, appName)
        println(vp)

        val signallingChannelUrl = vpRequestJson.getString("signallingChannelUrl")

        ConnectionManager.sendVp(context, vp, signallingChannelUrl)
    }

    fun generatePresentation() {
        _vp.value = CredentialManager.createVerifiablePresentationJwt("895643876", "https://example.com", "Example App")
        updateVpHeader()
        updateVpPayload()
        println(_vp.value)
    }

    private fun updateVpHeader() {
        _vpHeaderPlain.value = String(Base64.decode(_vp.value.split(".")[0], Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING),
            Charsets.UTF_8)
    }

    private fun updateVpPayload() {
        _vpPayloadPlain.value = String(Base64.decode(_vp.value.split(".")[1], Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING),
            Charsets.UTF_8)
    }

    fun verifyPresentation() {
        val vpParts = _vp.value.split(".")

        val signatureString = _vp.value.split(".")[2]
        val signatureBytes = Base64.decode(signatureString, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)

        val data = "${vpParts[0]}.${vpParts[1]}".toByteArray()

        _verificationStatus.value = if (CredentialManager.verifySignature(data, signatureBytes)) "Presentation is valid." else "Presentation is invalid."
    }

}