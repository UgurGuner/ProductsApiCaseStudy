package com.eugurguner.productsapicasestudy.di

import android.content.Context
import androidx.room.Room
import com.eugurguner.productsapicasestudy.data.local.db.AppDatabase
import com.eugurguner.productsapicasestudy.data.network.AppNetworkService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit =
        Retrofit
            .Builder()
            .baseUrl("https://5fc9346b2af77700165ae514.mockapi.io/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides
    @Singleton
    fun provideProductService(
        retrofit: Retrofit
    ): AppNetworkService = retrofit.create(AppNetworkService::class.java)

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext appContext: Context
    ): AppDatabase =
        Room
            .databaseBuilder(
                context = appContext,
                klass = AppDatabase::class.java,
                name = "product_database"
            ).build()
}