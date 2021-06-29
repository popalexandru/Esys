package com.example.leagueapp.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
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
import com.example.leagueapp.database.FavoriteItem
import com.example.leagueapp.databinding.MatchLayoutBinding
import com.example.leagueapp.databinding.PlayerLayoutBinding
import com.example.leagueapp.networking.model.Utils.Utility
import com.example.leagueapp.networking.model.models.*
import com.example.leagueapp.repository.ReadJsonRepository
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

class MatchViewerAdapter(private val listener : onClicked) : ListAdapter<PlayerObject, MatchViewerAdapter.CakeViewHolder>(
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CakeViewHolder {
        val binding = PlayerLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CakeViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: CakeViewHolder, position: Int) {
        val currentCake = getItem(position)

        holder.layout.setOnClickListener {
            if(position != RecyclerView.NO_POSITION){
                listener.onSummonerClicked(currentCake)
            }
        }

        holder.bind(currentCake)
    }

    inner class CakeViewHolder(private val binding : PlayerLayoutBinding, private val listener : onClicked) : RecyclerView.ViewHolder(binding.root){
        val layout = binding.playerCardView

        init {
            itemView.setOnClickListener {
                val position: Int = adapterPosition
                listener.onSummonerClicked(getItem(position))
            }
        }

        fun bind(champ: PlayerObject){
            binding.apply {

                separator.visibility = View.VISIBLE

                if(champ.participant.stats.win){
                    separator.setBackgroundColor(Color.CYAN)
                }else{
                    separator.setBackgroundColor(Color.MAGENTA)
                }

                rune1.load(ReadJsonRepository.getRuneById(participantStats.perkPrimaryStyle))
                rune2.load(ReadJsonRepository.getRuneById(participantStats.perkSubStyle))

                separator.visibility = View.GONE

                if(champ.participant.isCarry){
                    champImage.borderColor = Color.GREEN
                    champImage.borderWidth = 2
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                        champImage.outlineSpotShadowColor = Color.YELLOW
                    }
                }

                playerName.setText(champ.participantIdentity.player.summonerName)

                kills.setText(champ.participant.stats.kills.toString())
                assists.setText(champ.participant.stats.assists.toString())
                deaths.setText(champ.participant.stats.deaths.toString())

                champImage.load("https://raw.communitydragon.org/pbe/plugins/rcp-be-lol-game-data/global/default/v1/champion-icons/${champ.participant.championId}.png")

                loadImage(item0, champ.participant.stats.item0)
                loadImage(item1, champ.participant.stats.item1)
                loadImage(item2, champ.participant.stats.item2)
                loadImage(item3, champ.participant.stats.item3)
                loadImage(item4, champ.participant.stats.item4)
                loadImage(item5, champ.participant.stats.item5)


                textViewChampName.setText(ReadJsonRepository.getChampNameById(champ.participant.championId.toString()))

                champLevel.setText(champ.participant.stats.champLevel.toString())
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

    class DiffCallback : DiffUtil.ItemCallback<PlayerObject>() {
        override fun areItemsTheSame(oldItem: PlayerObject, newItem: PlayerObject) =
            oldItem.participant.participantId == newItem.participant.participantId

        override fun areContentsTheSame(oldItem: PlayerObject, newItem: PlayerObject) =
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

    interface onClicked{
        fun onSummonerClicked(item : PlayerObject)
    }


}