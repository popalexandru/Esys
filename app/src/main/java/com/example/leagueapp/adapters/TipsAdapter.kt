package com.example.leagueapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.leagueapp.R
import com.example.leagueapp.database.FavoriteItem
import com.example.leagueapp.databinding.FavoriteChampLayoutBinding
import com.example.leagueapp.databinding.PlayedChampLayoutBinding
import com.example.leagueapp.databinding.TipsLayoutBinding
import com.example.leagueapp.models.PlayedChampModel
import com.example.leagueapp.networking.model.models.ChampionMastery
import com.example.leagueapp.repository.ReadJsonRepository

class
TipsAdapter(private val isCon: Boolean) : ListAdapter<String, TipsAdapter.CakeViewHolder>(
    DiffCallback()
){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CakeViewHolder {
        val binding = TipsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CakeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CakeViewHolder, position: Int) {
        val currentCake = getItem(position)

        holder.bind(currentCake)
    }

    inner class CakeViewHolder(private val binding : TipsLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(champ : String){
            binding.apply {
                mainText.setText(champ)
                if(isCon){
                    /*image.setImageResource(R.drawable.target_red)*/
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem
    }
}