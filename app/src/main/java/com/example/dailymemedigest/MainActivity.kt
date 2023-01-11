package com.example.dailymemedigest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.drawer_header.view.*
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.login_page.*
import org.json.JSONObject
import java.util.HashMap

class MainActivity : AppCompatActivity() {

    companion object{
        val EXTRA_USERNAME = "EXTRA_USERNAME"
    }


    val fragHome: Fragment = HomeFragment()
    val fragLeaderboard: Fragment = LeaderboardFragment()
    val fragCreation: Fragment = CreationFragment()
    val fragSetting: Fragment = SettingFragment()
    val fragManager : FragmentManager = supportFragmentManager
    var mainFragment : Fragment = fragHome

    val fragmentList:ArrayList<Fragment> = ArrayList()

    override fun onResume() {
        super.onResume()

        // Navigation Bar
        var sharedName = "com.example.dailymemedigest"
        var preferences: SharedPreferences = getSharedPreferences(sharedName, Context.MODE_PRIVATE)
        val idUser = preferences.getInt(Login.SHARED_PLAYER_ID, 0)
        val bgDH = "https://ubaya.fun/native/160420024/bg/bg.jpg"
        getDataDrawer(idUser, bgDH)
    }

    private lateinit var menu : Menu
    private lateinit var menuItem : MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setContentView(R.layout.drawer_layout)

//        changeFragment()


        chooseFragment()
        viewPager.registerOnPageChangeCallback(
            object: ViewPager2.OnPageChangeCallback(){
                //anonymous class & object
                override fun onPageSelected(position: Int) {
                    bottomNavigationView.selectedItemId =
                        bottomNavigationView.menu.getItem(position).itemId
                }
            }
        )

        fab_meme.setOnClickListener(){
            Log.d("FAB", "CLICKED")
            val intent = Intent(this, AddNewMeme::class.java)
            this.startActivity(intent)
        }

        // Navigation Bar
        var sharedName = "com.example.dailymemedigest"
        var preferences: SharedPreferences = getSharedPreferences(sharedName, Context.MODE_PRIVATE)
        var editor: SharedPreferences.Editor = preferences.edit()
        val idUser = preferences.getInt(Login.SHARED_PLAYER_ID, 0)
        val bgDH = "https://ubaya.fun/native/160420024/bg/bg.jpg"
        getDataDrawer(idUser, bgDH)

//        supportActionBar?.setDisplayHomeAsUpEnabled(false)
//        var drawerToggle =
//            ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.app_name,
//                R.string.app_name)
//        drawerToggle.isDrawerIndicatorEnabled = true
//        drawerToggle.syncState()

//        navViewD.setNavigationItemSelectedListener {
//            chooseFragment()
//            viewPager.currentItem = when(it.itemId) {
////                R.id.itemAddNewMemes -> 0
//                R.id.itemHome -> 0
//                R.id.itemMyCreation -> 1
//                R.id.itemLeaderboard -> 2
//                R.id.itemSetting -> 3
//                else -> 0
//            }
//            drawerLayout.closeDrawer(GravityCompat.START)
//            true
//        }

        // Logout
        val navViewDH = findViewById<NavigationView>(R.id.navViewD)
        val DH:View = navViewDH.getHeaderView(0)
        val logoutBtn:View = DH.findViewById(R.id.btnLogoutDH)
        logoutBtn.setOnClickListener {
            editor.clear()
            editor.apply()

            //create intent object & determine the activity target
            val intent = Intent(this, Login::class.java)

            //"execute" the object and OS will launch the activity target
            startActivity(intent)
        }
    }

    fun getDataDrawer(user_id: Int, bgDH: String) {
        val q = Volley.newRequestQueue(this)
        val url = "https://ubaya.fun/native/160420024/memes_api/get_userprofile.php"

        val stringRequest = object : StringRequest(
            Request.Method.POST,
            url, Response.Listener{
                Log.d("Cek Parameter Settings", it.toString())
                val obj = JSONObject(it)
                val result = obj.getString("result")
                if(result == "OK"){
                    Log.d("Status", "Berhasil Ambil Data")
                    val arrData = obj.getJSONArray("data")
                    val userObj = arrData.getJSONObject(0)

                    val firstName = userObj.getString("first_name")
                    val lastName = userObj.getString("last_name")
                    val avatarLink = userObj.getString("avatar_link")
                    val username = userObj.getString("username")

                    navViewD.txtUsernameDH.setText(username.toString())
                    navViewD.txtFNameDH.setText(firstName.toString())
                    navViewD.txtLNameDH.setText(lastName.toString())
                    Picasso.get().load(avatarLink).into(navViewD.imgProfileDH)

                    Glide.with(this).load(bgDH)
                        .apply(RequestOptions.bitmapTransform(BlurTransformation(50, 1)))
                        .into(navViewD.imgBackgroundDH)

//                    txtUserName.setText(username.toString())
//                    txtUserFullName.setText(firstName + " " + lastName)
//                    Picasso.get().load(avatarLink).into(circleImgBorder)
//                    txtInputFirstName.editText?.setText(firstName)
//                    txtInputLastName.editText?.setText(lastName)

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

    private fun chooseFragment(){
        fragmentList.add(HomeFragment())
        fragmentList.add(CreationFragment())
        fragmentList.add(LeaderboardFragment())
        fragmentList.add(SettingFragment())

        val frameLayoutAdapter =FrameLayoutAdapter(this,  fragmentList)
        viewPager.adapter = frameLayoutAdapter

        bottomNavigationView.setOnItemSelectedListener{
            if(it.itemId == R.id.navi_home){
                viewPager.currentItem = 0
                Log.d("Fragment", "Home")
            }
            else if (it.itemId == R.id.navi_creation){
                viewPager.currentItem = 1
                Log.d("Fragment", "Creation")
            }
            else if (it.itemId == R.id.navi_leaderboard){
                viewPager.currentItem = 2
                Log.d("Fragment", "Leaderboard")
            }
            else if (it.itemId == R.id.navi_settings){
                viewPager.currentItem = 3
                Log.d("Fragment", "Settings")
            }
            true

        }

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        var drawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.app_name,
                R.string.app_name)
        drawerToggle.isDrawerIndicatorEnabled = true
        drawerToggle.syncState()

        navViewD.setNavigationItemSelectedListener {
            viewPager.currentItem = when(it.itemId) {
//                R.id.itemAddNewMemes -> 0
                R.id.itemHome -> 0
                R.id.itemMyCreation -> 1
                R.id.itemLeaderboard -> 2
                R.id.itemSetting -> 3
                else -> 0
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

    }
    //force close kalau ganti fragment bolak-balik (kalau transaction di dalam R.id)
    //fragment ketumpuk kalau semua transaction di awal
//    private fun changeFragment(){
//        fragManager.beginTransaction().add(R.id.fragmentContent, fragHome).show(fragHome).commit()
//
//
//
//        menu = bottomNavigationView.menu
////        menuItem = menu.getItem(0)
////        menuItem.isChecked = true
//
//        bottomNavigationView.setOnNavigationItemSelectedListener {
//            item->
//            when (item.itemId){
//                R.id.navi_home->{
//                    callFragment(0, fragHome)
//                }
//                R.id.navi_creation->{
//                    fragManager.beginTransaction().add(R.id.fragmentContent, fragCreation).show(fragCreation).commit()
//                    callFragment(1, fragCreation)
//                }
//                R.id.navi_leaderboard->{
//                    fragManager.beginTransaction().add(R.id.fragmentContent, fragLeaderboard).show(fragLeaderboard).commit()
//                    callFragment(2, fragLeaderboard)
//                }
//                R.id.navi_settings->{
//                    fragManager.beginTransaction().add(R.id.fragmentContent, fragSetting).show(fragSetting).commit()
//                    callFragment(3, fragSetting)
//                }
//            }
//            false
//        }
//
//
//
//    }
//
//    private fun callFragment(i:Int, frag: Fragment){
//        menuItem = menu.getItem(i)
//
//
//        fragManager.beginTransaction().hide(mainFragment).show(frag).commit()
//        mainFragment = frag
//    }
}



