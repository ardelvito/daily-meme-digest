package com.example.dailymemedigest

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.memes_card.view.*

class HomeAdapter(private val memes:ArrayList<Meme>)
    :RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    class HomeViewHolder(val v: View)
        :RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.memes_card, parent, false)
        Log.d("on create view holder", "success")
        return HomeViewHolder(v)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val posisi = position
        with(holder.v){

            val url = memes[posisi].url
            Picasso.get().load(url).into(imgMemes)
            memesTopText.setText(memes[posisi].top_text)
            memesBotText.setText(memes[position].bot_text)
            btnLikes.text = memes[posisi].total_likes.toString() + "Likes"
            btnComment.text = memes[posisi].total_komen.toString() + "Comment"
            txtCreatedUsername.text = memes[posisi].username
            var temp = memes[posisi].created_at.toString() + " ("
            if(memes[posisi].day > 0){
                temp += memes[posisi].day.toString() + "days and "
            }
            temp += memes[posisi].hours.toString() + "hours ago"

            txtCreatedAt.text = temp
            val expand:Boolean = memes[posisi].expandable
            expandable.visibility = if(expand) View.VISIBLE else View.GONE

            btnMore.setOnClickListener{
                memes[posisi].expandable = !memes[posisi].expandable
                notifyItemChanged(posisi)
            }
        }
    }

    override fun getItemCount(): Int {
        Log.d("memes size", memes.size.toString())
        return memes.size
    }
}