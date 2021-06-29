package com.example.leagueapp.ui.main

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.efaso.admob_advanced_native_recyvlerview.AdmobNativeAdAdapter
import com.example.leagueapp.R
import com.example.leagueapp.adapters.FavoritesAdapter
import com.example.leagueapp.alarm.AlarmBroadcastReceiver
import com.example.leagueapp.database.FavoriteItem
import com.example.leagueapp.databinding.MainFragmentBinding
import com.example.leagueapp.networking.model.models.Region
import com.example.leagueapp.repository.ReadJsonRepository
import com.example.leagueapp.ui.main.MainViewModel.SummonerUiState.Success
import com.example.leagueapp.ui.main.MainViewModel.SummonerUiState.Error
import com.example.leagueapp.ui.main.MainViewModel.SummonerUiState.Empty
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class MainFragment : Fragment(), FavoritesAdapter.onDeleteClick {

    private var _binding : MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(requireContext())
        RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("DCB08AD74B27BF7C8694E3ACA6B194C9"))
        ReadJsonRepository
    }

    override fun onResume() {
        super.onResume()
        val regions = resources.getStringArray(R.array.regions)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, regions)
        binding.autocomplete.setAdapter(arrayAdapter)


    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val favSummonersAdapter = FavoritesAdapter(this)

        lifecycleScope.launchWhenStarted {
            viewModel.isAlarmScheduled.collect {
                when(it.yes){
                    true -> {
                        Timber.d("Alarm already scheduled")
                    }

                    false -> {
                        Timber.d("Scheduling the alarm..")
/*
                        alarmMgr = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                        alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
                            PendingIntent.getBroadcast(context, 0, intent, 0)
                        }

                        alarmMgr?.set(
                            AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            SystemClock.elapsedRealtime() + 60 * 1000,
                            alarmIntent
                        )*/
                        // Set the alarm to start at approximately 2:00 p.m.
                        val calendar: Calendar = Calendar.getInstance().apply {
                            timeInMillis = System.currentTimeMillis()
                        }

                        alarmIntent = Intent(context, AlarmBroadcastReceiver::class.java).let { intent ->
                            PendingIntent.getBroadcast(context, 0, intent, 0)
                        }

                        alarmMgr?.setInexactRepeating(
                            AlarmManager.RTC_WAKEUP,
                            calendar.timeInMillis,
                            AlarmManager.INTERVAL_HALF_HOUR,
                            alarmIntent
                        )

                        viewModel.setAlarm(true)
                    }
                }
            }
        }


        lifecycleScope.launchWhenStarted {
            viewModel.preferencesFlow.collect {
                if(it.region.equals("")){

                }else{
                    //binding.autocomplete.setSelection(5)
                }
            }
        }

        binding.favoritesRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            hasFixedSize()
            adapter = favSummonersAdapter
        }

        binding.autocomplete.setOnItemClickListener { adapterView, view, i, l ->
            binding.regionLayout.error = null
            viewModel.setRegion(binding.autocomplete.text.toString().toLowerCase())
        }

        viewModel.favoritesFlow.observe(viewLifecycleOwner){
            favSummonersAdapter.submitList(it)
        }

        viewModel.searches.observe(viewLifecycleOwner){
            val searchQueries = mutableListOf<String>()

            for(search in it){
                searchQueries.add(search.name)
            }

            val adapter = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                searchQueries
            )

            binding.nameInput.setAdapter(adapter)
        }

        viewModel.myHeaders.observe(viewLifecycleOwner){
            Log.d("Headers", it.toString())
        }

        binding.getButton.setOnClickListener {
            hideKeyboard()
            val summonerInputName = binding.nameInput.text.toString()

            if(summonerInputName.isNotEmpty()){
                if(binding.autocomplete.text.toString().equals("Region"))
                {
                    binding.regionLayout.error = "Choose your region"
                }else{
                    binding.getButton.startAnimation()
                    viewModel.getSummoner(summonerInputName, binding.autocomplete.text.toString().toLowerCase())
                }
            }else{
                binding.nameInputLayout.error = "Insert your name"
            }
        }

        viewModel.uiState.asLiveData().observe(viewLifecycleOwner){ state ->
            when(state){

                is Success -> {
                    val bundle = bundleOf("summoner" to state.summoner)

                    findNavController().navigate(R.id.action_mainFragment_to_summonerFragment, bundle)

                    viewModel.insertSearch(state.summoner.name)
                    viewModel.emptyState()
                }

                is Error -> {
                    binding.getButton.clearAnimation()
                    binding.nameInputLayout.error = state.exception
                    binding.getButton.revertAnimation()
                }

                is Empty -> {
                    binding.getButton.revertAnimation()
                }

            }
        }

        binding.nameInput.doOnTextChanged { text, _, _, _ ->
            binding.nameInputLayout.error = null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        loadAd()
    }
    private fun hideKeyboard(){
        val view = activity?.currentFocus
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun deleteSummoner(item: FavoriteItem) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Do you want to delete ${item.name} ?")
        builder.setPositiveButton("Yes", DialogInterface.OnClickListener(){_,_ ->
            viewModel.deleteFavorite(item).invokeOnCompletion {
                if(it == null){
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(requireContext(), "Deleted ${item.name}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
        builder.setNegativeButton("No", null)
        builder.setCancelable(true)

        val dialog = builder.create()
        dialog.show()
    }

    override fun onSummonerClicked(item: FavoriteItem) {
        hideKeyboard()
        val summonerInputName = item.name

        if(summonerInputName.isNotEmpty()){
                binding.favoriteProgress.visibility = View.VISIBLE
                viewModel.getSummoner(summonerInputName, item.regionName.toLowerCase())
        }else{
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(requireContext(), "Error occured", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun loadAd() {
        val builder = AdLoader.Builder(requireContext(), "ca-app-pub-4479200586800321/6961703752")

        builder.forNativeAd { nativeAd -> // You must call destroy on old ads when you are done with them,
            val adView = layoutInflater
                .inflate(R.layout.ad_unified, null) as NativeAdView
            populateNativeAdView(nativeAd, adView)

            binding.adFrame.removeAllViews()
            binding.adFrame.addView(adView)
        }

        val adOptions = NativeAdOptions.Builder()
            .build()

        builder.withNativeAdOptions(adOptions)

        val adLoader = builder.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                val error =
                    """
           domain: ${loadAdError.domain}, code: ${loadAdError.code}, message: ${loadAdError.message}
          """"
                Toast.makeText(
                    requireContext(), "Failed to load native ad with error $error",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }).build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        // Set the media view.
        adView.mediaView = adView.findViewById<MediaView>(R.id.ad_media)

        // Set other ad assets.
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        adView.priceView = adView.findViewById(R.id.ad_price)
        adView.starRatingView = adView.findViewById(R.id.ad_stars)
        adView.storeView = adView.findViewById(R.id.ad_store)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

        // The headline and media content are guaranteed to be in every UnifiedNativeAd.
        (adView.headlineView as TextView).text = nativeAd.headline
        //adView.mediaView.setMediaContent(nativeAd.mediaContent)

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.body == null) {
            adView.bodyView.visibility = View.INVISIBLE
        } else {
            adView.bodyView.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }

        if (nativeAd.callToAction == null) {
            adView.callToActionView.visibility = View.INVISIBLE
        } else {
            adView.callToActionView.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }

        if (nativeAd.icon == null) {
            adView.iconView.visibility = View.GONE
        } else {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon.drawable
            )
            adView.iconView.visibility = View.VISIBLE
        }

        if (nativeAd.price == null) {
            adView.priceView.visibility = View.INVISIBLE
        } else {
            adView.priceView.visibility = View.VISIBLE
            (adView.priceView as TextView).text = nativeAd.price
        }

        if (nativeAd.store == null) {
            adView.storeView.visibility = View.INVISIBLE
        } else {
            adView.storeView.visibility = View.VISIBLE
            (adView.storeView as TextView).text = nativeAd.store
        }

        if (nativeAd.starRating == null) {
            adView.starRatingView.visibility = View.INVISIBLE
        } else {
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
            adView.starRatingView.visibility = View.VISIBLE
        }

        if (nativeAd.advertiser == null) {
            adView.advertiserView.visibility = View.INVISIBLE
        } else {
            (adView.advertiserView as TextView).text = nativeAd.advertiser
            adView.advertiserView.visibility = View.VISIBLE
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd)
    }
}