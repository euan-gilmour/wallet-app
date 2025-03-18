package org.walletapp.managers

import io.jsonwebtoken.Jwts
import java.util.*
import org.walletapp.data.VerifiablePresentationRequest
import org.walletapp.exceptions.NoDIDException
import org.walletapp.exceptions.ValueNotFoundException

/**
 * A singleton object for managing functionality around Verifiable Presentations
 *
 * Provides the functionality to create a Verifiable Presentation
 */
object PresentationManager {

    /**
     * Creates a Verifiable Presentation in JSON Web Token format
     *
     * @param request the Verifiable Presentation Request
     * @param vc the Verifiable Credential
     * @return a Verifiable Presentation in JSON Web Token format
     * @throws NoDIDException if no DID has been created
     */
    fun createVerifiablePresentationJwt(
        request: VerifiablePresentationRequest,
        vc: String
    ): String {

        // Retrieve the private key from the KeyManager
        val privateKey = KeyManager.getPrivateKey()

        // Retrieve the user's DID
        var did: String
        try {
            did = PreferencesManager.getValue(PreferencesManager.Keys.DID)
        } catch (_: ValueNotFoundException) {
            throw NoDIDException("You have not set up a DID")
        }

        // Create the JSON Web Token for the VP
        return Jwts.builder()
            .header()
            .add(
                mapOf(
                    "alg" to "ES256",
                    "typ" to "JWT",
                )
            )
            .and()
            .claims()
            .add(
                "vp", mapOf(
                    "@context" to listOf("https://www.w3.org/2018/credentials/v1"),
                    "type" to listOf("VerifiablePresentation"),
                    "verifiableCredential" to listOf(vc)
                )
            )
            .expiration(Date(System.currentTimeMillis() + 5 * 1000 * 60)) // Expires in 5 minutes
            .add("nonce", request.nonce)
            .add("domain", request.domain)
            .add("appName", request.appName)
            .issuer(did)
            .and()
            .signWith(privateKey)
            .compact()
    }

}
