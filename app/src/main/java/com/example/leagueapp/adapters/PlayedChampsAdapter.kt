package com.example.leagueapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.leagueapp.databinding.PlayedChampLayoutBinding
import com.example.leagueapp.models.PlayedChampModel
import com.example.leagueapp.networking.model.models.ChampionMastery
import com.example.leagueapp.repository.ReadJsonRepository

class PlayedChampsAdapter() : ListAdapter<PlayedChampModel, PlayedChampsAdapter.CakeViewHolder>(
    DiffCallback()
){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CakeViewHolder {
        val binding = PlayedChampLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CakeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CakeViewHolder, position: Int) {
        val currentCake = getItem(position)

        holder.bind(currentCake)
    }

    inner class CakeViewHolder(private val binding : PlayedChampLayoutBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(champ : PlayedChampModel){
            binding.apply {
                points.text = champ.points.toString()
                //champName.text = ReadJsonRepository.getChampNameById(champ.championId.toString())
                champName.text = champ.champName
                champImage.load("https://raw.communitydragon.org/pbe/plugins/rcp-be-lol-game-data/global/default/v1/champion-icons/${champ.id}.png")
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<PlayedChampModel>() {
        override fun areItemsTheSame(oldItem: PlayedChampModel, newItem: PlayedChampModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PlayedChampModel, newItem: PlayedChampModel) =
            oldItem == newItem
    }

}