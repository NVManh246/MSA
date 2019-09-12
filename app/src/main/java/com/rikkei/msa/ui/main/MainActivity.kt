package com.rikkei.msa.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rikkei.msa.R
import com.rikkei.msa.adapter.SongAdapter
import com.rikkei.msa.model.Song
import com.rikkei.msa.storage.SharedPrefsManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity(), MainContract.View {

    private val presenter: MainContract.Presenter by inject { parametersOf(this) }
    private val sharedPrefsManager: SharedPrefsManager by inject()
    private var song: Song? = null


    private val songAdapter = SongAdapter{
        onItemClick(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initPermission()
        initView()
        presenter.getSongs()
    }

    private fun initView() {
        with(recyclerSong){
            adapter = songAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    override fun showSongs(songs: ArrayList<Song>) {
        songAdapter.setSongs(songs)
        sharedPrefsManager.saveLastSong(songs[2], 10000)
        textTotal.text = "Songs: ${songs.size}"
        presenter.getLastSong()
    }

    override fun showLastSong(song: Song?) {
        if(song != null) {
            setControlMini(song)
        } else {
            println("null")
        }
    }

    private fun setControlMini(song: Song) {
        textTitleControl.text = song.title
        textArtistControl.text = song.artist
        seekArc.progress = ((song.lastTime.toFloat() / song.duration) * 100).toInt()
    }

    fun onItemClick(song: Song) {
        textTitleControl.text = song.title
        textArtistControl.text = song.artist
        if(song.image == null) {
            imageSongControl.setImageResource(R.drawable.ic_album_white_24dp)
        } else {
            Glide.with(this)
                .load(song.image)
                .apply(RequestOptions.circleCropTransform())
                .into(imageSongControl.imageSongControl)
        }
    }

    private fun initPermission() {
        val permission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1
            )
        }
    }
}
