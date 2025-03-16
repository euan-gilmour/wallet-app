package org.walletapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.walletapp.managers.KeyManager
import org.walletapp.managers.PreferencesManager

/**
 * Tests for the KeyManager object
 */
@RunWith(AndroidJUnit4::class)
class KeyManagerTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() {
        KeyManager.deleteKey()
        PreferencesManager.init(context)
    }

    @After
    fun tearDown() {
        KeyManager.deleteKey()
    }

    /**
     * Test that generating the keypair succeeds and that the keypair exists after generating is
     */
    @Test
    fun testGenerateKeys() {
        assertFalse(KeyManager.keyExists())
        KeyManager.generateKeys()
        assertTrue(KeyManager.keyExists())
    }

    /**
     * Test that the keypair no longer exists after deletion
     */
    @Test
    fun testDeleteKey() {
        KeyManager.generateKeys()
        assertTrue(KeyManager.keyExists())

        KeyManager.deleteKey()
        assertFalse(KeyManager.keyExists())
    }

    /**
     * Test that the key is stored in a Trusted Execution Environment
     *
     * Note: This test will fail if run on a device without access to a TEE (such as an emulator)
     */
    @Test
    fun testGetSecurityLevel() {
        KeyManager.generateKeys()
        val securityLevel = KeyManager.getSecurityLevel()
        assertTrue(securityLevel == "Trusted Environment")
    }

    /**
     * Test that the public key can be retrieved after generation
     */
    @Test
    fun testGetPublicKey() {
        KeyManager.generateKeys()
        val publicKey = KeyManager.getPublicKey()
        assertNotNull(publicKey)
    }

    /**
     * Test that the private key can be retrieved after generation
     */
    @Test
    fun testGetPrivateKey() {
        KeyManager.generateKeys()
        val privateKey = KeyManager.getPrivateKey()
        assertNotNull(privateKey)
    }
}