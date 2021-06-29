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
import com.example.leagueapp.Constants
import com.example.leagueapp.Constants.CHAMP_REVEAL
import com.example.leagueapp.Constants.CHAMP_SPOTLIGHT
import com.example.leagueapp.Constants.CHAMP_UPDATE
import com.example.leagueapp.Constants.PATCH_NOTES
import com.example.leagueapp.Constants.PATCH_NOTES_TFT
import com.example.leagueapp.Constants.SKIN_RELEASE
import com.example.leagueapp.R
import com.example.leagueapp.adapters.MatchViewerAdapter
import com.example.leagueapp.adapters.NewsViewPagerAdapter
import com.example.leagueapp.adapters.ViewPagerAdapter
import com.example.leagueapp.databinding.LayoutGameViewerBinding
import com.example.leagueapp.databinding.MatchWholeViewerBinding
import com.example.leagueapp.databinding.NewsFragmentBinding
import com.example.leagueapp.networking.model.Utils.Utility
import com.example.leagueapp.networking.model.models.*
import com.example.leagueapp.networking.model.repository.NewsRepository
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class NewsFragment() : Fragment() {
    private var _binding: NewsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = NewsFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newsList = NewsRepository.news

        val patchNotesNews = mutableListOf<News>()
        val patchNotesTFTNews = mutableListOf<News>()
        val champRevealNews = mutableListOf<News>()
        val champSpotlightNews = mutableListOf<News>()
        val champUpdateNews = mutableListOf<News>()
        val skinReleaseNews = mutableListOf<News>()
        val generalNews = mutableListOf<News>()

        for (newsItem in newsList) {
            if (newsItem.termIds.contains(PATCH_NOTES)) {
                patchNotesNews.add(newsItem)
            } else if (newsItem.termIds.contains(PATCH_NOTES_TFT)) {
                patchNotesTFTNews.add(newsItem)
            } else if (newsItem.termIds.contains(CHAMP_REVEAL)) {
                champRevealNews.add(newsItem)
            } else if (newsItem.termIds.contains(CHAMP_SPOTLIGHT)) {
                champSpotlightNews.add(newsItem)
            } else if (newsItem.termIds.contains(CHAMP_UPDATE)) {
                champUpdateNews.add(newsItem)
            } else if (newsItem.termIds.contains(SKIN_RELEASE)) {
                skinReleaseNews.add(newsItem)
            }else{
                generalNews.add(newsItem)
            }
        }

        Timber.d(generalNews.size.toString())

        binding.viewPager.apply {
            adapter = NewsViewPagerAdapter(requireParentFragment(), patchNotesNews, champRevealNews, skinReleaseNews, generalNews)
            setCurrentItem(0, true)
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            when (position) {
                1 -> {
                    tab.text = "Patch notes"
                }
                3 -> {
                    tab.text = "Champ reveal"
                }
                2 -> {
                    tab.text = "Skin reveal"
                }
                0 -> {
                    tab.text = "General"
                }
            }
        }).attach()
    }
}