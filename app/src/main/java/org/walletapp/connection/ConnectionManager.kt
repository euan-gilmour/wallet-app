package org.walletapp.connection

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request
import org.walletapp.data.VerifiableCredentialInvitation
import org.walletapp.viewmodels.CredentialViewModel
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

    fun initiateCredentialTransfer(invitation: VerifiableCredentialInvitation, credentialViewModel: CredentialViewModel) {
        val client = OkHttpClient.Builder()
            .readTimeout(0,  TimeUnit.MILLISECONDS)
            .build();

        val request = Request.Builder()
            .url(invitation.webSocketsUrl)
            .build();

        client.newWebSocket(request, CredentialTransferSocketListener(credentialViewModel))
    }


}