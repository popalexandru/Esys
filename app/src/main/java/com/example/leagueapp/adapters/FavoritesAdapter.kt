package com.example.leagueapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.leagueapp.database.FavoriteItem
import com.example.leagueapp.databinding.FavoriteChampLayoutBinding
import com.example.leagueapp.databinding.PlayedChampLayoutBinding
import com.example.leagueapp.models.PlayedChampModel
import com.example.leagueapp.networking.model.models.ChampionMastery
import com.example.leagueapp.repository.ReadJsonRepository

class
FavoritesAdapter(private val listener : onDeleteClick) : ListAdapter<FavoriteItem, FavoritesAdapter.CakeViewHolder>(
    DiffCallback()
){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CakeViewHolder {
        val binding = FavoriteChampLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CakeViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: CakeViewHolder, position: Int) {
        val currentCake = getItem(position)

        holder.layout.setOnClickListener {
            if(position != RecyclerView.NO_POSITION){
                val cake = getItem(position)
                listener.onSummonerClicked(cake)
            }
        }

        holder.bind(currentCake)
    }

    inner class CakeViewHolder(private val binding : FavoriteChampLayoutBinding, private val listener : onDeleteClick) : RecyclerView.ViewHolder(binding.root){
        val layout = binding.cardView

        fun bind(champ : FavoriteItem){
            binding.apply {
                summonerName.setText(champ.name)
                champImage.load("https://raw.communitydragon.org/latest/game/assets/ux/summonericons/profileicon${champ.profileIconId}.png")
                delete.setOnClickListener {
                    listener.deleteSummoner(champ)
                }

                region.setText(champ.regionName.substring(0, 2).toUpperCase())
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<FavoriteItem>() {
        override fun areItemsTheSame(oldItem: FavoriteItem, newItem: FavoriteItem) =
            oldItem.accountId == newItem.accountId

        override fun areContentsTheSame(oldItem: FavoriteItem, newItem: FavoriteItem) =
            oldItem == newItem
    }

    interface onDeleteClick{
        fun deleteSummoner(item: FavoriteItem)
        fun onSummonerClicked(item : FavoriteItem)
    }
}