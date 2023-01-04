package com.example.dailymemedigest

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.nvt.color.ColorPickerDialog
import kotlinx.android.synthetic.main.activity_add_new_meme.*
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.fragment_setting.editColorText

class AddNewMeme : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_meme)

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
                    }
                })
            colorPicker.show()
        }
    }
}