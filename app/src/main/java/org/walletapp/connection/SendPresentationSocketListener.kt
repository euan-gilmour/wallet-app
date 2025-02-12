package org.walletapp.connection

import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject

class SendPresentationSocketListener(val vp: String) : WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        val message = createVpMessage()
        println("Message: $message")
        webSocket.send(message)
    }

    private fun createVpMessage(): String {
        return JSONObject().apply {
            put("type", "VP")
            put("vp", vp)
        }.toString()
    }

}