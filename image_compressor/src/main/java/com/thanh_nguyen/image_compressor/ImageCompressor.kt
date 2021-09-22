package com.thanh_nguyen.image_compressor

import android.content.Context
import android.net.Uri
import com.thanh_nguyen.image_compressor.constraint.Compression
import com.thanh_nguyen.image_compressor.constraint.default
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.coroutines.CoroutineContext

object ImageCompressor {
    suspend fun compress(
            context: Context,
            imageUri: Uri,
            imageFile: File,
            coroutineContext: CoroutineContext = Dispatchers.IO,
            compressionPatch: Compression.() -> Unit = { default() }
    ) = withContext(coroutineContext) {
        val compression = Compression().apply(compressionPatch)
        var result = copyToCache(context, imageUri, imageFile)
        compression.constraints.forEach { constraint ->
            while (constraint.isSatisfied(result).not()) {
                result = constraint.satisfy(result)
            }
        }
        return@withContext result
    }
}