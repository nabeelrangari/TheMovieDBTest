package com.nabeel.themoviedbtest.view.adapter

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nabeel.themoviedbtest.view.fragment.MovieFragment

class DynamicViewPagerAdapter(
    activity: AppCompatActivity,
    val tabTitle: List<String>
) :
    FragmentStateAdapter(activity) {
    private val context: Context = activity

    override fun getItemCount(): Int {
        return tabTitle.size
    }

    override fun createFragment(position: Int): Fragment {
        return MovieFragment.newInstance(tabTitle[position], context)
    }

}