package com.app.realogyassesment.di

import com.app.realogyassesment.BuildConfig
import com.app.realogyassesment.common.UnsafeOkHttpClient
import com.app.realogyassesment.data.remote.CharactersApi
import com.app.realogyassesment.data.repository.CharactersRepositoryImpl
import com.app.realogyassesment.domain.repository.CharactersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCharactersApi(): CharactersApi {
        val client = UnsafeOkHttpClient.getUnsafeOkHttpClient()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CharactersApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCharactersRepository(api: CharactersApi): CharactersRepository {
        return CharactersRepositoryImpl(api = api)
    }
}