package org.walletapp.connection

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory
import java.util.concurrent.TimeUnit

object ConnectionManager {

    private val ICE_SERVERS = listOf<PeerConnection.IceServer>(PeerConnection.IceServer("stun:stun.l.google.com:19302"), PeerConnection.IceServer("stun:stun1.l.google.com:19302"))

    fun presentVp(context: Context, vp: String, signallingChannelUrl: String) {
        PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions.builder(context).createInitializationOptions())
        val peerConnection = PeerConnectionFactory.builder().createPeerConnectionFactory().createPeerConnection(ICE_SERVERS, SendPresentationObserver())

        val client = OkHttpClient.Builder()
            .readTimeout(0,  TimeUnit.MILLISECONDS)
            .build();

        val request = Request.Builder()
            .url(signallingChannelUrl)
            .build();

        peerConnection?.let {
            client.newWebSocket(request, SendPresentationSignallerListener(it))
        } ?: println("Error: peerConnection is null")

    }


}