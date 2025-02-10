package org.walletapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.walletapp.credential.CredentialManager
import org.walletapp.crypto.KeyManager

@RunWith(AndroidJUnit4::class)
class CredentialManagerTest {
    @Test
    fun testSigningVerification() {
        KeyManager.generateKeys()
        val payload = "TEST STRING".toByteArray()
        val signatureBytes = CredentialManager.signWithKeyStore(KeyManager.getPrivateKey(), payload)
        assert(CredentialManager.verifySignature(payload, signatureBytes))
    }
}