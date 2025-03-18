package org.walletapp.connection

import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject

/**
 * A websocket listener that sends a Verifiable Presentation to the server
 *
 * @property vp the Verifiable Presentation to send
 */
class SendPresentationSocketListener(val vp: String) : WebSocketListener() {

    /**
     * Upon opening, send a VP message to the server and close.
     *
     * @param webSocket the websocket that was opened
     * @param response the response from the server indicating a successful connection
     */
    override fun onOpen(webSocket: WebSocket, response: Response) {
        val message = createVpMessage()
        webSocket.send(message)
        webSocket.close(1000, null)
    }

    /**
     * Create a VP message as a JSON object with the verifiable presentation
     * and return it as a string
     *
     * @return the VP message object as a string
     */
    private fun createVpMessage(): String {
        return JSONObject().apply {
            put("type", "VP")
            put("vp", vp)
        }.toString()
    }

}