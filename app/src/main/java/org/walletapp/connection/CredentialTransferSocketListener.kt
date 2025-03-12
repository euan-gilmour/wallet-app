package org.walletapp.connection

import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import org.walletapp.viewmodels.CredentialViewModel

class CredentialTransferSocketListener(val credentialViewModel: CredentialViewModel) : WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        println("CONNECTED TO WS SERVER")
        val message = JSONObject().apply {
            put("type", "READY")
        }.toString()
        webSocket.send(message)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        println("RECEIVED WS MESSAGE: $text")
        val messageJson = JSONObject(text)
        when (messageJson.getString("type")) {
            "VC" -> {
                val vc = messageJson.getString("vc")
                println("Credential: $vc")
                credentialViewModel.credentialReceived(vc)
                webSocket.close(1000, null)
            }
        }
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        println("CONNECTION FAILED: ${t.message}")
    }

}