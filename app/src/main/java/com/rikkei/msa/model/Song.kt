package com.rikkei.msa.model

import android.graphics.Bitmap

data class Song(
    val title: String,
    val path: String,
    val album: String,
    val artist: String,
    val hasImage: Boolean,
    val duration: Long,
    var image: Bitmap? = null,
    var lastTime: Long = 0
) {

    companion object {
        const val UNKNOWN = "unknown"
    }
}
