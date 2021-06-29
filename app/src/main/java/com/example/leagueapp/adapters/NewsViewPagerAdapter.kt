package com.example.leagueapp.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.leagueapp.networking.model.models.Match
import com.example.leagueapp.networking.model.models.News
import com.example.leagueapp.ui.main.*

class NewsViewPagerAdapter(fa : Fragment, private val patchNews : MutableList<News>, private val champReveal : MutableList<News>, private val skinReleaseNews : MutableList<News>, private val generalNews : MutableList<News>) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> {
                return NewsShowFragment(generalNews)
            }

            1 -> {
                return NewsShowFragment(patchNews)
            }

            2 -> {
                return NewsShowFragment(skinReleaseNews)
            }

            3 -> {
                return NewsShowFragment(champReveal)
            }

            else -> Unit
        }

        return Fragment()
    }
}