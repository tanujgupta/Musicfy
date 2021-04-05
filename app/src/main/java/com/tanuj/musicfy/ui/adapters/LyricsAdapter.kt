package com.tanuj.musicfy.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tanuj.musicfy.R
import com.tanuj.musicfy.data.Lyric
import kotlinx.android.synthetic.main.list_item_lyrics.view.*
import javax.inject.Inject


class LyricsAdapter @Inject constructor()
    : RecyclerView.Adapter<LyricsAdapter.LyricsViewHolder>() {

    class LyricsViewHolder(itemView: View)  : RecyclerView.ViewHolder(itemView)

    var lyrics : List<Lyric> = listOf()

    fun submitList(lyrics : List<Lyric>) {
        this.lyrics = lyrics
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LyricsAdapter.LyricsViewHolder {
        return LyricsAdapter.LyricsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_lyrics,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LyricsViewHolder, position: Int) {
        val lyric = lyrics[position]

        holder.itemView.textview_lyric.text = lyric.value
    }

    override fun getItemCount(): Int {
        return lyrics.size
    }

}

