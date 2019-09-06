package com.rikkei.msa.ui.main

import com.rikkei.msa.model.Song

interface MainContract {

    interface View {
        fun showSongs(songs: ArrayList<Song>)
        fun showLastSong(song: Song?)
    }

    interface Presenter {
        fun getSongs()
        fun getLastSong()
    }
}
