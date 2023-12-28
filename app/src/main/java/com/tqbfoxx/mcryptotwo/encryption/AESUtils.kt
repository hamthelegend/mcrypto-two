package com.tqbfoxx.mcryptotwo.encryption

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

internal object AESUtils {
	
	private val HEX = "0123456789ABCDEF"
	
	@Throws(Exception::class)
	fun encrypt(cleartext: String, keyValue: ByteArray): String {
		val rawKey = getRawKey(keyValue)
		val result =
			encrypt(rawKey, cleartext.toByteArray())
		return toHex(result)
	}
	
	@Throws(Exception::class)
	fun decrypt(encrypted: String, keyValue: ByteArray): String {
		
		val enc = toByte(encrypted)
		val result = decrypt(enc, keyValue)
		return String(result)
	}
	
	@Throws(Exception::class)
	private fun getRawKey(keyValue: ByteArray): ByteArray {
		val key = SecretKeySpec(keyValue, "AES")
		return key.encoded
	}
	
	@Throws(Exception::class)
	private fun encrypt(raw: ByteArray, clear: ByteArray): ByteArray {
		val skeySpec = SecretKeySpec(raw, "AES")
		val cipher = Cipher.getInstance("AES")
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec)
		return cipher.doFinal(clear)
	}
	
	@Throws(Exception::class)
	private fun decrypt(encrypted: ByteArray, keyValue: ByteArray): ByteArray {
		val skeySpec = SecretKeySpec(keyValue, "AES")
		val cipher = Cipher.getInstance("AES")
		cipher.init(Cipher.DECRYPT_MODE, skeySpec)
		return cipher.doFinal(encrypted)
	}
	
	private fun toByte(hexString: String): ByteArray {
		val len = hexString.length / 2
		val result = ByteArray(len)
		for (i in 0 until len)
			result[i] = Integer.valueOf(
				hexString.substring(2 * i, 2 * i + 2),
				16
			).toByte()
		return result
	}
	
	private fun toHex(buf: ByteArray?): String {
		if (buf == null)
			return ""
		val result = StringBuffer(2 * buf.size)
		for (aBuf in buf) {
			appendHex(result, aBuf)
		}
		return result.toString()
	}
	
	private fun appendHex(sb: StringBuffer, b: Byte) {
		sb.append(HEX[b.toInt() shr 4 and 0x0f]).append(
			HEX[b.toInt() and 0x0f])
	}
}