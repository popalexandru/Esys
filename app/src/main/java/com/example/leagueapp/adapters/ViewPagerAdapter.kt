package com.example.leagueapp.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.leagueapp.networking.model.models.Match
import com.example.leagueapp.ui.main.*

class ViewPagerAdapter(fa : Fragment, private val match : Match, private val name: String, private val region: String) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 6
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> {
                return MatchParticipantsFragment(match, name, region)
            }

            1 -> {
                return DamageToChampionsFragment(match, name)
            }

            2 -> {
                return VisionScoreFragment(match, name)
            }

            3 -> {
                return GoldEarnedFragment(match, name)
            }

            4 -> {

            }

            5 -> {
                return DamageTakenFragment(match, name)
            }

            else -> Unit
        }

        return Fragment()
    }
}