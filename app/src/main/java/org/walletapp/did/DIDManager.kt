package org.walletapp.did

import org.json.JSONArray
import org.json.JSONObject
import org.walletapp.crypto.KeyManager
import java.util.Base64
import java.security.interfaces.ECPublicKey

object DIDManager {

    var did: String = ""

    fun createDid(domain: String): String {
        did = "did:web:$domain"

        val publicKey = KeyManager.getPublicKey() as ECPublicKey

        val jwk = createJwk(publicKey)

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

        println(didDocument.toString(4).replace("\\/", "/"))
        return didDocument.toString(4).replace("\\/", "/")
    }

    private fun createJwk(publicKey: ECPublicKey): JSONObject {
        val x = publicKey.w.affineX.toByteArray()
        val y = publicKey.w.affineY.toByteArray()

        val xEncoded = Base64.getUrlEncoder().withoutPadding().encodeToString(x)
        val yEncoded = Base64.getUrlEncoder().withoutPadding().encodeToString(y)

        return JSONObject().apply {
            put("kty", "EC")
            put("crv", "P-256")
            put("x", xEncoded)
            put("y", yEncoded)
        }
    }
}
