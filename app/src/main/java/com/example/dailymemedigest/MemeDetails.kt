package com.example.dailymemedigest

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_meme.*
import kotlinx.android.synthetic.main.memes_card.view.*
import org.json.JSONObject

class MemeDetails : AppCompatActivity() {
    var commentList: ArrayList<Comment> = ArrayList()

    fun updateList(){
        var sharedName = packageName
        var shared = getSharedPreferences(sharedName, Context.MODE_PRIVATE)
        var user_id = shared.getInt(Login.SHARED_PLAYER_ID, 0)

        val linearLayoutManager = LinearLayoutManager(this)
        val a = MemeDetailAdapter(this, commentList, user_id)
        with(recyclerViewCommentMeme){
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            adapter = a
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_meme)

        val meme = intent.getSerializableExtra("EXTRA_MEME") as Meme
        var sharedName = packageName
        var shared = getSharedPreferences(sharedName, Context.MODE_PRIVATE)
        var user_id = shared.getInt(Login.SHARED_PLAYER_ID, 0)




        //region card data
        Picasso.get().load(meme.url).into(newImgMemes)
        newMemeTopText.setText(meme.top_text)
        newMemeBotText.setText(meme.bot_text)
        if(meme.top_text_color.contains("#") && meme.bot_text_color.contains("#")){
            newMemeBotText.setTextColor(Color.parseColor(meme.bot_text_color))
            newMemeTopText.setTextColor(Color.parseColor(meme.top_text_color))
        }
        else{
            newMemeBotText.setTextColor(ContextCompat.getColor(this.applicationContext, R.color.white))
            newMemeTopText.setTextColor(ContextCompat.getColor(this.applicationContext, R.color.white))
        }
        btnLikes.text = meme.total_likes.toString() + "Likes"
//endregion

        //region display total likes
        val checkLikes = 0

        val q = Volley.newRequestQueue(this.applicationContext)
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
                        btnLikes.icon = ContextCompat.getDrawable(this.applicationContext, R.drawable.ic_baseline_thumb_up_alt_24)
                    }
                    else{
                        btnLikes.icon = ContextCompat.getDrawable(this.applicationContext, R.drawable.ic_baseline_thumb_up_off_alt_24)
                    }
                }
            },
            {
                Log.e("apiresult", it.message.toString())
            })
        {
            override fun getParams(): MutableMap<String, String>? {
                val map = HashMap<String, String>()
                map.set("id_meme", meme.id.toString())
                map.set("id_user", user_id.toString())
                return map
            }
        }
        q.add(stringRequest)
        //endregion

        //region recyclerview
        val que = Volley.newRequestQueue(this.applicationContext)
        val urlQue = "https://ubaya.fun/native/160420024/memes_api/get_comments.php"
        var stringRequestVol = object:StringRequest(
            Request.Method.POST,
            urlQue,
            {
                val obj = JSONObject(it)
                if(obj.getString("result") == "OK"){
                    val data = obj.getJSONArray("data")
                    for (i in 0 until data.length()){
                        Log.d("apiresultDetails", it)

                        val commentObj = data.getJSONObject(i)
                        val comment = Comment(
                            commentObj.getInt("id"),
                            commentObj.getString("username"),
                            commentObj.getString("date"),
                            commentObj.getString("content")
                        )
                        commentList.add(comment)
                    }
                    updateList()
                }
            },
            {
                Log.e("apiresultDetails", it.message.toString())
            })
        {
            override fun getParams(): MutableMap<String, String>? {
                val map = HashMap<String, String>()
                map.set("memes_id", meme.id.toString())
                return map
            }
        }
        que.add(stringRequestVol)
        //endregion

        //region button add comment

        textInputLayoutComment.setEndIconOnClickListener {
            if(textInputEditComment.text.toString() != ""){
                val content = textInputEditComment.text.toString()
                val queAddComment = Volley.newRequestQueue(this.applicationContext)
                val urlAddComment = "https://ubaya.fun/native/160420024/memes_api/add_comments.php"
                var stringRequestAddComment = object:StringRequest(
                    Request.Method.POST,
                    urlAddComment, Response.Listener {
                        Log.d("Add comments", it)
                        val obj = JSONObject(it)
                        val result = obj.getString("result")
                        val message = obj.getString("message")
                        if(result == "OK"){
                            val data = obj.getJSONArray("data")
                            for (i in 0 until data.length()){
                                Log.d("apiresultDetails", it)

                                val commentObj = data.getJSONObject(i)
                                val comment = Comment(
                                    commentObj.getInt("id"),
                                    commentObj.getString("username"),
                                    commentObj.getString("date"),
                                    commentObj.getString("content")
                                )
                                commentList.add(comment)
                            }
                            updateList()
                        }
                    }, {
                        Log.e("Add comments", it.message.toString())
                    })
                {
                    override fun getParams(): MutableMap<String, String>? {
                        val map = HashMap<String, String>()
                        map.set("users_id", user_id.toString())
                        map.set("memes_id", meme.id.toString())
                        map.set("content", content)
                        return map
                    }
                }
                queAddComment.add(stringRequestAddComment)
            }
            else{
                Toast.makeText(this, "Input terlebih dahulu comment nya", Toast.LENGTH_SHORT).show()
            }
        }

        //endregion
    }
}