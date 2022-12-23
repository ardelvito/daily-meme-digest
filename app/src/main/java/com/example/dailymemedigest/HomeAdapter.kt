package com.example.dailymemedigest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.memes_card.view.*

class HomeAdapter(val memes:ArrayList<Meme>)
    :RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    class HomeViewHolder(val v: View):RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.fragment_home, parent, false)
        return HomeViewHolder(v)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val url = memes[position].url
        with(holder.v){
            Picasso.get().load(url).into(imgMemes)
            memesTopText.setText(memes[position].top_text)
            memesBotText.setText(memes[position].bot_text)
            btnLikes.text = memes[position].total_likes.toString()
        }

    }

    override fun getItemCount(): Int {
        return memes.size
    }
}