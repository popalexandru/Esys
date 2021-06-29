package com.example.leagueapp.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.load
import coil.request.ImageRequest
import coil.util.CoilUtils
import com.example.leagueapp.Constants
import com.example.leagueapp.Constants.ITEM_IMAGE
import com.example.leagueapp.databinding.MatchLayoutBinding
import com.example.leagueapp.networking.model.Utils.Utility
import com.example.leagueapp.networking.model.models.Match
import com.example.leagueapp.networking.model.models.Participant
import com.example.leagueapp.networking.model.models.ParticipantIdentity
import com.example.leagueapp.networking.model.models.ParticipantStats
import com.example.leagueapp.repository.ReadJsonRepository
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

class MatchListAdapter(private var listener : MatchClickListener) : ListAdapter<Match, MatchListAdapter.CakeViewHolder>(
    DiffCallback()
){
    private var accountId : String = ""
    private var context: Context? = null

    fun setAccountId(Id : String){
        accountId = Id
    }

    fun setContext(ctx : Context){
        context = ctx
    }

    override fun submitList(list: MutableList<Match>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CakeViewHolder {
        val binding = MatchLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CakeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CakeViewHolder, position: Int) {
        val currentCake = getItem(position)

        holder.bind(currentCake)
    }

    inner class CakeViewHolder(private val binding : MatchLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            itemView.setOnClickListener {
                val position: Int = adapterPosition
                listener.onMatchClickListener(getItem(position))
            }
        }

        fun bind(champ: Match){
            val participantIdentity = getParticipantByAccountId(champ.participantIdentities)
            val participant = getScore(participantIdentity.participantId, champ.participants)
            val participantStats = participant.stats
            val champId = participant.championId.toString()

            binding.apply {
                kills.setText(participantStats.kills.toString())
                assists.setText(participantStats.assists.toString())
                deaths.setText(participantStats.deaths.toString())

                champImage.load("https://raw.communitydragon.org/pbe/plugins/rcp-be-lol-game-data/global/default/v1/champion-icons/${champId}.png")
                rune1.load(ReadJsonRepository.getRuneById(participantStats.perkPrimaryStyle))
                rune2.load(ReadJsonRepository.getRuneById(participantStats.perkSubStyle))
                

                loadImage(item0, participantStats.item0)
                loadImage(item1, participantStats.item1)
                loadImage(item2, participantStats.item2)
                loadImage(item3, participantStats.item3)
                loadImage(item4, participantStats.item4)
                loadImage(item5, participantStats.item5)

                damageDealt.setText(participantStats.totalDamageDealtToChampions.toString())
                creepScore.setText((participantStats.totalMinionsKilled + participantStats.neutralMinionsKilled).toString())
                wardScore.setText(participantStats.visionScore.toString())

                textViewChampName.setText(ReadJsonRepository.getChampNameById(champId))
                gameTime.setText(Utility.getTimeAgo(champ.gameCreation))
                GameType.setText(champ.queue.getName())
                GameDuration.setText(champ.duration)


                if(champ.isRemake){
                    separatorBottom.setBackgroundColor(Color.YELLOW)
                    damageDealt.setText("")
                    wardScore.setText("")
                    creepScore.setText("")
                    imageView.isVisible = false
                    imageView2.isVisible = false
                    imageView3.isVisible = false
                    kills.setText("Remake")
                    assists.setText("")
                    deaths.setText("")
                    slash1.setText("")
                    slash2.setText("")
                }else{
                    if(participantStats.win){
                        separatorBottom.setBackgroundColor(Color.CYAN)
                    }else{
                        separatorBottom.setBackgroundColor(Color.RED)
                    }
                }


                champLevel.setText(participantStats.champLevel.toString())
            }
        }

        private fun loadImage(image: ImageView, item: Int) {
            if(item != 0){
                Timber.d("Loading image for ${image.id}")
                image.load(ITEM_IMAGE+"${item}.png")
            }
        }
    }



    private fun getScore(participantIdentity: Int, participants: List<Participant>): Participant {
        var participantStats = Participant()

        for(participant in participants){
            if(participant.participantId == participantIdentity){
                participantStats = participant
            }
        }

        //return "${participantStats.kills} / ${participantStats.assists} / ${participantStats.deaths}"
        return participantStats
    }

    class DiffCallback : DiffUtil.ItemCallback<Match>() {
        override fun areItemsTheSame(oldItem: Match, newItem: Match) =
            oldItem.gameId == newItem.gameId

        override fun areContentsTheSame(oldItem: Match, newItem: Match) =
            oldItem == newItem
    }

    private fun getParticipantByAccountId(participantIdentities : List<ParticipantIdentity>) : ParticipantIdentity {
        var participantReturn = ParticipantIdentity()

        for(participant in participantIdentities){
            if(participant.player.accountId.equals(accountId)){
                participantReturn =  participant
            }
        }

        return participantReturn
    }

    interface MatchClickListener{
        fun onMatchClickListener(item : Match)
    }


}