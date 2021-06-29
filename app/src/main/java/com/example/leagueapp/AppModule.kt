package com.example.leagueapp

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.leagueapp.Constants.FAVORITE_DATABASE_NAME
import com.example.leagueapp.Constants.SEARCH_DATABASE_NAME
import com.example.leagueapp.database.FavoriteDAO
import com.example.leagueapp.database.FavoriteDatabase
import com.example.leagueapp.database.SearchDAO
import com.example.leagueapp.database.SearchesDatabase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.internal.managers.ApplicationComponentManager
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.migration.DisableInstallInCheck
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule{

    @Singleton
    @Provides
    fun provideSearchDatabase(@ApplicationContext context : Context) =
        Room.databaseBuilder(context, SearchesDatabase::class.java, SEARCH_DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideSearchDAO(appDatabase: SearchesDatabase): SearchDAO {
        return appDatabase.getSearchesDao()
    }

    @Singleton
    @Provides
    fun provideFavoriteDatabase(@ApplicationContext context : Context) =
        Room.databaseBuilder(context, FavoriteDatabase::class.java, FAVORITE_DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideFavoriteDAO(appDatabase: FavoriteDatabase): FavoriteDAO {
        return appDatabase.getSearchesDao()
    }

    @Singleton
    @Provides
    fun provideFirebaseInstance() = FirebaseFirestore.getInstance()
}