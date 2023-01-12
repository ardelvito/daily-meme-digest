package com.example.dailymemedigest

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_new_meme.*
import kotlinx.android.synthetic.main.activity_edit_photo_profile.*
import kotlinx.android.synthetic.main.activity_edit_photo_profile.btnBackEdit
import kotlinx.android.synthetic.main.activity_edit_photo_profile.editPhotoProfile
import kotlinx.android.synthetic.main.fragment_setting.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.HashMap

class EditPhotoProfile : AppCompatActivity() {
    lateinit var preferences: SharedPreferences
    var encodeImageString = ""
    val REQUEST_IMAGE_CAPTURE = 2

    fun takePicture() {
        val i = Intent()
        i.action = MediaStore.ACTION_IMAGE_CAPTURE
        startActivityForResult(i, REQUEST_IMAGE_CAPTURE)
    }

//    fun takePicture(){
//        val i = Intent().apply {
//            action = MediaStore.ACTION_IMAGE_CAPTURE
//
//        }
//        startActivityForResult(i, REQUEST_IMAGE_CAPTURE)
//    }

    fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        encodeImageString = Base64.encodeToString(b, Base64.DEFAULT)
        return Base64.encodeToString(b, Base64.DEFAULT)
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

        // Camera
        fabPhotoCam.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA), REQUEST_IMAGE_CAPTURE)
            } else {
                takePicture()
            }
        }

        // Gallery
        btnOpenGallery.setOnClickListener{
            Dexter.withActivity(this)
                .withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener{

                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        val intent = Intent(Intent.ACTION_PICK)
                        intent.type = "image/*"
                        startActivityForResult(Intent.createChooser(intent, "Browse Image"), 1)
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {

                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                }).check()
        }

        btnSaveChange.setOnClickListener{
            uploadImage()
        }

        btnBackEdit.setOnClickListener{
            finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePicture()
                }
                else {
                    Toast.makeText(this, "You must grant permission to access the camera.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun uploadImage() {
        val sharedName = this.packageName
        preferences = this.getSharedPreferences(sharedName, Context.MODE_PRIVATE)
        val id_user = preferences.getInt(Login.SHARED_PLAYER_ID, 0)
        val url = "https://ubaya.fun/native/160420024/memes_api/fileuploadprofile.php"

        val stringRequest = object: StringRequest(
            Request.Method.POST,
            url,
            Response.Listener {
                Log.d("Params", it.toString())
                Toast.makeText(applicationContext,"Berhasil", Toast.LENGTH_LONG).show()

            },
            Response.ErrorListener {
                Toast.makeText(applicationContext,"Gagal", Toast.LENGTH_LONG).show()

            }
        )
        {
            override fun getParams(): MutableMap<String, String>? {
                val map = HashMap<String, String>()
                map.put("user_id",id_user.toString())
                map.put("upload", encodeImageString)
                Log.d("image string", encodeImageString)
//                Log.d("MAP", map.toString())
                return map
            }
        }
        val q = Volley.newRequestQueue(this)
        q.add(stringRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("result ok", requestCode.toString() + REQUEST_IMAGE_CAPTURE.toString())
        if(resultCode == RESULT_OK && requestCode == 1){
//            val extras = data?.extras
//            extras?.let{
//                val imageBitmap = it.get("data") as Bitmap
//                editPhotoProfile.setImageBitmap(imageBitmap)
//                encodeImage(imageBitmap)
//            }
            val filePath =data?.data
            var img: ImageView = findViewById(R.id.editPhotoProfile)
            Log.d("msg", "test ganti gambar")
            val inputStream: InputStream? = filePath?.let { contentResolver.openInputStream(it) }
            val bitmap= BitmapFactory.decodeStream(inputStream)
            img.setImageBitmap(bitmap)
            encodeImage(bitmap)
        } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
            val extras = data!!.extras
            val imageBitmap: Bitmap = extras!!.get("data") as Bitmap
            editPhotoProfile.setImageBitmap(imageBitmap)
            encodeImage(imageBitmap)
            Log.d("bitmap", imageBitmap.toString())
        }
        else{
            Log.d("msg", "gagal ganti gambar")

        }
    }


}