package org.walletapp.managers

import io.jsonwebtoken.security.Jwks
import org.json.JSONArray
import org.json.JSONObject
import java.security.interfaces.ECPublicKey

object DIDManager {

    fun createDid(domain: String): String {
        val did = "did:web:$domain"

        PreferencesManager.setValue("did", did)

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

    fun createJwk(publicKey: ECPublicKey): JSONObject {
        return JSONObject(Jwks.builder().key(publicKey).build().toString())
    }
}
