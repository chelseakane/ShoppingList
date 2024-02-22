package com.example.shoppinglist.di

import android.content.Context
import android.content.res.Resources
import androidx.room.Room
import com.example.shoppinglist.data.storage.AppDatabase
import com.example.shoppinglist.network.MealsAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "shopping-list-db"
        ).build()
    }

    @Provides
    fun provideResources(@ApplicationContext context: Context): Resources = context.resources

    @Provides
    fun provideApiInterface() = MealsAPI.mealsApiInterface
}