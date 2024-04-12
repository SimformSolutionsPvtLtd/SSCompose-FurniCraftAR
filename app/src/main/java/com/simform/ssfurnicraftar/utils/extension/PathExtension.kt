package com.simform.ssfurnicraftar.utils.extension

import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.fileSize

/**
 * Check if the file exist at receivers path.
 * Optionally compare if the file size is same.
 *
 * @param size Optional size to compare with existing file size
 * @return Whether the file exist and optionally have the given size or not.
 */
fun Path.checkIfExist(size: Long? = null): Boolean =
    exists() && size?.let { it == fileSize() } != false
