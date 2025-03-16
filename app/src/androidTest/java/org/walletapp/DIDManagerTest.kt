package org.walletapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertEquals
import org.json.JSONObject
import org.junit.After
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.walletapp.exceptions.NoKeyException
import org.walletapp.managers.DIDManager
import org.walletapp.managers.KeyManager
import org.walletapp.managers.PreferencesManager

/**
 * Tests for the DidManager object
 */
@RunWith(AndroidJUnit4::class)
class DIDManagerTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() {
        PreferencesManager.init(context)
        KeyManager.generateKeys()
    }

    @After
    fun tearDown() {
        KeyManager.deleteKey()
    }

    /**
     * Test that the DID document is created correctly
     */
    @Test
    fun testCreateDidDocument() {
        val didDocument = DIDManager.createDidDocument("did:web:example.com")

        // Parse the DID document as a JSON object
        val json = JSONObject(didDocument)

        // Test that the ID is correct
        assertEquals("did:web:example.com", json.getString("id"))

        // Test that the verificationMethod array has an entry
        val verificationMethodArray = json.getJSONArray("verificationMethod")
        assertEquals(1, verificationMethodArray.length())

        // Test that the verificationMethod entry is correct
        val verificationMethod = verificationMethodArray.getJSONObject(0)
        assertEquals("did:web:example.com#keys-1", verificationMethod.getString("id"))
        assertEquals("JsonWebKey2020", verificationMethod.getString("type"))
        assertEquals("did:web:example.com", verificationMethod.getString("controller"))

        // Test that the authenticationArray has the correct entry
        val authenticationArray = json.getJSONArray("authentication")
        assertEquals(1, authenticationArray.length())
        assertEquals("did:web:example.com#keys-1", authenticationArray.getString(0))

        // Test that the assertionMethodArray has the correct entry
        val assertionMethodArray = json.getJSONArray("assertionMethod")
        assertEquals(1, assertionMethodArray.length())
        assertEquals("did:web:example.com#keys-1", assertionMethodArray.getString(0))
    }

    /**
     * Test that attempting to create a DID document without a key throws an exception
     */
    @Test
    fun testCreateDidDocumentNoKey() {
        KeyManager.deleteKey()

        assertThrows(NoKeyException::class.java) {
            DIDManager.createDidDocument("did:web:example.com")
        }
    }
}