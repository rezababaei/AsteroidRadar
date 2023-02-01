package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface AsteroidApiService {
    @GET("planetary/apod?")
    suspend fun getPictureOfDay(
        @Query("api_key") apikey: String = BuildConfig.NASA_API_KEY,
    ): PictureOfDay


    @GET("neo/rest/v1/feed?")
    suspend fun getAsteroids(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apikey: String = BuildConfig.NASA_API_KEY,
    ): String
}

