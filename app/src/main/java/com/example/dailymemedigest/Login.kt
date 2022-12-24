package com.example.dailymemedigest

//import android.app.DownloadManager.Request
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.login_page.*
import com.android.volley.Request
import com.android.volley.Response
import kotlinx.android.synthetic.main.activity_registration.*
import org.json.JSONObject

class Login : AppCompatActivity() {

    companion object{
        val SHARED_PLAYER_USERNAME = "SHARED_PLAYER_USERNAME"
        val SHARED_PLAYER_ID = "SHARED_PLAYER_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        //Pengecekan untuk apakah user pernah login atau tidak pada device ini
        var sharedName = packageName
        var shared = getSharedPreferences(sharedName, Context.MODE_PRIVATE)
        var username_shared = shared.getString(SHARED_PLAYER_USERNAME, null)
        var id_shared = shared.getInt(SHARED_PLAYER_ID, 0)
        val intent = Intent(this, MainActivity::class.java)
//        Toast.makeText(this, username_shared, Toast.LENGTH_SHORT).show()
        if(id_shared != null){
            startActivity(intent)
        }


        btnLogRegis.setOnClickListener{
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
        }

        btnLogLogin.setOnClickListener{
            val username:String = txtLoginUsername.text.toString()
            val password = txtLoginPassword.text.toString()

            val q = Volley.newRequestQueue(it.context)
            val url = "https://ubaya.fun/native/160420024/memes_api/user_login.php"
            val stringRequest =object:StringRequest(
                Request.Method.POST,
                url, Response.Listener {
                    Log.d("cekparams", it)
                    Log.d("IT", it.toString())
                    val obj = JSONObject(it)
                    val result = obj.getString("result")
                    val message = obj.getString("message")
                    if(result == "Success"){
                        val userId = obj.getInt("user_id")
                        var editor = shared.edit()
                        editor.putString(SHARED_PLAYER_USERNAME, username)
                        editor.putInt(SHARED_PLAYER_ID, userId)
                        editor.apply()
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }

                }
                , Response.ErrorListener{
                    Log.d("cekparams", it.message.toString())
                }
            )
            {
                override fun getParams(): MutableMap<String, String>? {
                    val map = HashMap<String, String>()
                    map.set("username", username)
                    map.set("password", password)
                    Log.d("MAP", map.toString())
                    return map
                }
            }
            q.add(stringRequest)
        }
    }
}