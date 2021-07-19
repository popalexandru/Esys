# Esys

Esys is an Android mobile application developed for the League of Legends players aspiring to improve its playstyle. This application gives the opportunity for a player to check his match history with details and statistics for each game.
The application also has a feature for junglers which can have an eye of the jungle camps timers, and also runes builds depending on the match history fetched by the server.

The Riot API key is secured being part of a AWS Lambda which handles all the requests, Esys only calls the AWS Gateway with the desired parameters.

    @GET(AWS_REQUEST_LINK)
    suspend fun getSummonerLeagues(
            @Query("region") region : String,
            @Query("encryptedSummonerId") encryptedSummonerId : String,
            @Query("request") request : String
    ) : Response<Set<LeagueEntry>>

    @GET(AWS_REQUEST_LINK)
    suspend fun getSummonerByRegion(
            @Query("region") region : String,
            @Query("summonerName") summonerName : String,
            @Query("request") request : String
    ) : Response<Summoner>

    @GET(AWS_REQUEST_LINK)
    suspend fun getChampionMastery(
            @Query("region") region : String,
            @Query("encryptedSummonerId") encryptedSummonerId : String,
            @Query("request") request : String
    ) : Response<List<ChampionMastery>>

     @GET(AWS_REQUEST_LINK)
     suspend fun getMatchV4(
             @Query("region") region: String,
             @Query("matchId") matchId: Long,
             @Query("request") request : String
     ) : Response<Match>

    @GET(AWS_REQUEST_LINK)
    suspend fun getMatchListV4(
            @Query("region") region: String,
            @Query("encryptedSummonerId") encryptedSummonerId : String,
            @Query("request") request : String
    ) : Response<MatchList>
    
        const val AWS_REQUEST_LINK = "https://*******.execute-api.us-east-2.amazonaws.com/stage/request"

## Home screen

The initial screen contains some fields for reading the summoner name and the region. It also displays a list with the favorite summoners.
Once the user inputs a valid summoner name and a region, the data fetching starts and the user is brought to the summoner screen

![Home screen](/images/home.jpg)

## Summoner screen

The Summoner screen displays the data needed for visualizing the profile of a summoner. It displays its name, summoner level, ranked solo/flex ranks, best 10 played champs and the match history.
As a background image, the splash image of the most played champ is loaded.
From this screen the user can tap the heart icon to add it to favorites.
It can also click on any game object and the match viewer screen will be displayed.

![summoner_fr screen](/images/summoner_fr.jpg)

## Match viewer screen

This screen displays some data about the selected game and some tabs for checking the damage dealt, vision, gold etc.

![Game overview screen](/images/overview.jpg)

## Champion builds screen

Here we have a list from which the user can choose a champion for which the build recommendation will be displayed.

![Build list screen](/images/bulds_list.jpg)

## Selected champion build screen

Once a champion is selected, the user will see some details but most important will se the recommended runes, which are determined based on the games already analysed inside the database.

![Build view screen](/images/build_view.jpg)

## Jungle timer screen

The jungle timer screen offers the posibility to click on any jungne camp picture and start a countdown so that the user will know when the camp will be respawned.

![Jungle timer screen](/images/jungle_timer.jpg)

![Jungle timer clicked screen](/images/jungle_timer_clicked.jpg)

Esys also has a screen for displaying the latest news based on different topics

![News screen](/images/news_list.jpg)

## A lot of new feature will be added like displaying the best game out of last 20, more statistics, skill order recommendations etc.

