package com.example.dailymemedigest

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_photo_profile.*
import kotlinx.android.synthetic.main.activity_edit_photo_profile.editPhotoProfile
import kotlinx.android.synthetic.main.fragment_setting.*
import org.json.JSONObject
import java.util.HashMap

class EditPhotoProfile : AppCompatActivity() {
    lateinit var preferences: SharedPreferences
    val REQUEST_IMAGE_CAPTURE = 3
    fun takePicture(){
        val i = Intent().apply {
            action = MediaStore.ACTION_IMAGE_CAPTURE

        }
        startActivityForResult(i, REQUEST_IMAGE_CAPTURE)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_photo_profile)

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

                    val avatarLink = userObj.getString("avatar_link")

                    Picasso.get().load(avatarLink).into(editPhotoProfile)
                    Log.d("Link", avatarLink)

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


        btnPickCamera.setOnClickListener{
            Log.d("Pick", "Camera")
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)!=
                PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),
                    REQUEST_IMAGE_CAPTURE)
            }
            else{
                takePicture()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_IMAGE_CAPTURE){
            val extras = data?.extras
            extras?.let{
                val imageBitmap = it.get("data") as Bitmap
                editPhotoProfile.setImageBitmap(imageBitmap)
            }
        }
    }


}