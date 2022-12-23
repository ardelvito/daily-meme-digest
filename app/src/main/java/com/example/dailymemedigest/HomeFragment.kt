package com.example.dailymemedigest

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
import org.json.JSONObject

class HomeFragment : Fragment() {
    var memesList:ArrayList<Meme> = ArrayList()

    fun updateList(){
        val lm:LayoutManager = LinearLayoutManager(activity)
        var recyclerView = view?.findViewById<RecyclerView>(R.id.memesView)
        recyclerView?.layoutManager = lm
        recyclerView?.setHasFixedSize(true)
        recyclerView?.adapter = HomeAdapter(memesList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val q = Volley.newRequestQueue(activity)
        val url = "https://ubaya.fun/native/160420024/memes_api/get_memes.php"
        var stringRequest = StringRequest(
            Request.Method.POST,
            url,
            Response.Listener{
                Log.d("apiresult", it)
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
                            memeObj.getString("bottom_text_color")
                        )
                        memesList.add(meme)
                    }
                    updateList()
                }
            }, Response.ErrorListener{
                Log.e("apiresult", it.message.toString())
            })
        q.add(stringRequest)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}