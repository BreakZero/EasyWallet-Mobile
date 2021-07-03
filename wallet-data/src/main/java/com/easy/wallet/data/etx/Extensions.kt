package com.easy.wallet.data.etx

import com.google.protobuf.ByteString
import org.web3j.utils.Numeric
import java.math.BigInteger

fun ByteArray.toHex(): String {
    return Numeric.toHexString(this)
}

fun String.toHexBytes(): ByteArray {
    return Numeric.hexStringToByteArray(this)
}

fun String.toHexByteArray(): ByteArray {
    return Numeric.hexStringToByteArray(this)
}

fun String.toByteString(): ByteString {
    return ByteString.copyFrom(this, Charsets.UTF_8)
}

fun String.toHexBytesInByteString(): ByteString {
    return ByteString.copyFrom(this.toHexBytes())
}

fun Int.toHexByteArray(): ByteArray {
    return Numeric.hexStringToByteArray(this.toString(16))
}

fun BigInteger.toHexByteArray(): ByteArray {
    return Numeric.hexStringToByteArray(this.toString(16))
}
