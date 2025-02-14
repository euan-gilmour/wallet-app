package org.walletapp.credential

import android.util.Base64
import io.jsonwebtoken.Jwts
import org.json.JSONArray
import org.json.JSONObject
import org.walletapp.crypto.KeyManager
import java.security.Security
import java.security.Signature
import java.util.*
import io.jsonwebtoken.SignatureAlgorithm
import org.bouncycastle.jce.ECNamedCurveTable
import org.bouncycastle.jce.spec.ECPrivateKeySpec
import java.math.BigInteger
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.util.*

object CredentialManager {

    private const val ISSUER_DID = "did:web:raw.githubusercontent.com:euan-gilmour:dids:main:issuer"
    private const val ISSUER_PRIVATE_KEY_HEX = "3ed49e3382e31b3b952e4d87a41046b01f55b622c3d4fe6a991b47cd37e048ea"
    private const val SAMPLE_CREDENTIAL_JWT_STRING = "eyJhbGciOiJFUzI1NksiLCJ0eXAiOiJKV1QifQ.eyJleHAiOjIwNTE5NDQxMzEsInZjIjp7IkBjb250ZXh0IjpbImh0dHBzOi8vd3d3LnczLm9yZy8yMDE4L2NyZWRlbnRpYWxzL3YxIiwiaHR0cHM6Ly93d3cudzMub3JnLzIwMTgvY3JlZGVudGlhbHMvZXhhbXBsZXMvdjEiXSwidHlwZSI6WyJWZXJpZmlhYmxlQ3JlZGVudGlhbCIsIlVuaXZlcnNpdHlEZWdyZWVDcmVkZW50aWFsIl0sImNyZWRlbnRpYWxTdWJqZWN0Ijp7ImRlZ3JlZSI6eyJ0eXBlIjoiQmFjaGVsb3JEZWdyZWUiLCJuYW1lIjoiQmFjaGVsb3Igb2YgU2NpZW5jZSJ9fX0sInN1YiI6ImRpZDp3ZWI6cmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbTpldWFuLWdpbG1vdXI6ZGlkczptYWluOnVzZXIiLCJuYmYiOjE3MzYzNzQ4NzEsImlzcyI6ImRpZDp3ZWI6cmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbTpldWFuLWdpbG1vdXI6ZGlkczptYWluOmlzc3VlciJ9.FTY_FWld59ajhyLqsgGhkXbDKDDmiJSutaKBXWBekrpP739Lwx0m_rPYpeNzTJrB5lwyjYOIVQ8xKibkWUolAw"

    fun createVerifiablePresentationJwt(nonce: String, domain: String, appName: String, vc: String): String {
        val privateKey = KeyManager.getPrivateKey()

        return Jwts.builder()
            .header()
            .add(mapOf(
                "alg" to "ES256",
                "typ" to "JWT",
            ))
            .and()
            .claims()
            .add("vp", mapOf(
                "@context" to listOf("https://www.w3.org/2018/credentials/v1"),
                "type" to listOf("VerifiablePresentation"),
                "verifiableCredential" to listOf(vc)
            ))
            .expiration(Date(System.currentTimeMillis() + 5 * 1000 * 60))
            .add("nonce", nonce)
            .add("domain", domain)
            .add("appName", appName)
            .issuer("did:web:raw.githubusercontent.com:euan-gilmour:dids:main:user")
            .and()
            .signWith(privateKey)
            .compact()

    }

    fun createVerifiableCredentialJwt(userDid: String): String {
        val issuerPrivateKey = createIssuerPrivateKey()

        return Jwts.builder()
            .header()
            .add(mapOf(
                "alg"  to "ES256K",
                "typ" to "JWT"
            ))
            .and()
            .claims()
            .add("vc", mapOf(
                "@context" to listOf(
                    "https://www.w3.org/2018/credentials/v1",
                    "https://www.w3.org/2018/credentials/examples/v1"
                ),
                "type" to listOf(
                    "VerifiableCredential",
                    "UniversityDegreeCredential"
                ),
                "credentialSubject" to mapOf(
                    "degree" to mapOf(
                        "type" to "BachelorDegree",
                        "name" to "Bachelor of Science"
                    ),
            )))
            .subject(userDid)
            .notBefore(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + 10 * 1000 * 60 * 60 * 24 * 365))
            .issuer(ISSUER_DID)
            .and()
            .signWith(issuerPrivateKey)
            .compact()
    }

    private fun createIssuerPrivateKey(): PrivateKey {

        val privateKeyInt = BigInteger(ISSUER_PRIVATE_KEY_HEX, 16)

        val ecParameterSpec = ECNamedCurveTable.getParameterSpec("secp256k1")

        val privateKeySpec = ECPrivateKeySpec(privateKeyInt, ecParameterSpec)


        val keyFactory = KeyFactory.getInstance("EC", "BC")
        return keyFactory.generatePrivate(privateKeySpec)
    }

    public fun signWithKeyStore(privateKey: java.security.PrivateKey, data: ByteArray): ByteArray {
        val signature = Signature.getInstance("SHA256withECDSA")
        signature.initSign(privateKey)
        signature.update(data)
        return signature.sign()
    }

    private fun base64UrlEncode(data: ByteArray): String {
        return Base64.encodeToString(data, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
    }

    public fun verifySignature(data: ByteArray, signatureBytes: ByteArray): Boolean {
        val publicKey = KeyManager.getPublicKey()
        val signature = Signature.getInstance("SHA256withECDSA")
        signature.initVerify(publicKey)
        signature.update(data)
        return signature.verify(signatureBytes)
    }

}
