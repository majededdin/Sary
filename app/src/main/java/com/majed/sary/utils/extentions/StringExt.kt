package com.majed.sary.utils.extentions

import android.util.Log
import com.majed.sary.data.consts.AppConst
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.security.SecureRandom


fun String.removeNumber(): String {
    var str = this
    if (str.isNotEmpty())
        str = str.substring(0, str.length - 1)
    return str
}

fun String.toLog() {
    if (AppConst.isDebug)
        Log.e("showLog ", this)
}

/**
 * Generates a 16-byte nonce with additional data.
 * The nonce should also include additional information, such as a user id or any other details
 * you wish to bind to this attestation. Here you can provide a String that is included in the
 * nonce after 24 random bytes. During verification, extract this data again and check it
 * against the request that was made with this nonce.
 */
fun String.getRequestNonce(): ByteArray? {
    val byteStream = ByteArrayOutputStream()
    val bytes = ByteArray(24)
    SecureRandom().nextBytes(bytes)
    try {
        byteStream.write(bytes)
        byteStream.write(this.toByteArray())
    } catch (e: IOException) {
        return null
    }
    return byteStream.toByteArray()
}
