package com.example.dailymemedigest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_registration.*
import com.android.volley.Request
import java.util.Objects

class Registration : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        val intent = Intent(this, Login::class.java)

        btnCreate.setOnClickListener{
            val username:String = txtUsername.text.toString()
            val password:String = txtPassword.text.toString()
            val firstName:String = txtFirstName.text.toString()
            val lastName:String = txtLastName.text.toString()
            var private = 0
            if(cbPrivate.isChecked){
                private = 1
            }

            if(txtPassword.text.toString() != txtRepeatPassword.text.toString()){
//                Toast.makeText(this, "Password does not match!", Toast.LENGTH_LONG).show();
            }
            else{
                val q = Volley.newRequestQueue(it.context)
                val url = "https://ubaya.fun/native/160420024/memes_api/user_regis.php"

                val stringRequest = object: StringRequest(
                    Request.Method.POST,
                    url,{
                        Log.d("cekparams", it)
                    },{
                        Log.e("cekparams", it.message.toString())
                    })
                {
                    override fun getParams(): MutableMap<String, String>? {
                        val map =HashMap<String, String>()
                        map.set("username", username)
                        map.set("password", password)
                        map.set("firstName", firstName)
                        map.set("lastName", lastName)
                        map.set("private", private.toString())
                        return map
                    }
                }
                q.add(stringRequest)
                Toast.makeText(this, "Berhasil menambahkan data! Silahkan melakukan login dengan akun yang telah dibuat", Toast.LENGTH_SHORT).show()
                startActivity(intent)
            }



        }
        btnBackLogin.setOnClickListener{
            startActivity(intent)
        }
    }
}