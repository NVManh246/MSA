package com.rikkei.msa.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
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
): RecyclerView.Adapter<SongAdapter.ViewHolder>(), Filterable {

    private val songs = ArrayList<Song>()
    private val songsFiltered = ArrayList<Song>()
    var newListFilter: ArrayList<Song>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_song, parent, false)
        return ViewHolder(view, onItemClick)
    }

    override fun getItemCount(): Int {
        return songsFiltered.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(songsFiltered[position])
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint.toString()
                newListFilter = songs.filter {
                    it.title.toUpperCase().contains(query.toUpperCase())
                } as ArrayList<Song>
                println(newListFilter!!.size.toString())
                return FilterResults().apply {
                    values = newListFilter
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                updateList(results!!.values as ArrayList<Song>)
            }
        }
    }

    fun setSongs(songs: ArrayList<Song>) {
        this.songs.addAll(songs)
        updateList(this.songs)
    }

    fun updateList(newList: ArrayList<Song>) {
        val diffResult = DiffUtil.calculateDiff(SongDiffCallback(this.songsFiltered, newList))
        this.songsFiltered.clear()
        this.songsFiltered.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
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

    private class SongDiffCallback(
        private val oldList: ArrayList<Song>,
        private val newList: ArrayList<Song>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun getOldListSize(): Int = oldList.size


        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
