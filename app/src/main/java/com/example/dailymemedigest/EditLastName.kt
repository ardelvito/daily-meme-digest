package com.example.dailymemedigest

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.SwitchCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_edit_first_name.*
import org.json.JSONObject
import java.util.HashMap

class EditLastName : AppCompatActivity() {

    var v: View? = null
    var txtFirstName: TextInputLayout? = null
    var txtLastName: TextInputLayout? = null
    var switchPrivacyStatus: SwitchCompat? = null
    var btnEdit: Button? = null
    var circleImg: ShapeableImageView? = null
    var editFirstName: ImageView? = null
    var editLastName: ImageView? = null


    lateinit var preferences: SharedPreferences

    fun updateLastName(user_id: Int, new_firstname: String){

        val sharedName = this.packageName
        preferences = this.getSharedPreferences(sharedName, Context.MODE_PRIVATE)
        val id_user = preferences.getInt(Login.SHARED_PLAYER_ID, 0)

        val q = Volley.newRequestQueue(this)
        val url = "https://ubaya.fun/native/160420024/memes_api/update_lastname.php"

        val stringRequest = object : StringRequest(
            Request.Method.POST,
            url, Response.Listener{
                Log.d("Cek Parameter Settings", it.toString())
                val obj = JSONObject(it)
                val result = obj.getString("result")
                if(result == "OK"){
                    Log.d("Status", "Berhasil Last Name")
                }
            }

            ,
            {
                Response.ErrorListener{
                    Log.d("Cek Parameter", it.message.toString())
                }
            }
        )
        {
            override fun getParams(): MutableMap<String, String>? {
                val map = HashMap<String, String>()
                map.set("user_id", id_user.toString())
                map.set("new_lastname", new_firstname)
                Log.d("MAP", map.toString())
                return map
            }

        }
        q.add(stringRequest)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_last_name)

        Log.d("Edit Last Name", "Edited")
        val sharedName = this.packageName
        preferences = this.getSharedPreferences(sharedName, Context.MODE_PRIVATE)
        val id_user = preferences.getInt(Login.SHARED_PLAYER_ID, 0)

        val q = Volley.newRequestQueue(this)
        val url = "https://ubaya.fun/native/160420024/memes_api/get_userprofile.php"

        val stringRequest = object : StringRequest(
            Request.Method.POST,
            url, Response.Listener{
                Log.d("Cek Parameter Settings", it.toString())
                val obj = JSONObject(it)
                val result = obj.getString("result")
                if(result == "OK"){
                    Log.d("Status", "Berhasil Baca")
                    val arrData = obj.getJSONArray("data")
                    val userObj = arrData.getJSONObject(0)

                    val lastName = userObj.getString("last_name")

                    txtInputLayoutLastName.editText?.setText(lastName)

                    Log.d("Last Name Edit", lastName)

                }
            }

            ,
            {
                Response.ErrorListener{
                    Log.d("Cek Parameter", it.message.toString())
                }
            }
        )
        {
            override fun getParams(): MutableMap<String, String>? {
                val map = HashMap<String, String>()
                map.set("user_id", id_user.toString())
                Log.d("MAP", map.toString())
                return map
            }

        }
        q.add(stringRequest)

        btnEditLastName.setOnClickListener{
            Log.d("Btn Edit", txtInputEditLastName.text.toString())
            updateLastName(id_user, txtInputEditLastName.text.toString())
        }
    }
}