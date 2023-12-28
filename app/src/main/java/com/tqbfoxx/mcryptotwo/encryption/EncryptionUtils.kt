package com.tqbfoxx.mcryptotwo.encryption

import android.content.Context

const val START_ID = "//"
const val END_ID = "√√"

fun Context.encrypt(string: String): String {
	val simpleEncryptedText = AESUtils.encrypt(string, this.activeCipher.keyValue.toByteArray())
	return "$START_ID$simpleEncryptedText$END_ID"
}

fun Context.decrypt(string: String): String {
	val simpleEncryptedText = string.removePrefix(START_ID).removeSuffix(
		END_ID
	)
	return AESUtils.decrypt(simpleEncryptedText, this.activeCipher.keyValue.toByteArray())
}

fun String.isEncrypted(): Boolean {
	return this.startsWith(START_ID) || this.endsWith(END_ID)
}