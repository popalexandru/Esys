package com.example.leagueapp.ui.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.ImageRequest
import coil.request.ImageRequest.Listener
import coil.request.ImageResult
import com.example.leagueapp.R
import com.example.leagueapp.adapters.MatchListAdapter
import com.example.leagueapp.adapters.PlayedChampsAdapter
import com.example.leagueapp.database.FavoriteItem
import com.example.leagueapp.databinding.SummonerFragmentBinding
import com.example.leagueapp.models.PlayedChampModel
import com.example.leagueapp.networking.model.Utils.Utility
import com.example.leagueapp.networking.model.models.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SummonerFragment : Fragment(), MatchListAdapter.MatchClickListener {

    companion object {
        fun newInstance() = SummonerFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    val matchListAdapter = MatchListAdapter(this)

    private var _binding : SummonerFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var receivedSummoner: Summoner
    private var summonerExists : Boolean = false

    private val globalViewModel : GlobalViewModel by viewModels()
    private val viewModel : MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SummonerFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val summoner = arguments?.getSerializable("summoner") as Summoner
        receivedSummoner = summoner

        lifecycleScope.launchWhenStarted {
            globalViewModel.getAll(summoner.id, summoner.accountId, summoner.puuid, summoner.region)
        }

        globalViewModel.uiState.asLiveData().observe(viewLifecycleOwner){
            when(it){
                is GlobalViewModel.SummonerUiState.Success -> {
                    loadViews(it.summonerPackage, summoner)
                }
            }
        }

        globalViewModel.getLiveMatches().observe(viewLifecycleOwner){
            matchListAdapter.submitList(it as MutableList<Match>)
            Timber.d(it.size.toString())

            binding.loadMore.revertAnimation()
        }


    }

    private var isLoading = false
    private var previousTotal = 0

    private fun loadViews(summonerPackage: SummonerPackage, summoner: Summoner) {
        loadBackgroundImage(summonerPackage.championMasteries[0])

        binding.apply {
            summonerName.setText(summoner.name)
            summonerLevel.setText(summoner.summonerLevel.toString())

            val played_champs_adapter = PlayedChampsAdapter()
            val playedChampsList = mutableListOf<PlayedChampModel>()
            matchListAdapter.setAccountId(summoner.accountId)
            matchListAdapter.setContext(requireContext())

            for (i in 0..10){
                val champ = summonerPackage.championMasteries.get(i)

                playedChampsList.add(
                    PlayedChampModel(
                        champ.championId,
                        champ.championPoints,
                        champ.championLevel
                    )
                )
            }

            recyclerViewPlayedChamps.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                hasFixedSize()
                adapter = played_champs_adapter
            }

            recyclerViewMatchList.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                hasFixedSize()
                adapter = matchListAdapter
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
/*                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                            binding.pbar.visibility = View.VISIBLE

                            globalViewModel.loadMoreGames()
                        }else{
                            binding.pbar.visibility = View.INVISIBLE
                        }

                    }

                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if((recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() == (recyclerView.layoutManager as LinearLayoutManager).itemCount-1){
                            Toast.makeText(requireContext(), "Prepreprep", Toast.LENGTH_SHORT).show()
                        }

                        super.onScrolled(recyclerView, dx, dy)
                    }*/
                })
            }

            binding.loadMore.setOnClickListener {
                binding.loadMore.startAnimation()
                globalViewModel.loadMoreGames()
            }

            played_champs_adapter.submitList(playedChampsList)

            matchListAdapter.submitList(summonerPackage.matchList as MutableList<Match>)
            //Timber.d("Submitting list ${summonerPackage.matchList.size}")

            val soloEntry = summonerPackage.soloEntry
            val flexEntry = summonerPackage.flexEntry

            bindgDetailsForLeague(soloEntry, flexEntry)

            summonerImage.load("https://raw.communitydragon.org/latest/game/assets/ux/summonericons/profileicon${summoner.profileIconId}.png"){
                crossfade(true)
                listener(object : Listener {
                    override fun onError(request: ImageRequest, throwable: Throwable) {
                        super.onError(request, throwable)

                        //Toast.makeText(requireContext(), "${throwable.message}", Toast.LENGTH_SHORT).show()
                    }

                    override fun onSuccess(
                        request: ImageRequest,
                        metadata: ImageResult.Metadata
                    ) {
                        super.onSuccess(request, metadata)
                        stopLoading()
                    }
                })
            }
        }
    }

    private fun loadBackgroundImage(championMastery: ChampionMastery) {
        val champId = championMastery.championId
        val skinLink = "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/champion-splashes/${champId}/${champId}004.jpg"
        Timber.d("Loading $skinLink")
        binding.backgroundImage.load(skinLink)
    }

    private fun bindgDetailsForLeague(soloLeague: LeagueEntry, flexLeague: LeagueEntry) {

/*        val soloImageLink = soloLeague.tier.toLowerCase() + "_1.png"
        val flexImageLink = flexLeague.tier.toLowerCase() + "_1.png"*/

        val drawableSolo = getDrawableFromTier(soloLeague.tier, soloLeague.rank)
        binding.rankImageSolo.load(drawableSolo)

        val drawableFlex = getDrawableFromTier(flexLeague.tier, flexLeague.rank)
        binding.rankImageFlex.load(drawableFlex)

        if(soloLeague.tier.isNotEmpty() && soloLeague.rank.isNotEmpty()){
            binding.tierSolo.setText("${soloLeague.tier} ${getArabic(soloLeague.rank)}")
            binding.soloTierDesc.setText("${soloLeague.leaguePoints}")
            //binding.soloTierDesc.setText(soloLeague.leagueId)
        }else{
            //binding.soloTierDesc.setText("")
            binding.tierSolo.setText("Unranked")
            binding.soloTierDesc.setText("")
        }

        if(flexLeague.tier.isNotEmpty() && flexLeague.rank.isNotEmpty()){
            binding.tierFlex.setText("${flexLeague.tier} ${getArabic(flexLeague.rank)}")
            binding.flexTierDesc.setText("${flexLeague.leaguePoints}")
            //binding.soloTierDesc.setText(flexLeague.leagueId)
        }else{
            //binding.soloTierDesc.setText("")
            binding.tierFlex.setText("Unranked")
            binding.flexTierDesc.setText("")
        }

    }

    private fun getDrawableFromTier(tier: String, rank: String): Int {
        val drawableString = tier.toLowerCase() + rank

        return when(drawableString){
            "challengerI" -> R.drawable.challenger_1

            "ironI" -> R.drawable.iron_1
            "ironII" -> R.drawable.iron_2
            "ironIII" -> R.drawable.iron_3
            "ironIV" -> R.drawable.iron_4

            "bronzeI" -> R.drawable.bronze_1
            "bronzeII" -> R.drawable.bronze_2
            "bronzeIII" -> R.drawable.bronze_3
            "bronzeIV" -> R.drawable.bronze_4

            "silverI" -> R.drawable.silver_1
            "silverII" -> R.drawable.silver_2
            "silverIII" -> R.drawable.silver_3
            "silverIV" -> R.drawable.silver_4

            "goldI" -> R.drawable.gold_1
            "goldII" -> R.drawable.gold_2
            "goldIII" -> R.drawable.gold_3
            "goldIV" -> R.drawable.gold_4

            "platinumI" -> R.drawable.platinum_1
            "platinumII" -> R.drawable.platinum_2
            "platinumIII" -> R.drawable.platinum_3
            "platinumIV" -> R.drawable.platinum_4

            "masterI" -> R.drawable.master_1

            "grandmasterI" -> R.drawable.grandmaster_1

            else -> R.drawable.notranked1
        }
    }

    private fun stopLoading(){
        binding.apply{
            group.isVisible = true
            binding.shimmerFrame.stopShimmer()
            binding.shimmerFrame.visibility = View.GONE
        }
    }

    private fun getArabic(old: String) : String {
        return when(old){
            "I" -> "1"
            "II" -> "2"
            "III" -> "3"
            "IV" -> "4"
            else -> ""
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.save_button, menu)
        val favoriteButton = menu.findItem(R.id.action_search)

        viewModel.favoriteExists(receivedSummoner.accountId)

        lifecycleScope.launchWhenStarted {
            viewModel.summonerExists.collect {
                if(it){
                    favoriteButton.isChecked = true
                    favoriteButton.setIcon(R.drawable.ic_favorite)
                }else{
                    favoriteButton.isChecked = false
                    favoriteButton.setIcon(R.drawable.ic_favorite_border)
                }
            }
        }




/*        favoriteButton.setOnMenuItemClickListener {
            Toast.makeText(requireContext(), "clicked", Toast.LENGTH_SHORT).show()
        }*/

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_search -> {
                item.setChecked(!item.isChecked)

                toggleFavorite(item.isChecked, item)
            }
            else -> Unit
        }

        return super.onOptionsItemSelected(item)
    }

    private fun toggleFavorite(state: Boolean, item: MenuItem){
        if(item.isChecked){
            item.setIcon(R.drawable.ic_favorite)
            viewModel.insertFavorite(receivedSummoner)

            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(
                    requireContext(),
                    "Added ${receivedSummoner.name} to favorites",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }else{
            viewModel.deleteFavorite(
                FavoriteItem(
                    receivedSummoner.accountId,
                    receivedSummoner.profileIconId,
                    receivedSummoner.name,
                    receivedSummoner.summonerLevel,
                        receivedSummoner.region
                )
            ).invokeOnCompletion {
                if(it == null){
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(
                            requireContext(),
                            "Deleted ${receivedSummoner.name} from favorites",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    item.setIcon(R.drawable.ic_favorite_border)
                }
            }

        }
    }

    override fun onMatchClickListener(item: Match) {
        val bundle = bundleOf("match" to item)
        bundle.putString("name", receivedSummoner.name)
        bundle.putString("region", receivedSummoner.region)

        findNavController().navigate(R.id.action_summonerFragment_to_matchWholeViewer, bundle)
    }
}