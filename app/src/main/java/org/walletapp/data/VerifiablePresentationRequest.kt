package org.walletapp.data

/**
 * A simple data class to encapsulate
 * the parameters of a Verifiable Presentation Request
 */
data class VerifiablePresentationRequest(
    val nonce: String,
    val domain: String,
    val appName: String,
    val signallingChannelUrl: String
)