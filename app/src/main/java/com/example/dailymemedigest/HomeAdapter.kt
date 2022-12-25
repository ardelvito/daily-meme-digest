package com.example.dailymemedigest

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import com.android.volley.Request
import kotlinx.android.synthetic.main.memes_card.view.*
import org.json.JSONObject

class HomeAdapter(private val memes:ArrayList<Meme>, private val user_id:Int)
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
            //region card data
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
            //endregion

            //region expandable
            txtCreatedAt.text = temp
            val expand:Boolean = memes[posisi].expandable
            expandable.visibility = if(expand) View.VISIBLE else View.GONE

            btnMore.setOnClickListener{
                memes[posisi].expandable = !memes[posisi].expandable
                notifyItemChanged(posisi)
            }
//endregion

            //region Check likes
            val checkLikes = 0

            val q = Volley.newRequestQueue(this.context)
            val urlVol = "https://ubaya.fun/native/160420024/memes_api/check_likes.php"

            val stringRequest = object : StringRequest(
                Request.Method.POST,
                urlVol,
                {
                    val obj = JSONObject(it)
                    Log.d("cekparams", it)
                    if(obj.getString("result") == "DETECTED"){
                        val like_val = obj.getInt("data")
                        Log.d("id user", user_id.toString())

//                        var buttonLike = findViewById<Button>(R.id.btnLikes)
                        if(like_val == 1){
                            btnLikes.icon = ContextCompat.getDrawable(this.context, R.drawable.ic_baseline_thumb_up_alt_24)
                        }
                        else{
                            btnLikes.icon = ContextCompat.getDrawable(this.context, R.drawable.ic_baseline_thumb_up_off_alt_24)
                        }
                    }
                },
                {
                    Log.e("apiresult", it.message.toString())
                })
            {
                override fun getParams(): MutableMap<String, String>? {
                    val map = HashMap<String, String>()
                    map.set("id_meme", memes[posisi].id.toString())
                    map.set("id_user", user_id.toString())
                    return map
                }
            }
            q.add(stringRequest)
            //endregion
        }
    }

    override fun getItemCount(): Int {
        Log.d("memes size", memes.size.toString())
        return memes.size
    }
}