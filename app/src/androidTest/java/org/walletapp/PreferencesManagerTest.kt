package org.walletapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.walletapp.managers.PreferencesManager
import org.walletapp.exceptions.ValueNotFoundException

/**
 * Tests for the PreferencesManager object
 */
@RunWith(AndroidJUnit4::class)
class PreferencesManagerTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() {
        PreferencesManager.init(context)
    }

    @After
    fun tearDown() {
        PreferencesManager.deleteValue(PreferencesManager.Keys.DID)
        PreferencesManager.deleteValue(PreferencesManager.Keys.DID_DOCUMENT)
        PreferencesManager.deleteValue(PreferencesManager.Keys.VERIFIABLE_CREDENTIAL)
    }

    /**
     * Test that values can be set and retrieved.
     */
    @Test
    fun testSetAndGetValue() {
        PreferencesManager.setValue(PreferencesManager.Keys.DID, "testdid")
        val value = PreferencesManager.getValue(PreferencesManager.Keys.DID)
        assertEquals("testdid", value)
    }

    /**
     * Test that retrieving values for nonexistent entries throws an exception
     */
    @Test
    fun testGetValueNotFound() {
        assertThrows(ValueNotFoundException::class.java) { PreferencesManager.getValue("nonexistentkey") }
    }

    /**
     * Test that values can be deleted
     */
    @Test
    fun testDeleteValue() {
        PreferencesManager.setValue(PreferencesManager.Keys.DID, "testdid")
        PreferencesManager.deleteValue(PreferencesManager.Keys.DID)
        assertThrows(ValueNotFoundException::class.java) {
            PreferencesManager.getValue(
                PreferencesManager.Keys.DID
            )
        }
    }
}