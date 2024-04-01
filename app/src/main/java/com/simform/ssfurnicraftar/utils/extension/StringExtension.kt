package com.simform.ssfurnicraftar.utils.extension

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * Encode String to base64
 *
 * @return The base64 encoded value
 */
@OptIn(ExperimentalEncodingApi::class)
fun String.encodeToBase64(): String =
    Base64.encode(this.toByteArray())

/**
 * Decode base64 value to string
 *
 * @return The decoded value
 */
@OptIn(ExperimentalEncodingApi::class)
fun String.decodeToBase64(): String =
    Base64.decode(this.toByteArray()).decodeToString()
