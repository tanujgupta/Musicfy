package com.tanuj.musicfy.ui.adapters

import android.content.Context
import android.graphics.Color
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

    private var lyrics : List<Lyric> = listOf()
    private var context : Context? = null

    var curLyricPosition = 4
    private var lastSeekbarPositioninSec : Int = 0

    fun submitList(lyrics : List<Lyric>, context: Context?) {
        this.lyrics = lyrics
        this.context = context
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

        if (position == curLyricPosition) {
            holder.itemView.textview_lyric.setTextColor(
                context?.resources?.getColor(R.color.white) ?: Color.WHITE)
        } else {
            holder.itemView.textview_lyric.setTextColor(
                context?.resources?.getColor(R.color.transparent_white) ?: Color.WHITE)
        }
    }

    override fun getItemCount(): Int {
        return lyrics.size
    }

    fun setCurrentLyricPosition(curSeekbarPositioninSec : Int) {
        if (curSeekbarPositioninSec == lastSeekbarPositioninSec) return

        for (i in 0..(lyrics.size - 1) step 1) {
            if ((lyrics[i].timeStamp/1000).toInt() == curSeekbarPositioninSec) {
                lastSeekbarPositioninSec = curSeekbarPositioninSec
                curLyricPosition = i
            }
        }
        notifyDataSetChanged()
    }

}

