package com.example.leagueapp

import android.app.Application
import com.example.leagueapp.networking.model.repository.NewsRepository
import com.google.android.gms.ads.RequestConfiguration
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.merakianalytics.orianna.Orianna
import com.merakianalytics.orianna.types.common.Region
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.util.*

@HiltAndroidApp
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        NewsRepository.readNews()

        RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("DCB08AD74B27BF7C8694E3ACA6B194C9"))


/*        val optiones = FirebaseOptions.Builder()
                .setApiKey("AIzaSyC-kWAttGax6yAn37WgrlZOzTSnAr3UJYw")
                .setApplicationId("1:189825605818:android:96a8fd0d49b728a6406283")
                .setDatabaseUrl("https://reactcrud-a6f49-default-rtdb.firebaseio.com")
                .build()

        FirebaseApp.getInstance().delete()
        FirebaseApp.initializeApp(applicationContext, optiones)*/
    }
}