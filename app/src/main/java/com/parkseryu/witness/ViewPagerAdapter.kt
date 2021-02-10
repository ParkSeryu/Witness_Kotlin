package com.parkseryu.witness

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.parkseryu.witness.ui.home.HomeFragment
import com.parkseryu.witness.ui.snailLetter.SlideshowFragment

class ViewPagerAdapter(fa : FragmentActivity) : FragmentStateAdapter(fa){

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            1 -> HomeFragment()
            2 -> SlideshowFragment()
            else -> HomeFragment()
        }
    }
}

