package com.example.dailymemedigest

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.nvt.color.ColorPickerDialog
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_new_meme.*
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.fragment_setting.editColorText

class AddNewMeme : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_meme)

        var sharedName = packageName
        var shared = getSharedPreferences(sharedName, Context.MODE_PRIVATE)
        var id_shared = shared.getInt(Login.SHARED_PLAYER_ID, 0)
        var color = "white"
        btnAddNewMeme.setOnClickListener{
            val url:String = txtInputEditUrlMeme.text.toString()
            val topText:String =txtInputEditTopText.text.toString()
            val botText:String = txtInputEditBottomText.text.toString()
            val q = Volley.newRequestQueue(it.context)
            val url_vol = "https://ubaya.fun/native/160420024/memes_api/add_memes.php"
            val stringRequest = object : StringRequest(
                Request.Method.POST,
                url_vol, {
                    Log.d("addNewMemes", it)
                }, {
                    Log.e("error add new memes", it.message.toString())
                })
            {
                override fun getParams(): MutableMap<String, String>? {
                    val map = HashMap<String, String>()
                    map.set("url", url)
                    map.set("topText", topText)
                    map.set("botText", botText)
                    map.set("users_id", id_shared.toString())
                    map.set("topTextColor", color)
                    map.set("botTextColor", color)
                    map.set("url", url)
                    return map
                }
            }
            q.add(stringRequest)
            Toast.makeText(this, "Berhasil menambahkan data memes!", Toast.LENGTH_SHORT).show()
        }
        txtInputEditTopText.addTextChangedListener {
            if(txtInputEditTopText.text.toString() != ""){
                txtNewMemeTopText.setText(txtInputEditTopText.text)
            }
        }
        txtInputEditBottomText.addTextChangedListener {
            if(txtInputEditBottomText.text.toString() != ""){
                txtNewMemeBottomText.setText(txtInputEditBottomText.text)
            }
        }

        txtInputEditUrlMeme.addTextChangedListener {
            if(txtInputEditUrlMeme.text.toString() != ""){
                Picasso.get().load(txtInputEditUrlMeme.text.toString())
                    .placeholder(R.drawable.cool_memes)
                    .error(R.drawable.cool_memes)
                    .into(newImgMemes)
            }
        }

        editColorText.setOnClickListener {
            Log.d("Color Picker", "Clicked")
            val colorPicker = ColorPickerDialog(
                this,
                Color.BLACK,
                true,
                object: ColorPickerDialog.OnColorPickerListener{
                    override fun onCancel(dialog: ColorPickerDialog?) {
                        // handle click button Cancel
                    }

                    override fun onOk(dialog: ColorPickerDialog?, colorPicker: Int) {
                        // handle click button OK
                        txtNewMemeBottomText.setTextColor(colorPicker)
                        txtNewMemeTopText.setTextColor(colorPicker)
                        color = "#" + Integer.toHexString(colorPicker).substring(2)
                        Log.d("Color:", color)
                    }
                })
            colorPicker.show()
        }
    }
}