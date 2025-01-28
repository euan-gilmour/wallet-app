package org.walletapp.connection

import okhttp3.WebSocket
import org.webrtc.SdpObserver
import org.webrtc.SessionDescription

class SendPresentationSdpObserver(private val webSocket: WebSocket) : SdpObserver {

    override fun onCreateSuccess(sdp: SessionDescription) {
        webSocket.send(sdp.description)
    }

    override fun onSetSuccess() {
        println("SET SUCCESS")
    }

    override fun onCreateFailure(error: String?) {
        println("CREATE FAILURE: $error")
    }

    override fun onSetFailure(error: String?) {
        println("SET FAILURE: $error")
    }

}