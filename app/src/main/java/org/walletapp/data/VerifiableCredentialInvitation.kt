package org.walletapp.data

/**
 * A simple data class to encapsulate
 * the parameters of a Verifiable Credential Invitation
 *
 * @property issuer the issuer of the Verifiable Credential
 * @property recipient the recipient of the Verifiable Credential
 * @property type the type of the Verifiable Credential
 * @property webSocketsUrl the URL of the WebSockets server
 */
data class VerifiableCredentialInvitation(
    val issuer: String,
    val recipient: String,
    val type: String,
    val webSocketsUrl: String
)