package com.rikkei.msa.storage

import android.content.Context
import android.content.SharedPreferences
import com.rikkei.msa.model.Song

class SharedPrefsManager(private val context: Context) {

    private fun get(): SharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)

    fun saveLastSong(song: Song, currentTime: Long) {
        get().setValue(KEY_SONG, song.path)
        get().setValue(KEY_CURRENT_TIME, currentTime)
    }

    fun getLastSongPath(): String? = get().get(KEY_SONG, "")

    fun getLastTime(): Long? = get().get(KEY_CURRENT_TIME, -1)

    companion object {
        private const val SP_NAME = "MSAPref"
        private const val KEY_SONG = "KEY_SONG"
        private const val KEY_CURRENT_TIME = "KEY_CURRENT_TIME"
    }
}
