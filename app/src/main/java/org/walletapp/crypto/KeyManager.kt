package org.walletapp.crypto

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyInfo
import android.security.keystore.KeyProperties
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.ECGenParameterSpec


object KeyManager {

    private const val KEYPAIR_ALIAS = "SolidWalletDIDKeys"

    private val keyStore by lazy {
        KeyStore.getInstance("AndroidKeyStore").apply {
            load(null)
        }
    }

    fun generateKeys() {
        val keyPairGenerator = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_EC,
            "AndroidKeyStore"
        )

        val parameterSpec = KeyGenParameterSpec.Builder(
            KEYPAIR_ALIAS,
            KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
        ).run {
            setAlgorithmParameterSpec(ECGenParameterSpec("secp256r1"))
            setDigests(KeyProperties.DIGEST_SHA256)
            setUserAuthenticationRequired(false)
            build()
        }

        keyPairGenerator.initialize(parameterSpec)
        keyPairGenerator.generateKeyPair()
    }

    fun getPublicKey(): PublicKey {
        return keyStore.getCertificate(KEYPAIR_ALIAS).publicKey
    }

    fun getPrivateKey(): PrivateKey {
        return keyStore.getKey(KEYPAIR_ALIAS, null) as PrivateKey
    }

    fun keyExists(): Boolean {
        return keyStore.aliases().toList().contains(KEYPAIR_ALIAS)
    }

    fun deleteKey() {
        keyStore.deleteEntry(KEYPAIR_ALIAS)
    }

    fun getSecurityLevel(): String {
        val privateKey = getPrivateKey()
        val keyFactory = KeyFactory.getInstance(privateKey.algorithm, "AndroidKeyStore")
        val keyInfo: KeyInfo = keyFactory.getKeySpec(privateKey, KeyInfo::class.java)
        return when (keyInfo.securityLevel) {
            KeyProperties.SECURITY_LEVEL_TRUSTED_ENVIRONMENT -> "Trusted Environment"
            KeyProperties.SECURITY_LEVEL_STRONGBOX -> "Strongbox"
            KeyProperties.SECURITY_LEVEL_SOFTWARE -> "Software"
            else -> "Unknown or Other"
        }
    }

}