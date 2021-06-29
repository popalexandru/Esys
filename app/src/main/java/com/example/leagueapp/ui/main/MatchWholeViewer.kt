package com.example.leagueapp.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.leagueapp.R
import com.example.leagueapp.adapters.MatchViewerAdapter
import com.example.leagueapp.adapters.ViewPagerAdapter
import com.example.leagueapp.databinding.LayoutGameViewerBinding
import com.example.leagueapp.databinding.MatchWholeViewerBinding
import com.example.leagueapp.networking.model.Utils.Utility
import com.example.leagueapp.networking.model.models.*
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchWholeViewer() : Fragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setHasOptionsMenu(true)
    }

    private var _binding : MatchWholeViewerBinding? = null
    private val binding get() = _binding!!

    private lateinit var receivedSummoner: Summoner
    private var summonerExists : Boolean = false
    private lateinit var receivedMatch : Match
    private lateinit var myName : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MatchWholeViewerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val match = arguments?.getSerializable("match") as Match
        val name = arguments?.getString("name")
        val region = arguments?.getString("region")

        receivedMatch = match


        binding.viewPager.apply {
            if(name != null && region != null)
                adapter = ViewPagerAdapter(requireParentFragment(), match, name, region)
            setCurrentItem(0, true)
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Overview"
                }
                1 -> {
                    tab.text = "Damage dealt"
                }
                2 -> {
                    tab.text = "Vision"
                }
                3 -> {
                    tab.text = "Gold"
                }

                4 -> {
                    tab.text = "CS"
                }

                5 -> {
                    tab.text = "Damage taken"
                }
            }
        }).attach()

        setTimeAndDate(match)
    }

    private fun setTimeAndDate(match: Match) {
        binding.gameDuration.setText(match.duration)
        binding.matchTime.setText(Utility.getTimeAgo(match.gameCreation))
        binding.gameType.setText(match.queue.getName())
    }
}