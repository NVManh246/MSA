package com.rikkei.msa.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.transition.Transition
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionScene
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.OneShotPreDrawListener.add
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rikkei.msa.R
import com.rikkei.msa.adapter.SongAdapter
import com.rikkei.msa.model.Song
import com.rikkei.msa.storage.SharedPrefsManager
import com.rikkei.msa.ui.search.SearchFragment
import com.rikkei.msa.util.transaction
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import android.R.attr.data
import android.app.Activity
import android.os.Handler
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import com.rikkei.msa.util.onTextChange
import java.nio.file.Files.size


class MainActivity : AppCompatActivity(), MainContract.View {

    private val presenter: MainContract.Presenter by inject { parametersOf(this) }
    private val sharedPrefsManager: SharedPrefsManager by inject()
    private var song: Song? = null

    private val songAdapter = SongAdapter {
        onItemClick(it)
    }

    private val songAdapterSearchable = SongAdapter {
        onItemClick(it)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initPermission()
        initView()
        configureMotion()
        configureSearch()
        presenter.getSongs()
    }

    private fun configureSearch() {
        editSearch.onTextChange {
            songAdapterSearchable.filter.filter(it)
        }
    }

    private fun configureMotion() {

        editSearch.setOnFocusChangeListener { v, hasFocus ->
            if (motionContainer.currentState == R.id.start) {
                motionContainer.transitionToState(R.id.end2)
                motionContainer.setTransitionDuration(200)
            }
        }

        buttonCloseSearch.setOnClickListener {
            setStateFirst()
        }

        recyclerSong.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                if (e.action == MotionEvent.ACTION_DOWN
                    && rv.scrollState == RecyclerView.SCROLL_STATE_SETTLING
                ) {
                    rv.stopScroll()
                }
                return false
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })

        recyclerSongSearch.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                if (e.action == MotionEvent.ACTION_DOWN
                    && rv.scrollState == RecyclerView.SCROLL_STATE_SETTLING
                ) {
                    rv.stopScroll()
                }
                return false
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })

    }

    private fun initView() {
        with(recyclerSong) {
            adapter = songAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        with(recyclerSongSearch) {
            adapter = songAdapterSearchable
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        viewControlMini.setOnClickListener {
            println("abc")
        }
    }

    override fun showSongs(songs: ArrayList<Song>) {
        songAdapter.setSongs(songs)
        songAdapterSearchable.setSongs(songs)
        sharedPrefsManager.saveLastSong(songs[2], 10000)
        textTotal.text = "Songs: ${songs.size}"
        presenter.getLastSong()
    }

    override fun showLastSong(song: Song?) {
        if (song != null) {
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

    private fun onItemClick(song: Song) {
        setStateFirst()
        textTitleControl.text = song.title
        textArtistControl.text = song.artist
        if (song.image == null) {
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

    private fun setStateFirst() {
        if (motionContainer.currentState == R.id.end2) {
            editSearch.clearFocus()
            val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(editSearch.windowToken, 0)

            Handler().postDelayed({
                motionContainer.setTransitionDuration(200)
                motionContainer.transitionToState(R.id.start)
                editSearch.setText("")
            }, 300)
        }
    }
}
