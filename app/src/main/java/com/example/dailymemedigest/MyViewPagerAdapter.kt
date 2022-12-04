package com.example.dailymemedigest

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyViewPagerAdapter(val activity: AppCompatActivity,
                         val fragments: ArrayList<Fragment>)
    : FragmentStateAdapter(activity) {


    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        //membuat fragment berdasarkan index tertentu

        return fragments[position]
    }

}