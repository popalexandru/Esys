package com.example.leagueapp.ui.main

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.efaso.admob_advanced_native_recyvlerview.AdmobNativeAdAdapter
import com.example.leagueapp.adapters.GoldEarnAdapter
import com.example.leagueapp.adapters.MatchViewerAdapter
import com.example.leagueapp.adapters.NewsAdapter
import com.example.leagueapp.databinding.GoldEarnedFragmentBinding
import com.example.leagueapp.networking.model.models.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsShowFragment(private val newsList : MutableList<News>) : Fragment(), NewsAdapter.onDeleteClick {
    private var _binding : GoldEarnedFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GoldEarnedFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populateRecyclerViews()
    }

    private fun populateRecyclerViews() {
        val newsAdapter = NewsAdapter(this)

        val admobNativeAdapter = AdmobNativeAdAdapter.Builder.with("ca-app-pub-4479200586800321/6961703752", newsAdapter, "small").adItemInterval(4).build()

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            hasFixedSize()
            adapter = newsAdapter
        }

        newsAdapter.submitList(newsList)
    }

    override fun onNewsClicked(item: News) {
        try {
            val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.URL))
            startActivity(myIntent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }
}