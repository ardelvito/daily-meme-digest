package com.example.dailymemedigest

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class CreationFragment : Fragment() {

    var memesList:ArrayList<Meme> = ArrayList()
    lateinit var preferences: SharedPreferences
    var username: String = ""
    var user_id: Int = 0 //temp

    fun updateList(){
        getUser()
        val lm:LinearLayoutManager = LinearLayoutManager(activity)
        val rv = view?.findViewById<RecyclerView>(R.id.creationView)
        rv?.layoutManager = lm
        rv?.setHasFixedSize(true)
        rv?.adapter = CreationAdapter(memesList, user_id)
    }
    fun getUser(){
        val sharedName = this.activity?.packageName
        preferences = this.requireActivity().getSharedPreferences(sharedName, Context.MODE_PRIVATE)
        val id_user = preferences.getInt(Login.SHARED_PLAYER_ID, 0)
        user_id = id_user
    }

    override fun onResume() {
        super.onResume()

        getUser()
        memesList.clear()
        getMemes()
    }

    fun getMemes() {
        val q = Volley.newRequestQueue(activity)
        val url = "https://ubaya.fun/native/160420024/memes_api/get_memes.php"

        var stringRequest = object: StringRequest(
            Request.Method.POST,
            url,
            {
                val obj = JSONObject(it)
                Log.d("Samting", user_id.toString())

                if(obj.getString("result") == "OK"){
                    val data = obj.getJSONArray("data")
                    for(i in 0 until data.length()){

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
        {
            override fun getParams(): MutableMap<String, String>? {
                val map = HashMap<String, String>()
                map.set("user_id", user_id.toString())
                return map
            }
        }
        q.add(stringRequest)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        getUser()
//        memesList.clear()

//        val q = Volley.newRequestQueue(activity)
//        val url = "https://ubaya.fun/native/160420024/memes_api/get_memes.php"
//
//        var stringRequest = object: StringRequest(
//            Request.Method.POST,
//            url,
//            {
//                val obj = JSONObject(it)
//                Log.d("Samting", user_id.toString())
//
//                if(obj.getString("result") == "OK"){
//                    val data = obj.getJSONArray("data")
//                    for(i in 0 until data.length()){
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
//        {
//            override fun getParams(): MutableMap<String, String>? {
//                val map = HashMap<String, String>()
//                map.set("user_id", user_id.toString())
//                return map
//            }
//        }
//        q.add(stringRequest)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_creation, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreationFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}