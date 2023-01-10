package com.example.dailymemedigest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.comment_card.view.*

class MemeDetailAdapter(val context: Context): RecyclerView.Adapter<MemeDetailAdapter.MemeDetailViewHolder>() {
    class MemeDetailViewHolder(val view: View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemeDetailViewHolder {
        //load comment card
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.comment_card, parent, false)
        return MemeDetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemeDetailViewHolder, position: Int) {
        //bind data ke comment meme card
        val comment = Global.comment[position]
        with(holder.view){
            txtUsernameComment.text = comment.firstName
            txtContentComment.text = comment.commentContent
            txtDateComment.text = comment.date
        }
    }

    override fun getItemCount()= Global.comment.size
}