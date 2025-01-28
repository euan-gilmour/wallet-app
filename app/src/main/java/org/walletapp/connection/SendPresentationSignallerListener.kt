package org.walletapp.connection

import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.webrtc.MediaConstraints
import org.webrtc.PeerConnection

class SendPresentationSignallerListener(private val peerConnection: PeerConnection) : WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        sendSdpOffer(webSocket)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        println("MESSAGE: $text")
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        println("MESSAGE: ${bytes.hex()}")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(1000, null)
        println("CLOSE: $code $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        t.printStackTrace()
    }

    fun sendSdpOffer(webSocket: WebSocket) {
        println("Sending SDP Offer")
        peerConnection.createOffer(SendPresentationSdpObserver(webSocket), MediaConstraints())
    }

}