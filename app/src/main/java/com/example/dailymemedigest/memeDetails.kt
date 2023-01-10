package com.example.dailymemedigest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_detail_meme.*

class memeDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_meme)

        //Set Up Recycler View dengan adapter
        val linearLayoutManager = LinearLayoutManager(this)
        val a = MemeDetailAdapter(this)

        with(recyclerViewCommentMeme){
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            adapter = a
        }
    }
}