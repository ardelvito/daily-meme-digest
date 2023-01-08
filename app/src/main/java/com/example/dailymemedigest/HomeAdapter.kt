package com.example.dailymemedigest

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
            val top_text_color = memes[position].top_text_color
            val bot_text_color = memes[position].bot_text_color
            Picasso.get().load(url).into(newImgMemes)
            newMemeTopText.setText(memes[posisi].top_text)
            newMemeBotText.setText(memes[position].bot_text)
            if(top_text_color.contains("#") && bot_text_color.contains("#")){
                newMemeBotText.setTextColor(Color.parseColor(bot_text_color))
                newMemeTopText.setTextColor(Color.parseColor(top_text_color))
            }
            else{
                newMemeBotText.setTextColor(ContextCompat.getColor(context, R.color.white))
                newMemeTopText.setTextColor(ContextCompat.getColor(context, R.color.white))
            }
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

            //region btn likes
            btnLikes.setOnClickListener{
                val q = Volley.newRequestQueue(this.context)
                val urlVol = "https://ubaya.fun/native/160420024/memes_api/set_likes.php"
                val stringRequest = object : StringRequest(
                    Request.Method.POST,
                    urlVol, {
                        val obj = JSONObject(it)
                        Log.d("cekparamsSetLikes", it)
                        if(obj.getString("result") == "OK"){
                            val statLike = obj.getInt("status")
                            if(statLike == 1){
                                memes[posisi].total_likes += 1
                                btnLikes.icon = ContextCompat.getDrawable(this.context, R.drawable.ic_baseline_thumb_up_alt_24)
                            }
                            else{
                                memes[posisi].total_likes -= 1
                                btnLikes.icon = ContextCompat.getDrawable(this.context, R.drawable.ic_baseline_thumb_up_off_alt_24)
                            }
                            btnLikes.text = memes[posisi].total_likes.toString() + "Likes"
                            Toast.makeText(this.context, "Berhasil melakukan likes!", Toast.LENGTH_SHORT).show()
                        }
                    }, {
                        Log.e("apiresult", it.message.toString())
                    }
                )
                {
                    override fun getParams(): MutableMap<String, String>? {
                        val map = HashMap<String, String>()
                        map.set("id_memes", memes[posisi].id.toString())
                        map.set("id_user", user_id.toString())
                        return map
                    }
                }
                q.add(stringRequest)
            }
            //endregion

            //region btn comment
            btnComment.setOnClickListener{
                val intent = Intent(this.context, memeDetails::class.java)
                this.context.startActivity(intent)
            }

            //endregion
        }
    }

    override fun getItemCount(): Int {
        Log.d("memes size", memes.size.toString())
        return memes.size
    }
}