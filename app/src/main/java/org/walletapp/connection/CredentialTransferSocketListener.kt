package org.walletapp.connection

import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import org.walletapp.viewmodels.CredentialViewModel

/**
 * A WebSocketListener used to receive a Verifiable Credential from the server
 *
 * @property credentialViewModel the ViewModel for the Credential Management screen
 */
class CredentialTransferSocketListener(val credentialViewModel: CredentialViewModel) : WebSocketListener() {

    /**
     * Upon opening, send a READY message to the server to communicate
     * that we are ready to receive the Verifiable Credential
     *
     * @param webSocket the websocket that was opened
     * @param response the response from the server indicating a successful connection
     */
    override fun onOpen(webSocket: WebSocket, response: Response) {
        val message = JSONObject().apply {
            put("type", "READY")
        }.toString()
        webSocket.send(message)
    }

    /**
     * Upon receiving a message, check if it is a VC message.
     * If so, extract the Verifiable credential, close the websocket,
     * and pass it to the viewModel via its callback function
     *
     * @param webSocket the websocket that received the message
     * @param text the text of the message
     */
    override fun onMessage(webSocket: WebSocket, text: String) {
        val messageJson = JSONObject(text)
        when (messageJson.getString("type")) {
            "VC" -> {
                val vc = messageJson.getString("vc")
                webSocket.close(1000, null)
                credentialViewModel.credentialReceived(vc)
            }
        }
    }

}