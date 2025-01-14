package org.walletapp.credential

import android.util.Base64
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.json.JSONArray
import org.json.JSONObject
import org.walletapp.crypto.KeyManager
import java.security.Security
import java.security.Signature
import java.util.*

object CredentialManager {

    private const val ISSUER_DID = "did:web:raw.githubusercontent.com:euan-gilmour:dids:main:issuer"
    private const val ISSUER_PRIVATE_KEY = "3ed49e3382e31b3b952e4d87a41046b01f55b622c3d4fe6a991b47cd37e048ea"
    private const val SAMPLE_CREDENTIAL_JWT_STRING = "eyJhbGciOiJFUzI1NksiLCJ0eXAiOiJKV1QifQ.eyJleHAiOjIwNTE5NDQxMzEsInZjIjp7IkBjb250ZXh0IjpbImh0dHBzOi8vd3d3LnczLm9yZy8yMDE4L2NyZWRlbnRpYWxzL3YxIiwiaHR0cHM6Ly93d3cudzMub3JnLzIwMTgvY3JlZGVudGlhbHMvZXhhbXBsZXMvdjEiXSwidHlwZSI6WyJWZXJpZmlhYmxlQ3JlZGVudGlhbCIsIlVuaXZlcnNpdHlEZWdyZWVDcmVkZW50aWFsIl0sImNyZWRlbnRpYWxTdWJqZWN0Ijp7ImRlZ3JlZSI6eyJ0eXBlIjoiQmFjaGVsb3JEZWdyZWUiLCJuYW1lIjoiQmFjaGVsb3Igb2YgU2NpZW5jZSJ9fX0sInN1YiI6ImRpZDp3ZWI6cmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbTpldWFuLWdpbG1vdXI6ZGlkczptYWluOnVzZXIiLCJuYmYiOjE3MzYzNzQ4NzEsImlzcyI6ImRpZDp3ZWI6cmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbTpldWFuLWdpbG1vdXI6ZGlkczptYWluOmlzc3VlciJ9.FTY_FWld59ajhyLqsgGhkXbDKDDmiJSutaKBXWBekrpP739Lwx0m_rPYpeNzTJrB5lwyjYOIVQ8xKibkWUolAw"

    fun createVerifiablePresentationJwt(nonce: String, domain: String, appName: String): String {
        val headerJson = JSONObject().apply {
            put("alg", "ES256")
            put("typ", "JWT")
            put("kid", "did:web:raw.githubusercontent.com:euan-gilmour:dids:main:user#keys-1")
        }
        val headerBase64 = base64UrlEncode(headerJson.toString().toByteArray())

        val now = System.currentTimeMillis()
        val exp = (now / 1000) + 5 * 60

        val vpJson = JSONObject().apply {
            put("@context", JSONArray().put("https://www.w3.org/2018/credentials/v1"))
            put("type", JSONArray().put("VerifiablePresentation"))
            put("verifiableCredential", JSONArray().put(SAMPLE_CREDENTIAL_JWT_STRING))
        }

        val payloadJson = JSONObject().apply {
            put("vp", vpJson)
            put("exp", exp)
            put("nonce", nonce)
            put("domain", domain)
            put("appName", appName)
            put("iss", ISSUER_DID)
        }
        val payloadBase64 = base64UrlEncode(payloadJson.toString().toByteArray())

        val signingInput = "$headerBase64.$payloadBase64"

        val privateKey = KeyManager.getPrivateKey()
        val signature = signWithKeyStore(privateKey, signingInput.toByteArray())

        val signatureBase64 = base64UrlEncode(signature)
        return "$signingInput.$signatureBase64"
    }

    private fun signWithKeyStore(privateKey: java.security.PrivateKey, data: ByteArray): ByteArray {
        val signature = Signature.getInstance("SHA256withECDSA")
        signature.initSign(privateKey)
        signature.update(data)
        return signature.sign()
    }

    private fun base64UrlEncode(data: ByteArray): String {
        return Base64.encodeToString(data, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
    }

}