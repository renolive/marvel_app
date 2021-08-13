package com.renatoaoliveira.character.utils

import java.security.MessageDigest

fun String.digestMD5(): String {
    val bytes = this.toByteArray()
    val messageDigest = MessageDigest.getInstance("MD5")
    return messageDigest.digest(bytes).joinToString("") {
        "%02x".format(it)
    }
}