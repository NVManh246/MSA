package com.rikkei.msa.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.rikkei.msa.R
import com.rikkei.msa.model.Song

class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val songs = arguments!!.get("SONGS") as ArrayList<*>
        println(songs.size)
    }

    companion object {
        fun newInstance(songs: ArrayList<Song>) = SearchFragment().apply {
            arguments = bundleOf(
                "SONGS" to songs
            )
        }
    }
}
