package org.walletapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.walletapp.crypto.KeyManager
import org.walletapp.did.DIDManager
import java.security.interfaces.ECPublicKey

@RunWith(AndroidJUnit4::class)
class DIDManagerTest {

    @Test
    fun testJwkCreation() {
        KeyManager.generateKeys()
        val publicKey = KeyManager.getPublicKey() as ECPublicKey
        DIDManager.createJwk(publicKey)
    }

}