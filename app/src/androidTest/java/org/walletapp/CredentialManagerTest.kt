package org.walletapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.jsonwebtoken.Jwts
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.walletapp.managers.KeyManager
import org.walletapp.managers.PreferencesManager
import org.walletapp.managers.PresentationManager
import org.walletapp.data.VerifiablePresentationRequest
import org.walletapp.exceptions.NoDIDException

/**
 * Tests for the CredentialManager object
 */
@RunWith(AndroidJUnit4::class)
class CredentialManagerTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() {
        KeyManager.generateKeysTestMode()
        PreferencesManager.init(context)
        PreferencesManager.setValue(PreferencesManager.Keys.DID, "did:web:example.com")
    }

    @After
    fun tearDown() {
        KeyManager.deleteKey()
        PreferencesManager.deleteValue(PreferencesManager.Keys.DID)
    }

    /**
     * Test that the Verifiable Presentation is created correctly
     */
    @Test
    fun testCreateVerifiablePresentationJwt() {
        val request = VerifiablePresentationRequest(
            nonce = "testnonce",
            domain = "testdomain",
            appName = "testapp",
            webSocketsUrl = "http://example.com"
        )

        // Sample VC
        val vc = """
            {
                "@context": ["https://www.w3.org/2018/credentials/v1"],
                "type": ["VerifiableCredential"],
                "credentialSubject": {
                    "id": "did:web:example.com"
                }
            }
        """.trimIndent()

        val jwt = PresentationManager.createVerifiablePresentationJwt(request, vc)

        // Parse the JWT and verify with the public key
        val claims = Jwts.parser()
            .verifyWith(KeyManager.getPublicKey())
            .build()
            .parseSignedClaims(jwt)
            .payload

        // Test that the claims are correct
        assertEquals("did:web:example.com", claims.issuer)
        assertEquals("testnonce", claims["nonce"])
        assertEquals("testdomain", claims["domain"])
        assertEquals("testapp", claims["appName"])
    }

    /**
     * Test that VP creation fails if no DID has been set up
     */
    @Test
    fun testCreateVerifiablePresentationJwtNoDID() {
        PreferencesManager.deleteValue(PreferencesManager.Keys.DID)

        val request = VerifiablePresentationRequest(
            nonce = "testnonce",
            domain = "testdomain",
            appName = "testapp",
            webSocketsUrl = "http://example.com"
        )

        val vc = """
            {
                "@context": ["https://www.w3.org/2018/credentials/v1"],
                "type": ["VerifiableCredential"],
                "credentialSubject": {
                    "id": "did:web:example.com"
                }
            }
        """.trimIndent()

        // Test that the method throws a NoDIDException.
        assertThrows(NoDIDException::class.java) {
            PresentationManager.createVerifiablePresentationJwt(request, vc)
        }
    }
}