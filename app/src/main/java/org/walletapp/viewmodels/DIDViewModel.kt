package org.walletapp.viewmodels

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.walletapp.exceptions.ValueNotFoundException
import org.walletapp.managers.DIDManager
import org.walletapp.managers.PreferencesManager

/**
 * A ViewModel for the DID tab
 *
 * This ViewModel manages the DID, DID Document, and domain field, as well as functionality
 * for creating and deleting DIDs and their DID documents
 */
class DIDViewModel : ViewModel() {

    private var _did = mutableStateOf(try { PreferencesManager.getValue(PreferencesManager.Keys.DID) } catch (e: ValueNotFoundException) { "No DID" })
    val did = _did

    private var _didDocument = mutableStateOf(try { PreferencesManager.getValue(PreferencesManager.Keys.DID_DOCUMENT) } catch (e: ValueNotFoundException) { "No DID Document" })
    val didDocument = _didDocument

    private var _domain = mutableStateOf("")
    val domain = _domain

    /**
     * Create a DID and corresponding DID document using the domain entered by the user
     * and the keys previously generated
     *
     * @throws Exception if there is an issue creating the DID document (this is left generic as
     * all that matters is that the exception is propagated to the component.)
     */
    fun createDid() {
        val prospectiveDid = "did:web:${_domain.value}"
        try {
            _didDocument.value = DIDManager.createDidDocument(prospectiveDid)
        } catch (e: Exception) {
            throw e
        }
        PreferencesManager.setValue("didDocument", _didDocument.value)
        _did.value = prospectiveDid
        PreferencesManager.setValue(PreferencesManager.Keys.DID, _did.value)
    }

    /**
     * Copy the DID document to the device clipboard
     *
     * @param context the application context
     */
    fun copyDID(context: Context) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("DID", _didDocument.value)
        clipboard.setPrimaryClip(clip)
    }

    /**
     * Delete the DID and corresponding DID documents
     *
     * Any existing Verifiable Credentials are invalidated by this and thus also deleted
     */
    fun deleteDid() {
        _did.value = "No DID"
        _didDocument.value = "No DID Document"
        PreferencesManager.deleteValue(PreferencesManager.Keys.DID)
        PreferencesManager.deleteValue(PreferencesManager.Keys.DID_DOCUMENT)
        PreferencesManager.deleteValue(PreferencesManager.Keys.VERIFIABLE_CREDENTIAL)
    }

    /**
     * Handler for state changes in the domain field
     */
    fun domainChanged(newDomain: String) {
        _domain.value = newDomain
    }

}