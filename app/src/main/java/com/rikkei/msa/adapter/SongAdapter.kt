package com.rikkei.msa.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.rikkei.msa.R
import com.rikkei.msa.model.Song
import com.rikkei.msa.util.TimeHelper
import kotlinx.android.synthetic.main.item_song.view.*
import java.io.ByteArrayOutputStream

class SongAdapter(
    private val onItemClick: (Song) -> Unit
): RecyclerView.Adapter<SongAdapter.ViewHolder>() {
    private val songs = ArrayList<Song>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_song, parent, false)
        return ViewHolder(view, onItemClick)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(songs[position])
    }

    fun setSongs(songs: ArrayList<Song>) {
        this.songs.addAll(songs)
        this.songs.addAll(songs)
        this.songs.addAll(songs)
        notifyDataSetChanged()
    }

    class ViewHolder(parent: View, private val onItemClick: (Song) -> Unit): RecyclerView.ViewHolder(parent) {
        fun bindTo(song: Song) {
            itemView.setOnClickListener {
                onItemClick(song)
            }
            with(itemView) {
                textDuration.text = TimeHelper.covertMillisecondToMinute(song.duration.toLong())
                textTitle.text = song.title
                textArtist.text = song.artist
            }
            if(song.image == null) {
                itemView.imageSong.setImageResource(R.drawable.ic_album_black_24dp)
            } else {
                Glide.with(itemView)
                    .load(song.image)
                    .apply(RequestOptions.circleCropTransform())
                    .into(itemView.imageSong)
            }
        }
    }
}
