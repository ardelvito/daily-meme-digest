package com.example.dailymemedigest

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.memes_card.*
import kotlinx.android.synthetic.main.memes_card.view.*
import org.json.JSONObject

class HomeFragment : Fragment() {
    var memesList:ArrayList<Meme> = ArrayList()
    lateinit var preferences: SharedPreferences
    var username: String = ""
    var user_id: Int = 0

    fun updateList(){
        val sharedName = "com.example.dailymemedigest"
        preferences = this.requireActivity().getSharedPreferences(sharedName, Context.MODE_PRIVATE)
        val id_user = preferences.getInt(Login.SHARED_PLAYER_ID, 0)
        user_id = id_user

        val lm:LinearLayoutManager = LinearLayoutManager(activity)
        val rv = view?.findViewById<RecyclerView>(R.id.memesView)
        rv?.layoutManager = lm
        rv?.setHasFixedSize(true)
        rv?.adapter = HomeAdapter(memesList, user_id)
    }

    override fun onResume() {
        super.onResume()

        memesList.clear()
        getMemes()
    }

    fun getMemes() {
        val q = Volley.newRequestQueue(activity)
        val url = "https://ubaya.fun/native/160420024/memes_api/get_memes.php"
        var stringRequest = StringRequest(
            Request.Method.POST,
            url,
            {
                val obj = JSONObject(it)
                if(obj.getString("result") == "OK"){
                    val data = obj.getJSONArray("data")
                    for(i in 0 until data.length()){
                        Log.d("apiresult", it)

                        val memeObj = data.getJSONObject(i)
                        val meme = Meme(
                            memeObj.getInt("id"),
                            memeObj.getString("url"),
                            memeObj.getString("top_text"),
                            memeObj.getString("bottom_text"),
                            memeObj.getInt("users_id"),
                            memeObj.getInt("total_likes"),
                            memeObj.getString("created_at"),
                            memeObj.getString("top_text_color"),
                            memeObj.getString("bottom_text_color"),
                            memeObj.getInt("total_komen"),
                            false,
                            memeObj.getString("username"),
                            memeObj.getInt("private"),
                            memeObj.getInt("day_differ"),
                            memeObj.getInt("hours_differ")
                        )
                        memesList.add(meme)
                    }
                    updateList()
                }
            }, {
                Log.e("apiresult", it.message.toString())
            })
        q.add(stringRequest)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        memesList.clear()
//
//        val q = Volley.newRequestQueue(activity)
//        val url = "https://ubaya.fun/native/160420024/memes_api/get_memes.php"
//        var stringRequest = StringRequest(
//            Request.Method.POST,
//            url,
//            {
//                val obj = JSONObject(it)
//                if(obj.getString("result") == "OK"){
//                    val data = obj.getJSONArray("data")
//                    for(i in 0 until data.length()){
//                        Log.d("apiresult", it)
//
//                        val memeObj = data.getJSONObject(i)
//                        val meme = Meme(
//                            memeObj.getInt("id"),
//                            memeObj.getString("url"),
//                            memeObj.getString("top_text"),
//                            memeObj.getString("bottom_text"),
//                            memeObj.getInt("users_id"),
//                            memeObj.getInt("total_likes"),
//                            memeObj.getString("created_at"),
//                            memeObj.getString("top_text_color"),
//                            memeObj.getString("bottom_text_color"),
//                            memeObj.getInt("total_komen"),
//                            false,
//                            memeObj.getString("username"),
//                            memeObj.getInt("private"),
//                            memeObj.getInt("day_differ"),
//                            memeObj.getInt("hours_differ")
//                        )
//                        memesList.add(meme)
//                    }
//                    updateList()
//                }
//            }, {
//                Log.e("apiresult", it.message.toString())
//            })
//        q.add(stringRequest)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}