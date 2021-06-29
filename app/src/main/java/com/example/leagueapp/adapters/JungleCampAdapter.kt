package com.example.leagueapp.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.leagueapp.databinding.JungleTimerLayoutBinding
import com.example.leagueapp.databinding.LayoutGraphItemBinding
import com.example.leagueapp.databinding.LayoutJungleCampBinding
import com.example.leagueapp.databinding.PlayedChampLayoutBinding
import com.example.leagueapp.models.ParticipantTeamPair
import com.example.leagueapp.models.PlayedChampModel
import com.example.leagueapp.networking.model.models.ChampionMastery
import com.example.leagueapp.networking.model.models.JungleCamp
import com.example.leagueapp.networking.model.models.Participant
import com.example.leagueapp.repository.ReadJsonRepository

class JungleCampAdapter() : ListAdapter<JungleCamp, JungleCampAdapter.CakeViewHolder>(
    DiffCallback()
){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CakeViewHolder {
        val binding = LayoutJungleCampBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CakeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CakeViewHolder, position: Int) {
        val currentCake = getItem(position)

        holder.bind(currentCake)
    }

    inner class CakeViewHolder(private val binding : LayoutJungleCampBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(champ : JungleCamp){
            binding.apply {
                campName.setText(champ.jungleCampName)
                campImage.setImageResource(champ.jungleCampImage)
                counter.setText(champ.jungleCampSpawnTime.toString())
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<JungleCamp>() {
        override fun areItemsTheSame(oldItem: JungleCamp, newItem: JungleCamp) =
            oldItem.jungleCampName == newItem.jungleCampName

        override fun areContentsTheSame(oldItem: JungleCamp, newItem: JungleCamp) =
            oldItem == newItem
    }

}