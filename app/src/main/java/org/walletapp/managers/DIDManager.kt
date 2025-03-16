package org.walletapp.managers

import io.jsonwebtoken.security.Jwks
import org.json.JSONArray
import org.json.JSONObject
import org.walletapp.exceptions.NoKeyException
import java.security.interfaces.ECPublicKey

/**
 * A singleton object for managing functionality around DID document creation
 *
 * Provides the functionality to create a DID document using a given DID and a previously generated
 * public key from KeyManager
 */
object DIDManager {

    /**
     * Create a valid DID document for a given DID
     *
     * @param did the DID for which to create the document
     * @return a valid DID document as a string representation of a JSON object
     * @throws NoKeyException if no keypair has been generated
     */
    fun createDidDocument(did: String): String {

        // Verify that a key exists
        if (!KeyManager.keyExists()) {
            throw NoKeyException("You have not generated a key")
        }

        // Retrieve the public key as an ECPublicKey
        val publicKey = KeyManager.getPublicKey() as ECPublicKey

        // Format the public key as a JSON Web Key
        val jwk = formatKeyAsJwk(publicKey)

        // Construct the DID document as a JSON object
        val didDocument = JSONObject().apply {
            put("@context", JSONArray().apply {
                put("https://www.w3.org/ns/did/v1")
                put("https://w3id.org/security/suites/jws-2020/v1")
            })
            put("id", did)

            put("verificationMethod", JSONArray().apply {
                put(JSONObject().apply {
                    put("id", "$did#keys-1")
                    put("type", "JsonWebKey2020")
                    put("controller", did)
                    put("publicKeyJwk", jwk)
                })
            })

            put("authentication", JSONArray().put("$did#keys-1"))
            put("assertionMethod", JSONArray().put("$did#keys-1"))
        }

        // Return the document as a string
        return didDocument.toString(4).replace("\\/", "/")
    }

    /**
     * Format a PublicKey object as a string representation of a JSON Web Key
     *
     * @param publicKey the public key to format
     * @return a string representation of a JSON Web Key
     */
    private fun formatKeyAsJwk(publicKey: ECPublicKey): JSONObject {
        return JSONObject(Jwks.builder().key(publicKey).build().toString())
    }
}
