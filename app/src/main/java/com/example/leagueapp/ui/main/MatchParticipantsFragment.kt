package com.example.leagueapp.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.leagueapp.R
import com.example.leagueapp.adapters.MatchViewerAdapter
import com.example.leagueapp.databinding.LayoutGameViewerBinding
import com.example.leagueapp.networking.model.Utils.Utility
import com.example.leagueapp.networking.model.models.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchParticipantsFragment(private val match: Match, private val name : String, private val region: String) : Fragment(), MatchViewerAdapter.onClicked {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setHasOptionsMenu(true)
    }

    private var _binding : LayoutGameViewerBinding? = null
    private val binding get() = _binding!!

    private lateinit var receivedSummoner: Summoner
    private var summonerExists : Boolean = false
    private lateinit var receivedMatch : Match
    private lateinit var myName : String

    private val globalViewModel : GlobalViewModel by viewModels()
    private val viewModel : MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LayoutGameViewerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val match = arguments?.getSerializable("match") as Match
        receivedMatch = match

        val participantObjects = mutableListOf<PlayerObject>()

        for(participant in match.participants){
            val participantId = participant.participantId

            val participantIdentity = getParticipantIdentity(participantId, match.participantIdentities)

            if(region != null){
                participantObjects.add(PlayerObject(participant, participantIdentity, region))
            }
        }

        val teamOne = participantObjects.subList(0, 5)
        val teamTwo = participantObjects.subList(5, 10)

        determineWinner(match)

        populateRecyclerViews(teamOne, teamTwo)

        loadBannedChamps(match)

        //setTimeAndDate(match)

        setTeamStatistics(teamOne, teamTwo, match)
    }

    private fun setTeamStatistics(
        teamOne: MutableList<PlayerObject>,
        teamTwo: MutableList<PlayerObject>,
        match: Match
    ) = binding.apply {
        var visionTeamOne = 0L
        var visionTeamTwo = 0L

        for(player in teamOne){
            visionTeamOne += player.participant.stats.visionScore
        }

        for(player in teamTwo){
            visionTeamTwo += player.participant.stats.visionScore
        }

        dragonKills1.setText(match.teams[0].dragonKills.toString())
        dragonKills2.setText(match.teams[1].dragonKills.toString())

        baronKills1.setText(match.teams[0].baronKills.toString())
        baronKills2.setText(match.teams[1].baronKills.toString())

        wardsPlaced1.setText(visionTeamOne.toString())
        wardsPlaced2.setText(visionTeamTwo.toString())
    }

    private fun loadBannedChamps(match: Match) {
        val teamOneStats = match.teams[0]
        val teamTwoStats = match.teams[1]

        binding.apply {
            ban1.load(champLink(teamOneStats.bans[0].championId))
            ban2.load(champLink(teamOneStats.bans[1].championId))
            ban3.load(champLink(teamOneStats.bans[2].championId))
            ban4.load(champLink(teamOneStats.bans[3].championId))
            ban5.load(champLink(teamOneStats.bans[4].championId))

            ban21.load(champLink(teamTwoStats.bans[0].championId))
            ban22.load(champLink(teamTwoStats.bans[1].championId))
            ban23.load(champLink(teamTwoStats.bans[2].championId))
            ban24.load(champLink(teamTwoStats.bans[3].championId))
            ban25.load(champLink(teamTwoStats.bans[4].championId))
        }
    }

    private fun champLink(champId : Int) : String {
        return "https://raw.communitydragon.org/pbe/plugins/rcp-be-lol-game-data/global/default/v1/champion-icons/${champId}.png"
    }

    private fun populateRecyclerViews(teamOne: MutableList<PlayerObject>, teamTwo: MutableList<PlayerObject>) {
        val adapterOne = MatchViewerAdapter(this)
        val adapterTwo = MatchViewerAdapter(this)

        binding.recyclerViewTeam1.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            hasFixedSize()
            adapter = adapterOne
        }

        binding.recyclerViewTeam2.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            hasFixedSize()
            adapter = adapterTwo
        }

        adapterOne.submitList(teamOne)
        adapterTwo.submitList(teamTwo)

        binding.idsGroup.isVisible = true
    }


    private fun determineWinner(match: Match) {
        if(match.teams[0].win == "Win"){
            binding.winOrLose1.text = "Win"
            binding.winOrLose1.setTextColor(Color.CYAN)
            binding.winOrLose2.text = "Lose"
            binding.winOrLose2.setTextColor(Color.MAGENTA)
        }else{
            binding.winOrLose2.text = "Win"
            binding.winOrLose2.setTextColor(Color.CYAN)
            binding.winOrLose1.text = "Lose"
            binding.winOrLose1.setTextColor(Color.MAGENTA)
        }
    }

    private fun getParticipantIdentity(participantId: Int, participantIdentities: List<ParticipantIdentity>): ParticipantIdentity {
        var participantIdentity = ParticipantIdentity()

        for(identity in participantIdentities){
            if(identity.participantId == participantId){
                participantIdentity = identity
            }
        }

        return participantIdentity
    }

    override fun onSummonerClicked(item: PlayerObject) {
        //findNavController().navigate(R.id.action_matchWholeViewer_to_jungleTimerFragment)
        //findNavController().navigate(R.id.action_matchViewFragment_to_damageToChampionsFragment, bundle)
/*        viewModel.getSummoner(item.participantIdentity.player.summonerName, item.region)

        viewModel.uiState.asLiveData().observe(viewLifecycleOwner){ state ->
            when(state){

                is MainViewModel.SummonerUiState.Success -> {
                    val bundle = bundleOf("summoner" to state.summoner)

                    findNavController().navigate(R.id.action_matchViewFragment_to_summonerFragment, bundle)

                    viewModel.insertSearch(state.summoner.name)
                    viewModel.emptyState()
                }

                is MainViewModel.SummonerUiState.Error -> {
                    //binding.getButton.clearAnimation()
                    //binding.nameInputLayout.error = state.exception
                    Toast.makeText(requireContext(), "Can't do that ${state.exception}", Toast.LENGTH_SHORT).show()
                }

                is MainViewModel.SummonerUiState.Empty -> {
                    //binding.getButton.revertAnimation()
                }

            }
        }*/

    }
}