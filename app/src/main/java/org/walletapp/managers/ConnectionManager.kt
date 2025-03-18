package org.walletapp.managers

import okhttp3.OkHttpClient
import okhttp3.Request
import org.walletapp.connection.CredentialTransferSocketListener
import org.walletapp.connection.SendPresentationSocketListener
import org.walletapp.data.VerifiableCredentialInvitation
import org.walletapp.data.VerifiablePresentationRequest
import org.walletapp.viewmodels.CredentialViewModel
import java.util.concurrent.TimeUnit

/**
 * A singleton object for managing the transmission of Verifiable Credentials and Presentations
 * via websockets
 *
 * Provides functionality for sending a Verifiable Presentation and initiating transfer of a
 * Verifiable Credential
 */
object ConnectionManager {

    /**
     * Sets up a websocket to send a Verifiable Presentation
     *
     * @param vp the Verifiable Presentation to send
     * @param vpRequest the URL of the websockets server to send the VP to
     */
    fun sendVp(vp: String, vpRequest: VerifiablePresentationRequest) {
        // Set up an HTTP client
        val client = OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .build()

        // Build a request to the websockets server
        val request = Request.Builder()
            .url(vpRequest.webSocketsUrl)
            .build()

        // Set up a new websocket with a SendPresentationSocketListener
        client.newWebSocket(request, SendPresentationSocketListener(vp))
    }

    /**
     * Sets up a websocket to handle the transfer of a Verifiable Credential
     *
     * @param invitation the Verifiable Credential invitation
     * @param credentialViewModel the ViewModel for the Credential Management screen
     */
    fun initiateCredentialTransfer(
        invitation: VerifiableCredentialInvitation,
        credentialViewModel: CredentialViewModel
    ) {
        // Set up an HTTP client
        val client = OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .build()

        // Build a request to the websockets server
        val request = Request.Builder()
            .url(invitation.webSocketsUrl)
            .build()

        // Set up a new websocket with a CredentialTransferSocketListener
        client.newWebSocket(request, CredentialTransferSocketListener(credentialViewModel))
    }

}