package org.walletapp.connection

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request
import org.walletapp.CredentialViewModel
import java.util.concurrent.TimeUnit

object ConnectionManager {

    fun sendVp(context: Context, vp: String, signallingChannelUrl: String) {
        val client = OkHttpClient.Builder()
            .readTimeout(0,  TimeUnit.MILLISECONDS)
            .build();

        val request = Request.Builder()
            .url(signallingChannelUrl)
            .build();

        client.newWebSocket(request, SendPresentationSocketListener(vp))

    }

    fun initiateCredentialTransfer(webSocketsUrl: String, credentialViewModel: CredentialViewModel) {
        val client = OkHttpClient.Builder()
            .readTimeout(0,  TimeUnit.MILLISECONDS)
            .build();

        val request = Request.Builder()
            .url(webSocketsUrl)
            .build();

        client.newWebSocket(request, CredentialTransferSocketListener(credentialViewModel))
    }


}