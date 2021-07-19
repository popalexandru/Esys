package com.example.leagueapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.leagueapp.databinding.GridBuildsRowBinding
import com.example.leagueapp.databinding.NewsitemBinding
import com.example.leagueapp.models.ChampionData
import com.example.leagueapp.networking.model.models.News

class GridViewForbuilds(private val listener : onDeleteClick) : ListAdapter<ChampionData, GridViewForbuilds.CakeViewHolder>(
    DiffCallback()
){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CakeViewHolder {
        val binding = GridBuildsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CakeViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: CakeViewHolder, position: Int) {
        val currentCake = getItem(position)

        holder.layout.setOnClickListener {
            if(position != RecyclerView.NO_POSITION){
                val cake = getItem(position)
                listener.onNewsClicked(cake)
            }
        }

        holder.bind(currentCake)
    }

    inner class CakeViewHolder(private val binding : GridBuildsRowBinding, private val listener : onDeleteClick) : RecyclerView.ViewHolder(binding.root){
        val layout = binding.layout
        fun bind(champ : ChampionData){
            binding.apply {
                imgChampIcon.load("https://raw.communitydragon.org/pbe/plugins/rcp-be-lol-game-data/global/default/v1/champion-icons/${champ.key}.png")
                champName.text = champ.id
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ChampionData>() {
        override fun areItemsTheSame(oldItem: ChampionData, newItem: ChampionData) =
            oldItem.id == newItem.id

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: ChampionData, newItem: ChampionData) =
            oldItem == newItem
    }

    interface onDeleteClick{
        fun onNewsClicked(item : ChampionData)
    }

}