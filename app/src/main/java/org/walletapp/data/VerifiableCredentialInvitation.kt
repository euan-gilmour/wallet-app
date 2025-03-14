package org.walletapp.data

/**
 * A simple data class to encapsulate
 * the parameters of a Verifiable Credential Invitation
 */
data class VerifiableCredentialInvitation(
    val issuer: String,
    val recipient: String,
    val type: String,
    val webSocketsUrl: String
)