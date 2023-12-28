package com.tqbfoxx.mcryptotwo.encryption

import android.content.Context
import android.content.SharedPreferences
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize

const val ERROR_STRING = "ERROR"

val gson = Gson()

@Parcelize
data class CipherX(var name: String, var keyValue: String, var index: Int): Parcelable

val Context.ciphersPref: SharedPreferences
	get() = getSharedPreferences("ciphersPref", Context.MODE_PRIVATE)

var Context.ciphers: ArrayList<CipherX>
	get() {
		val ciphersJson = ciphersPref.getString("ciphers", ERROR_STRING)
		return gson.fromJson(ciphersJson, object : TypeToken<ArrayList<CipherX>>() {}.type)
	}
	set(ciphers) = ciphersPref.edit()
		.putString("ciphers", gson.toJson(ciphers)).apply()

var Context.activeCipher: CipherX
	get() {
		val activeCipherJson = ciphersPref.getString("activeCipher", /*defValue*/ERROR_STRING)
		return gson.fromJson(activeCipherJson, CipherX::class.java)
	}
	set(activeCipher) = ciphersPref.edit()
		.putString("activeCipher", gson.toJson(activeCipher)).apply()

fun Context.addCipher(cipher: CipherX) {
	val newCiphers = (ciphers + cipher) as ArrayList
	ciphers = newCiphers
}