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
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.memes_card.view.*
import org.json.JSONObject

class CreationAdapter(private val memes:ArrayList<Meme>, private val user_id:Int)
    :RecyclerView.Adapter<CreationAdapter.CreationViewHolder>() {
    class CreationViewHolder(val v: View): RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.memes_card, parent, false)
        Log.d("creation viewholder", "success")
        return CreationViewHolder(v)
    }

    override fun onBindViewHolder(holder: CreationViewHolder, position: Int) {
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
            if(memes[posisi].private == 0){
                val username = memes[posisi].username.toString()
                var tampungU = ArrayList<String>()
                var gantiU = ""
                var tampungUN = ""
                for (i in 3..username.length){
                    tampungUN = username.substring(3,username.length)
                    tampungU += "*"
                }
                for (i in 0..tampungUN.length-1){
                    gantiU += tampungU[i]
                }
                txtCreatedUsername.text = username.replace("$tampungUN", "$gantiU")
            } else if (memes[posisi].private == 1) {
                txtCreatedUsername.text = memes[posisi].username
            }
            var temp = memes[posisi].created_at.toString() + " ("
            if(memes[posisi].day > 0){
                temp += memes[posisi].day.toString() + "days and "
            }
            temp += memes[posisi].hours.toString() + "hours ago)"
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
                Toast.makeText(this.context, "Stop being narcistic by liking your own content bruh", Toast.LENGTH_SHORT).show()
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