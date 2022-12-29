package com.example.dailymemedigest

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ToggleButton
import androidx.appcompat.widget.SwitchCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.fragment_setting.view.*
import kotlinx.android.synthetic.main.login_page.view.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.memes_card.view.*
import org.json.JSONObject
import java.net.URL
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class SettingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var v: View? = null
    var txtFirstName: TextInputLayout? = null
    var txtLastName: TextInputLayout? = null
    var switchPrivacyStatus: SwitchCompat? = null
    var btnEdit: Button? = null
    var circleImg: ShapeableImageView? = null
    var editFirstName: ImageView? = null
    var editLastName: ImageView? = null


    lateinit var preferences: SharedPreferences



    fun refresh(user_id: Int){
        val q = Volley.newRequestQueue(this.activity)
        val url = "https://ubaya.fun/native/160420024/memes_api/get_userprofile.php"

        val stringRequest = object : StringRequest(
            Request.Method.POST,
            url, Response.Listener{
                Log.d("Cek Parameter Settings", it.toString())
                val obj = JSONObject(it)
                val result = obj.getString("result")
                if(result == "OK"){
                    Log.d("Status", "Berhasil Refresh")
                    val arrData = obj.getJSONArray("data")
                    val userObj = arrData.getJSONObject(0)

                    val firstName = userObj.getString("first_name")
                    val lastName = userObj.getString("last_name")
                    val registDate = userObj.getString("regis_date")
                    val avatarLink = userObj.getString("avatar_link")
                    val private = userObj.getInt("private")
                    val username = userObj.getString("username")
                    val dateObj = arrData.getJSONObject(1)

                    txtUserName.setText(username.toString())
                    txtUserFullName.setText(firstName + " " + lastName)
                    Picasso.get().load(avatarLink).into(circleImgBorder)
                    txtInputFirstName.editText?.setText(firstName)
                    txtInputLastName.editText?.setText(lastName)

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
                map.set("user_id", user_id.toString())
                Log.d("MAP", map.toString())
                return map
            }

        }
        q.add(stringRequest)
    }

    fun updatePrivacy(user_id: Int){

        val sharedName = this.activity?.packageName
        preferences = this.requireActivity().getSharedPreferences(sharedName, Context.MODE_PRIVATE)
        val id_user = preferences.getInt(Login.SHARED_PLAYER_ID, 0)

        val q = Volley.newRequestQueue(activity)
        val url = "https://ubaya.fun/native/160420024/memes_api/set_privacystatus.php"

        val stringRequest = object : StringRequest(
            Request.Method.POST,
            url, Response.Listener{
                Log.d("Cek Parameter Settings", it.toString())
                val obj = JSONObject(it)
                val result = obj.getString("result")
                if(result == "OK"){
                    Log.d("Status", "Berhasil Update Status Private")
                    val updatedStatus = obj.getInt("status")
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
    }

    override fun onStart() {
        super.onStart()
        val sharedName = this.activity?.packageName
        preferences = this.requireActivity().getSharedPreferences(sharedName, Context.MODE_PRIVATE)
        val id_user = preferences.getInt(Login.SHARED_PLAYER_ID, 0)
        refresh(id_user)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        val sharedName = this.activity?.packageName
        preferences = this.requireActivity().getSharedPreferences(sharedName, Context.MODE_PRIVATE)
        val id_user = preferences.getInt(Login.SHARED_PLAYER_ID, 0)

        val q = Volley.newRequestQueue(activity)
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

                    val firstName = userObj.getString("first_name")
                    val lastName = userObj.getString("last_name")
                    val registDate = userObj.getString("regis_date")
                    val avatarLink = userObj.getString("avatar_link")
                    val private = userObj.getInt("private")
                    val username = userObj.getString("username")
                    val dateObj = arrData.getJSONObject(1)

                    val month = dateObj.getString("month")
                    val year = dateObj.getString("year")

//                    Log.d("Date", month.toString() + year.toString())
                    txtUserFullName.setText(firstName.toString() + " " + lastName.toString())
                    txtActiveDate.setText("Active Since " + month.toString() + " " + year.toString())
                    txtUserName.setText(username.toString())

                    Picasso.get().load(avatarLink).into(circleImgBorder)
                    txtInputFirstName.editText?.setText(firstName)
                    txtInputLastName.editText?.setText(lastName)
                    if(private == 0){
                        Log.d("Privacy", "ON")
                        switchPrivacyStatus?.isChecked = true
                    }
                    else{
                        Log.d("Privacy", "OFF")
                        switchPrivacyStatus?.isChecked = false
                    }
                    Log.d("First Name", firstName)
                    Log.d("Last Name", lastName)
                    Log.d("id user", id_user.toString())
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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_setting, container, false)

        txtFirstName = v?.findViewById(R.id.txtInputFirstName)
        txtLastName = v?.findViewById(R.id.txtInputLastName)
        switchPrivacyStatus = v?.findViewById(R.id.switchPrivacy)
        btnEdit = v?.findViewById(R.id.btnEditProfile)
        circleImg = v?.findViewById(R.id.circleImgBorder)
        editFirstName = v?.findViewById(R.id.imgEditFirstName)
        editLastName = v?.findViewById(R.id.imgEditLastName)

        txtFirstName?.isEnabled = false
        txtLastName?.isEnabled = false


        val sharedName = this.activity?.packageName
        preferences = this.requireActivity().getSharedPreferences(sharedName, Context.MODE_PRIVATE)
        val id_user = preferences.getInt(Login.SHARED_PLAYER_ID, 0)

        switchPrivacyStatus?.setOnClickListener{
            if(switchPrivacyStatus?.isChecked == true){
                Log.d("Switch", "ON->akun menjadi private")
                updatePrivacy(id_user)
            }
            else{
                Log.d("Switch", "OFF->akun menjadi public")
                updatePrivacy(id_user)
            }
        }

        editFirstName?.setOnClickListener {
            Log.d("First Name", "Edit")
            val intent = Intent(activity, EditFirstName::class.java)
            activity?.startActivity(intent)


        }

        editLastName?.setOnClickListener {
            Log.d("Last Name", "Edit")
            val intent = Intent(activity, EditLastName::class.java)
            activity?.startActivity(intent)
        }

        circleImg?.setOnClickListener{
            Log.d("IMG", "Clicked")
        }

        return v
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}