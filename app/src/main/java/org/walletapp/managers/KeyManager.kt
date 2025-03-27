package org.walletapp.managers

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyInfo
import android.security.keystore.KeyProperties
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.ECGenParameterSpec

/**
 * A singleton object that manages the cryptographic keypair associated with the user's DID
 * stored in the devices Trusted Execution Environment via the Android KeyStore.
 *
 * Provides functionality for keypair generation, deletion, Public and PrivateKey object retrieval,
 * and checking the security level of the key
 */
object KeyManager {

    // Alias for the keypair in the Android KeyStore
    private const val KEYPAIR_ALIAS = "SolidWalletDIDKeys"

    // Lazily initialize the Android KeyStore instance
    private val keyStore by lazy {
        KeyStore.getInstance("AndroidKeyStore").apply {
            load(null)
        }
    }

    /**
     * Generate a new secp256r1 keypair via the keystore
     */
    fun generateKeys() {
        // Set up generator instance
        val keyPairGenerator = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_EC,
            "AndroidKeyStore"
        )

        // Configure the parameters of the key:
        val parameterSpec = KeyGenParameterSpec.Builder(
            KEYPAIR_ALIAS,
            KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY // Can be used for signing and verifying
        ).run {
            setAlgorithmParameterSpec(ECGenParameterSpec("secp256r1")) // Secp256r1 algorithm
            setDigests(KeyProperties.DIGEST_SHA256) // SHA256 digest
            setUserAuthenticationRequired(true) // Requires user authentication
            setUserAuthenticationParameters(
                30,
                KeyProperties.AUTH_BIOMETRIC_STRONG
            ) // User must have authenticated via biometric within last thirty seconds
            build()
        }

        // Generate the keypair
        keyPairGenerator.initialize(parameterSpec)
        keyPairGenerator.generateKeyPair()
    }

    /**
     * Retrieve the public key as a PublicKey object
     *
     * @return a PublicKey associated with the keypair
     */
    fun getPublicKey(): PublicKey {
        return keyStore.getCertificate(KEYPAIR_ALIAS).publicKey
    }

    /**
     * Retrieve the private key as a PrivateKey object
     *
     * Note: The PrivateKey object does not expose the raw key material,
     * as the key should be securely managed by the Trusted Execution Environment.
     *
     * @return a PrivateKey object corresponding to the keypair
     */
    fun getPrivateKey(): PrivateKey {
        return keyStore.getKey(KEYPAIR_ALIAS, null) as PrivateKey
    }

    /**
     * Check if the keypair exists in the keystore
     *
     * @return true if the keypair exists, false otherwise
     */
    fun keyExists(): Boolean {
        return keyStore.aliases().toList().contains(KEYPAIR_ALIAS)
    }

    /**
     * Remove the key entry from the keystore
     * and delete any associated DID and VC
     */
    fun deleteKey() {
        if (!keyExists()) return // No further action required

        keyStore.deleteEntry(KEYPAIR_ALIAS)
        PreferencesManager.deleteValue(PreferencesManager.Keys.DID)
        PreferencesManager.deleteValue(PreferencesManager.Keys.DID_DOCUMENT) // With the destruction of the key, any existing DID document is invalidated
        PreferencesManager.deleteValue(PreferencesManager.Keys.VERIFIABLE_CREDENTIAL) // With the destruction of the DID, any existing VC is invalidated
    }

    /**
     * Retrieve the security level of the keypair as a string
     *
     * @return a string corresponding to the security level of the key
     */
    fun getSecurityLevel(): String {
        // Set up a KeyInfo object for the private key
        val privateKey = getPrivateKey()
        val keyFactory = KeyFactory.getInstance(privateKey.algorithm, "AndroidKeyStore")
        val keyInfo = keyFactory.getKeySpec(privateKey, KeyInfo::class.java)

        // Return a string corresponding to the security level
        return when (keyInfo.securityLevel) {
            KeyProperties.SECURITY_LEVEL_TRUSTED_ENVIRONMENT -> "Trusted Environment"
            KeyProperties.SECURITY_LEVEL_STRONGBOX -> "Strongbox"
            KeyProperties.SECURITY_LEVEL_SOFTWARE -> "Software"
            else -> "Unknown or Other"
        }
    }

    /**
     * This function is identical to generateKeys except that it does not
     * enforce biometric authentication for key use.
     *
     * It's exclusive use is for testing that signed Verifiable Presentations can be verified
     * in instrumented testing via PresentationManagerTest
     */
    fun generateKeysTestMode() {
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
            build()
        }

        keyPairGenerator.initialize(parameterSpec)
        keyPairGenerator.generateKeyPair()
    }

}