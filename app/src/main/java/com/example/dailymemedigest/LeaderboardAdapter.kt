package com.example.dailymemedigest

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_ldb.view.*
import kotlinx.android.synthetic.main.memes_card.view.*

class LeaderboardAdapter(val ldbs:ArrayList<Leaderboard>): RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder>() {
    class LeaderboardViewHolder(val v: View) : RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var v = inflater.inflate(R.layout.fragment_ldb, parent,false)
        return LeaderboardViewHolder(v)
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val leaderboard = ldbs[position]
//        var numlikes = leaderboard.num_likes
//        if(numlikes == null){
//            numlikes = 0
//        } else {
//            numlikes = numlikes
//        }
        with(holder.v) {
            val url = leaderboard.image_url
            Picasso.get().load(url).into(imgProfileLDB)
//            txtFNameLDB.text = leaderboard.first
//            txtLNameLDB.text = leaderboard.last
            txtNL.text = "${leaderboard.num_likes} LIKES"
            if(leaderboard.private == 0){
                val first = leaderboard.first.toString()
                val last = leaderboard.last.toString()
                var tampungF = ArrayList<String>()
                var tampungL = ArrayList<String>()
                var gantiF = ""
                var gantiL = ""
                var tampungFN = ""

                //First Name
                for (i in 3..first.length){
                    tampungFN = first.substring(3,first.length)
                    tampungF += "*"
                }
                for (i in 0..tampungFN.length-1){
                    gantiF += tampungF[i]
                }
                txtFNameLDB.text = first.replace("$tampungFN", "$gantiF")

                //Last Name
                for (i in 0..last.length-1){
                    tampungL += "*"
                    gantiL += tampungL[i]
                }
                txtLNameLDB.text = last.replace("$last", "$gantiL")
            } else if(leaderboard.private == 1){
                val first = leaderboard.first
                val last = leaderboard.last.toString()
                txtFNameLDB.text = first
                txtLNameLDB.text = last
            }
        }
    }

    override fun getItemCount(): Int {
        return ldbs.size
    }
}