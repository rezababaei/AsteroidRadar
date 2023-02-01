package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.api.PictureOfDay
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch


class MainViewModel(application: Application) : ViewModel() {


    private val database = AsteroidDatabase.getDatabase(application)
    private val asteroidRepository = AsteroidsRepository(database)

    init {
        viewModelScope.launch {
            asteroidRepository.getPictureOfDay()
            asteroidRepository.refreshAsteroids()
        }
    }

    private val _pictureOfDay = asteroidRepository.pictureOfDay
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay


    private val _asteroids = asteroidRepository.asteroids
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids



}