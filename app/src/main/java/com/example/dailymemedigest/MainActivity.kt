package com.example.dailymemedigest
import android.content.Intent
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.login_page.*

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

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        var drawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.app_name,
                R.string.app_name)
        drawerToggle.isDrawerIndicatorEnabled = true
        drawerToggle.syncState()

        navViewD.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.itemAddNewMemes -> Toast.makeText(this, "Add", Toast.LENGTH_SHORT).show()
                R.id.itemHome -> Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                R.id.itemMyCreation -> Toast.makeText(this, "MyCreation", Toast.LENGTH_SHORT).show()
                R.id.itemLeaderboard -> Toast.makeText(this, "LDB", Toast.LENGTH_SHORT).show()
                R.id.itemSetting -> Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show()
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
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



