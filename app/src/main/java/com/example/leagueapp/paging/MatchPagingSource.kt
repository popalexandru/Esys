package com.example.leagueapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.leagueapp.networking.model.api.RiotApi
import com.example.leagueapp.networking.model.models.Match
import com.example.leagueapp.networking.model.models.MatchReference
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

private const val MATCH_STARTING_PAGE_INDEX = 0

class MatchPagingSource(
    private val riotApi: RiotApi,
    private val matchId: Long,
    private val matchReferences : List<MatchReference>,
    private val region: String
) : PagingSource<Int, Match>(){


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Match> {
        val position = params.key ?: MATCH_STARTING_PAGE_INDEX
        val matchesPage = mutableListOf<Match>()

        val response = riotApi.getMatchV4(region, matchReferences[position].gameId, "match")
        val match = response.body()

        val job = coroutineScope {
            launch { riotApi.getMatchV4(region, matchReferences[position].gameId, "match").body()?.let {
                matchesPage.add(
                    it
                )
            } }
            launch { riotApi.getMatchV4(region, matchReferences[position + 1].gameId, "match").body()?.let {
                matchesPage.add(it)
            } }
            launch { riotApi.getMatchV4(region, matchReferences[position + 2].gameId, "match").body()?.let {
                matchesPage.add(it)
            } }
            launch { riotApi.getMatchV4(region, matchReferences[position + 3].gameId, "match").body()?.let {
                matchesPage.add(it)
            } }
        }

        job.join()

        return LoadResult.Page(
            data = matchesPage,
            prevKey = if(position == MATCH_STARTING_PAGE_INDEX) null else position - 4,
            nextKey = if(matchesPage.isEmpty()) null else position + 4
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Match>): Int? {
        TODO("Not yet implemented")
    }


}