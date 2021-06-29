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
import com.example.leagueapp.databinding.LayoutGraphItemBinding
import com.example.leagueapp.databinding.PlayedChampLayoutBinding
import com.example.leagueapp.models.ParticipantTeamPair
import com.example.leagueapp.models.PlayedChampModel
import com.example.leagueapp.networking.model.models.ChampionMastery
import com.example.leagueapp.networking.model.models.Participant
import com.example.leagueapp.repository.ReadJsonRepository

class GoldEarnAdapter(private val id : Int) : ListAdapter<Participant, GoldEarnAdapter.CakeViewHolder>(
    DiffCallback()
){
    var myTeam = 0
    var maxValue = 0L

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CakeViewHolder {
        val binding = LayoutGraphItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CakeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CakeViewHolder, position: Int) {
        val currentCake = getItem(position)

        holder.bind(currentCake)
    }

    inner class CakeViewHolder(private val binding : LayoutGraphItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(champ : Participant){
            binding.apply {
                progressValue.max = maxValue.toInt()
                progressValue.progress = champ.stats.goldEarned.toInt()
                img.load("https://raw.communitydragon.org/pbe/plugins/rcp-be-lol-game-data/global/default/v1/champion-icons/${champ.championId}.png")
                value.setText(champ.stats.goldEarned.toString())

                if(champ.teamId != myTeam){
                    progressValue.progressTintList = ColorStateList.valueOf(Color.RED)
                }

                if(champ.participantId == id){
                    binding.progressValue.progressTintList = ColorStateList.valueOf(Color.BLUE)
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Participant>() {
        override fun areItemsTheSame(oldItem: Participant, newItem: Participant) =
            oldItem.participantId == newItem.participantId

        override fun areContentsTheSame(oldItem: Participant, newItem: Participant) =
            oldItem == newItem
    }

}