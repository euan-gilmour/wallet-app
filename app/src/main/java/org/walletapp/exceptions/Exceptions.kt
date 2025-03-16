package org.walletapp.exceptions

class ValueNotFoundException(message: String) : Exception(message)
class NoKeyException(message: String) : Exception(message)
class NoDIDException(message: String) : Exception(message)
class NoVerifiableCredentialException(message: String) : Exception(message)
class MismatchedRecipientException(message: String) : Exception(message)
class InvalidVerifiableCredentialInvitationException(message: String) : Exception(message)
class InvalidVerifiablePresentationRequestException(message: String) : Exception(message)