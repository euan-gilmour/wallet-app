package org.walletapp.connection

import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import org.walletapp.viewmodels.CredentialViewModel

class CredentialTransferSocketListener(val credentialViewModel: CredentialViewModel) : WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        val message = JSONObject().apply {
            put("type", "READY")
        }.toString()
        webSocket.send(message)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        val messageJson = JSONObject(text)
        when (messageJson.getString("type")) {
            "VC" -> {
                val vc = messageJson.getString("vc")
                credentialViewModel.credentialReceived(vc)
                webSocket.close(1000, null)
            }
        }
    }

}