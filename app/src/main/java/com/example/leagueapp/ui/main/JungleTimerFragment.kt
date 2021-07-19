package com.example.leagueapp.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.*
import android.view.View.VISIBLE
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.leagueapp.Constants
import com.example.leagueapp.Constants.SPAWN_BARON
import com.example.leagueapp.Constants.SPAWN_BLUE
import com.example.leagueapp.Constants.SPAWN_DRAGON
import com.example.leagueapp.Constants.SPAWN_GROMP
import com.example.leagueapp.Constants.SPAWN_KRUGS
import com.example.leagueapp.Constants.SPAWN_RAPTORS
import com.example.leagueapp.Constants.SPAWN_RED
import com.example.leagueapp.Constants.SPAWN_SCUTTER
import com.example.leagueapp.Constants.SPAWN_WOLVES
import com.example.leagueapp.R
import com.example.leagueapp.databinding.JungleTimerLayoutBinding
import com.example.leagueapp.services.JungleService
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class JungleTimerFragment() : Fragment() {

    private var _binding : JungleTimerLayoutBinding? = null
    private val binding get() = _binding!!

    private val globalViewModel : GlobalViewModel by viewModels()
    private val viewModel : MainViewModel by viewModels()

    private lateinit var vibrator: Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = JungleTimerLayoutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        loadAd()

        binding.apply {
            redCardView.setOnClickListener {
                sendCommandToService(Constants.ACTION_START_OR_RESUME, "red")
                binding.redCounter.apply{
                    isVisible = !JungleService.isRedCounting
                    text = ""

                    if(!JungleService.isRedCounting){
                        text = SPAWN_RED.toString()
                        grayImage(redImage)
                    }else{
                        colorImage(redImage)
                    }
                }
            }

            blueCardView.setOnClickListener {
                sendCommandToService(Constants.ACTION_START_OR_RESUME, "blue")
                binding.blueCounter.apply{
                    isVisible = !JungleService.isBlueCounting
                    text = ""

                    if(!JungleService.isBlueCounting){
                        text = SPAWN_BLUE.toString()
                        grayImage(blueImage)
                    }else{
                        colorImage(blueImage)
                    }
                }
            }

            dragonCardView.setOnClickListener {
                sendCommandToService(Constants.ACTION_START_OR_RESUME, "dragon")
                binding.dragonCounter.apply{
                    isVisible = !JungleService.isDragonCounting
                    text = ""

                    if(!JungleService.isDragonCounting){
                        text = SPAWN_DRAGON.toString()
                        grayImage(binding.dragonImage)
                    }else{
                        colorImage(binding.dragonImage)
                    }
                }

            }

            baronCardView.setOnClickListener {
                sendCommandToService(Constants.ACTION_START_OR_RESUME, "baron")
                binding.baronCounter.apply{
                    isVisible = !JungleService.isBaronCounting
                    text = ""

                    if(!JungleService.isBaronCounting){
                        text = SPAWN_BARON.toString()
                        grayImage(baronImage)
                    }else{
                        colorImage(baronImage)
                    }
                }
            }

            raptorsCardView.setOnClickListener {
                sendCommandToService(Constants.ACTION_START_OR_RESUME, "raptors")
                binding.raptorsCounter.apply{
                    isVisible = !JungleService.isRaptorsCounting
                    text = ""

                    if(!JungleService.isRaptorsCounting){
                        text = SPAWN_RAPTORS.toString()
                        grayImage(raptorsImage)
                    }else{
                        colorImage(raptorsImage)
                    }
                }

            }

            wolvesCardView.setOnClickListener {
                sendCommandToService(Constants.ACTION_START_OR_RESUME, "wolves")
                binding.wolvesCounter.apply{
                    isVisible = !JungleService.isWolvesCounting
                    text = ""

                    if(!JungleService.isWolvesCounting){
                        text = SPAWN_WOLVES.toString()
                        grayImage(wolvesImage)
                    }else{
                        colorImage(wolvesImage)
                    }
                }
            }

            krugsCardView.setOnClickListener {
                sendCommandToService(Constants.ACTION_START_OR_RESUME, "krugs")
                binding.krugsCounter.apply{
                    isVisible = !JungleService.isKrugsCounting
                    text = ""

                    if(!JungleService.isKrugsCounting){
                        text = SPAWN_KRUGS.toString()
                        grayImage(krugsImage)
                    }else{
                        colorImage(krugsImage)
                    }
                }
            }

            grompCardView.setOnClickListener {
                sendCommandToService(Constants.ACTION_START_OR_RESUME, "gromp")
                binding.grompCounter.apply{
                    isVisible = !JungleService.isGrompCounting
                    text = ""

                    if(!JungleService.isGrompCounting){
                        text = SPAWN_GROMP.toString()
                        grayImage(grompImage)
                    }else{
                        colorImage(grompImage)
                    }
                }
            }

            scutter1CardView.setOnClickListener {
                sendCommandToService(Constants.ACTION_START_OR_RESUME, "scutter1")
                binding.scutterCounter1.apply{
                    isVisible = !JungleService.isScutter1Counting
                    text = ""

                    if(!JungleService.isScutter1Counting){
                        text = SPAWN_SCUTTER.toString()
                        grayImage(sc1Image)
                    }else{
                        colorImage(sc1Image)
                    }
                }
            }

            scutter2CardView.setOnClickListener {
                sendCommandToService(Constants.ACTION_START_OR_RESUME, "scutter2")
                binding.scuttercounter2.apply{
                    isVisible = !JungleService.isScutter2Counting
                    text = ""

                    if(!JungleService.isScutter2Counting){
                        text = SPAWN_SCUTTER.toString()
                        grayImage(sc2Image)
                    }else{
                        colorImage(sc2Image)
                    }
                }
            }
        }

        JungleService.countdownRed.observe(viewLifecycleOwner) {
            if(it < SPAWN_RED){
                if(it < 5){
                    animateCardView(binding.redCardView)
                }
                if(it == 0){
                    colorImage(binding.redImage)
                }
                binding.redCounter.apply {
                    setText(it.toString())
                    isVisible = true
                }
            }else if (it == SPAWN_RED){
                binding.redCounter.isVisible = false
            }
        }

        JungleService.countdownBlue.observe(viewLifecycleOwner) {
            if(it < Constants.SPAWN_BLUE){
                if(it < 5){
                    animateCardView(binding.blueCardView)
                }
                binding.blueCounter.apply {
                    setText(it.toString())
                    isVisible = true
                }
            }else if (it == Constants.SPAWN_BLUE){
                binding.blueCounter.isVisible = false
            }
        }

        JungleService.countdownDragon.observe(viewLifecycleOwner) {
            if(it < Constants.SPAWN_DRAGON){
                if(it < 5){
                    animateCardView(binding.dragonCardView)
                }
                binding.dragonCounter.apply {
                    setText(it.toString())
                    isVisible = true
                }
            }else if (it == Constants.SPAWN_DRAGON){
                binding.dragonCounter.isVisible = false
            }
        }

        JungleService.countdownBaron.observe(viewLifecycleOwner) {
            if(it < Constants.SPAWN_BARON){
                if(it < 5){
                    animateCardView(binding.baronCardView)
                }
                binding.baronCounter.apply {
                    setText(it.toString())
                    isVisible = true
                }
            }else if (it == Constants.SPAWN_BARON){
                binding.baronCounter.isVisible = false
            }
        }

        JungleService.countdownRaptors.observe(viewLifecycleOwner) {
            if(it < Constants.SPAWN_RAPTORS){
                if(it < 5){
                    animateCardView(binding.raptorsCardView)
                }
                binding.raptorsCounter.apply {
                    setText(it.toString())
                    isVisible = true
                }
            }else if (it == Constants.SPAWN_RAPTORS){
                binding.raptorsCounter.isVisible = false
            }
        }

        JungleService.countdownWolves.observe(viewLifecycleOwner) {
            if(it < Constants.SPAWN_WOLVES){
                if(it < 5){
                    animateCardView(binding.wolvesCardView)
                }
                binding.wolvesCounter.apply {
                    setText(it.toString())
                    isVisible = true
                }
            }else if (it == Constants.SPAWN_WOLVES){
                binding.wolvesCounter.isVisible = false
            }
        }

        JungleService.countdownKrugs.observe(viewLifecycleOwner) {
            if(it < Constants.SPAWN_KRUGS){
                if(it < 5){
                    animateCardView(binding.krugsCardView)
                }
                binding.krugsCounter.apply {
                    setText(it.toString())
                    isVisible = true
                }
            }else if (it == Constants.SPAWN_KRUGS){
                binding.krugsCounter.isVisible = false
            }
        }

        JungleService.countdownGromp.observe(viewLifecycleOwner) {
            if(it < Constants.SPAWN_GROMP){
                if(it < 5){
                    animateCardView(binding.grompCardView)
                }
                binding.grompCounter.apply {
                    setText(it.toString())
                    isVisible = true
                }
            }else if (it == Constants.SPAWN_GROMP){
                binding.grompCounter.isVisible = false
            }
        }

        JungleService.countdownScutter1.observe(viewLifecycleOwner) {
            if(JungleService.isScutter1Counting){
                grayImage(binding.redImage)
            }else{
                colorImage(binding.redImage)
            }

            if(it < Constants.SPAWN_SCUTTER){
                if(it < 5){
                    animateCardView(binding.scutter1CardView)
                }
                binding.scutterCounter1.apply {
                    setText(it.toString())
                    isVisible = true
                }
            }else if (it == Constants.SPAWN_SCUTTER){
                binding.scutterCounter1.isVisible = false
            }
        }

        JungleService.countdownScutter2.observe(viewLifecycleOwner) {
            if(it < Constants.SPAWN_SCUTTER){
                if(it < 5){
                    animateCardView(binding.scutter2CardView)
                }
                binding.scuttercounter2.apply {
                    setText(it.toString())
                    isVisible = true
                }
            }else if (it == Constants.SPAWN_SCUTTER){
                binding.scuttercounter2.isVisible = false
            }
        }
    }


    private fun grayImage(image: ImageView) {
        val matrix = ColorMatrix()
        matrix.setSaturation(0F)
        image.setColorFilter(ColorMatrixColorFilter(matrix))
    }

    private fun colorImage(image: ImageView){
        image.clearColorFilter()
    }

    private fun animateCardView(cardView: CardView) {
        cardView.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.shake
            )
        )

        vibrate()
    }

    private fun vibrate() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            vibrator?.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.EFFECT_DOUBLE_CLICK))
        }else{
            vibrator?.vibrate(50)
        }
    }

    private fun sendCommandToService(action: String, param: String) =
        Intent(requireContext(), JungleService::class.java).also {
            it.action = action
            it.putExtra("param", param)
            requireContext().startService(it)
        }

    private fun loadAd() {
        val builder = AdLoader.Builder(requireContext(), "ca-app-pub-4479200586800321/6961703752")

        builder.forNativeAd { nativeAd -> // You must call destroy on old ads when you are done with them,
            val adView = layoutInflater
                .inflate(R.layout.ad_unified_bigger, null) as NativeAdView
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

            override fun onAdLoaded() {
                binding.adFrame.apply {
                    alpha = 0f
                    //visibility = VISIBLE
                    animate().alpha(1f).setDuration(400).setListener(null)
                }
            }
        }).build()

        adLoader.loadAds(AdRequest.Builder().build(), 3)
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
/*        if (nativeAd.body == null) {
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
        }*/

        if (nativeAd.icon == null) {
            adView.iconView.visibility = View.GONE
        } else {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon.drawable
            )
            adView.iconView.visibility = View.VISIBLE
        }

/*        if (nativeAd.price == null) {
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
        }*/

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