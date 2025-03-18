package org.walletapp.data

/**
 * A simple data class to encapsulate
 * the parameters of a Verifiable Presentation Request
 *
 * @property nonce the nonce to include in Verifiable Presentation
 * @property domain the domain of the resource requested in the Verifiable Presentation
 * @property appName the name of the application requesting the Verifiable Presentation
 * @property webSocketsUrl the URL of the WebSockets server
 */
data class VerifiablePresentationRequest(
    val nonce: String,
    val domain: String,
    val appName: String,
    val webSocketsUrl: String
)