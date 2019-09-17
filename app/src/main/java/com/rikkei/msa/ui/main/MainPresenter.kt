package com.rikkei.msa.ui.main


import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.AsyncTask
import android.os.Environment
import com.rikkei.msa.model.Song
import com.rikkei.msa.storage.SharedPrefsManager
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.io.File
import java.lang.ref.WeakReference

class MainPresenter(
    private val view: MainContract.View
) : MainContract.Presenter, KoinComponent {

    private val sharedPrefsManager: SharedPrefsManager by inject()

    override fun getSongs() {
        FetchSongsAsync(view).execute(MEDIA_PATH)
    }

    override fun getLastSong() {
        GetLastSongAsync(sharedPrefsManager, view).execute()
    }

    private class GetLastSongAsync internal constructor(
        private val sharedPrefsManager: SharedPrefsManager,
        view: MainContract.View
    ) : AsyncTask<Void, Void, Song>() {

        private val view: WeakReference<MainContract.View> = WeakReference(view)

        override fun doInBackground(vararg params: Void?): Song? {
            val path = sharedPrefsManager.getLastSongPath()
            if(path.equals("")) return null
            val metaRetriever = MediaMetadataRetriever().apply {
                setDataSource(path)
            }
            val file = File(path)
            val song = Song(
                metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                    ?: file.name.substring(0, file.name.length - 4),
                file.absolutePath,
                metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
                    ?: Song.UNKNOWN,
                metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                    ?: Song.UNKNOWN,
                false,
                metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toLong()
                    ?: -1
            )
            song.image = metaRetriever.embeddedPicture?.let {
                BitmapFactory.decodeByteArray(it, 0, it.size)
            }
            song.lastTime = sharedPrefsManager.getLastTime() ?: -1
            metaRetriever.release()
            return song
        }

        override fun onPostExecute(result: Song?) {
            super.onPostExecute(result)
            view.get()?.showLastSong(result)
        }
    }

    private class FetchSongsAsync internal constructor(view: MainContract.View) :
        AsyncTask<String, Void, ArrayList<Song>>() {

        private val view: WeakReference<MainContract.View> = WeakReference(view)
        private val songs = ArrayList<Song>()

        override fun doInBackground(vararg params: String?): ArrayList<Song> {
            songs.addAll(getPlayList(params[0]!!))
            return songs
        }

        override fun onPostExecute(result: ArrayList<Song>?) {
            super.onPostExecute(result)
            view.get()!!.showSongs(songs)
        }

        fun getPlayList(rootPath: String): ArrayList<Song> {
            val metaRetriever = MediaMetadataRetriever()
            val songList = ArrayList<Song>()
            val rootFolder = File(rootPath)
            val files = rootFolder.listFiles()
            for (file in files) {
                if (file.isDirectory) {
                    songList.addAll(getPlayList(file.absolutePath))
                } else if (file.name.endsWith(".mp3") || file.name.endsWith(".MP3")) {
                    val path = file.absolutePath
                    metaRetriever.setDataSource(path)

                    val song = Song(
                        metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                            ?: file.name.substring(0, file.name.length - 4),
                        file.absolutePath,
                        metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
                            ?: Song.UNKNOWN,
                        metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                            ?: Song.UNKNOWN,
                        false,
                        metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toLong()
                            ?: -1
                    )
                    song.image = metaRetriever.embeddedPicture?.let {
                        BitmapFactory.decodeByteArray(it, 0, it.size)
                    }
                    songList.add(song)
                }
            }
            metaRetriever.release()
            return songList
        }
    }

    companion object {
//        val MEDIA_PATH: String = Environment.getExternalStorageDirectory().path + "/MIUI/"
        val MEDIA_PATH: String = Environment.getExternalStorageDirectory().path
    }
}
