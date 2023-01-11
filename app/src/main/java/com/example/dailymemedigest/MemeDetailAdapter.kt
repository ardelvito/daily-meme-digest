package com.example.dailymemedigest

import android.content.Context
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
import kotlinx.android.synthetic.main.comment_card.view.*
import com.android.volley.Request
import kotlinx.android.synthetic.main.memes_card.view.*
import org.json.JSONObject


class MemeDetailAdapter(val context: Context, private val comments:ArrayList<Comment>, private val userId: Int): RecyclerView.Adapter<MemeDetailAdapter.MemeDetailViewHolder>() {
    class MemeDetailViewHolder(val view: View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemeDetailViewHolder {
        //load comment card
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.comment_card, parent, false)
        return MemeDetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemeDetailViewHolder, position: Int) {
        //bind data ke comment meme card
        val comment = comments[position]
        with(holder.view){
            txtUsernameComment.text = comment.fullName
            txtContentComment.text = comment.commentContent
            txtDateComment.text = comment.date

            //region check likes
            val checkLikes = 0

            val q = Volley.newRequestQueue(this.context)
            val urlvol = "https://ubaya.fun/native/160420024/memes_api/check_comment_likes.php"

            val stringRequest = object : StringRequest(
                Request.Method.POST,
                urlvol,
                {
                    val obj = JSONObject(it)
                    Log.d("cekparams", it)
                    if(obj.getString("result") == "DETECTED"){
                        val like_val = obj.getInt("data")
                        Log.d("Liked", like_val.toString())
                        if(like_val == 1){
                            btnLikeComment.icon = ContextCompat.getDrawable(this.context, R.drawable.ic_baseline_thumb_up_alt_24)
                        }
                        else{
                            btnLikeComment.icon = ContextCompat.getDrawable(this.context, R.drawable.ic_baseline_thumb_up_off_alt_24)
                        }
                    }
                },
                {
                    Log.e("apiresult", it.message.toString())
                })
            {
                override fun getParams(): MutableMap<String, String>? {
                    val map = HashMap<String, String>()
                    map.set("id_comment", comment.id.toString())
                    map.set("id_user", userId.toString())
                    return map
                }
            }
            q.add(stringRequest)
            //endregion

            //region btn likes
            btnLikeComment.setOnClickListener{
                val q = Volley.newRequestQueue(this.context)
                val urlVol = "https://ubaya.fun/native/160420024/memes_api/set_comment_likes.php"
                val stringRequest = object : StringRequest(
                    Request.Method.POST,
                    urlVol, {
                        val obj = JSONObject(it)
                        if(obj.getString("result") == "OK"){
                            val statLike = obj.getInt("status")
                            if(statLike == 1){
                                btnLikeComment.icon = ContextCompat.getDrawable(this.context, R.drawable.ic_baseline_thumb_up_alt_24)
                                Toast.makeText(this.context, "Berhasil melakukan likes!", Toast.LENGTH_SHORT).show()
                            }
                            else{
                                btnLikeComment.icon = ContextCompat.getDrawable(this.context, R.drawable.ic_baseline_thumb_up_off_alt_24)
                                Toast.makeText(this.context, "Berhasil melakukan unlikes!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }, {
                        Log.e("apiresult", it.message.toString())
                    })
                {
                    override fun getParams(): MutableMap<String, String>? {
                        val map = HashMap<String, String>()
                        map.set("id_comment", comment.id.toString())
                        map.set("id_user", userId.toString())
                        return map
                    }
                }
                q.add(stringRequest)
            }
            //endregion
        }
    }

    override fun getItemCount()= comments.size
}