package com.example.dailymemedigest

//import android.app.DownloadManager.Request
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        btnLogRegis.setOnClickListener{
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
        }

        btnLogLogin.setOnClickListener{
            val username:String = txtLoginUsername.text.toString()
            val password = txtLoginPassword.text.toString()
            Log.d("USERNAME", username.toString())
            Log.d("PASSWORD", password.toString())

            val q = Volley.newRequestQueue(it.context)
            val url = "https://ubaya.fun/native/160420024/memes_api/user_login.php"
            var result = ""
            val stringRequest =object:StringRequest(
                Request.Method.POST,
                url, Response.Listener {
                    Log.d("cekparams", it)
//                    val obj = JSONObject(it)
                    Log.d("IT", it.toString())
//                    result = obj.getString("result")
//                    if(result == "Success"){
//                        Log.d("Succcess", "Login")
//                    }
//                    else{
//                        Log.d("Failed", "Login")
//                    }
//                    Toast.makeText(this, result, Toast.LENGTH_SHORT).show()

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
                    return map
                }
            }
            q.add(stringRequest)
        }
    }
}