package com.example.leagueapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.leagueapp.R
import com.example.leagueapp.adapters.GridViewForbuilds
import com.example.leagueapp.databinding.FragmentBuildsBinding
import com.example.leagueapp.models.ChampionData
import com.example.leagueapp.repository.ReadJsonRepository
import com.google.android.material.snackbar.Snackbar

class BuildsFragment : Fragment(), GridViewForbuilds.onDeleteClick{
    private var _binding: FragmentBuildsBinding? = null
    private val binding get() = _binding!!
    private val viewModel : MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBuildsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = ReadJsonRepository.championsData

        val buildsAdapter =  GridViewForbuilds(this)
        binding.recyclerView.layoutManager = GridLayoutManager(context, 4)
        binding.recyclerView.hasFixedSize()

        binding.recyclerView.adapter = buildsAdapter

        buildsAdapter.submitList(data)

    }

    override fun onNewsClicked(item: ChampionData) {
        val bundle = bundleOf("champ" to item)
        findNavController().navigate(R.id.action_buildsFragment_to_buildViewFragment, bundle)
    }
}