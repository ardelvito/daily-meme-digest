package com.example.dailymemedigest

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ToggleButton
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.fragment_setting.view.*
import kotlinx.android.synthetic.main.login_page.view.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.memes_card.view.*
import org.json.JSONObject
import java.net.URL
import java.util.*

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
    var togglePrivacy: ToggleButton? = null
    var btnEdit: Button? = null

    lateinit var preferences: SharedPreferences



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

                    Picasso.get().load(avatarLink).into(circleImgBorder)
//                    imageView2.setImageDrawable()
                    txtInputFirstName.editText?.setText(firstName)
                    txtInputLastName.editText?.setText(lastName)
                    Log.d("First Name", firstName)


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
        togglePrivacy = v?.findViewById(R.id.togglePrivacy)
        btnEdit = v?.findViewById(R.id.btnEditProfile)

        txtFirstName?.isEnabled = false
        txtLastName?.isEnabled = false
        togglePrivacy?.isEnabled = false

//        val sharedName = this.activity?.packageName
//        preferences = this.requireActivity().getSharedPreferences(sharedName, Context.MODE_PRIVATE)
//        val id_user = preferences.getInt(Login.SHARED_PLAYER_ID, 0)





        btnEdit?.setOnClickListener{
            Log.d("Btn Edit", "Clicked")
//            Log.d("ID User", id_user.toString())
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