package com.udacity.asteroidradar.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.api.PictureOfDay
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch

enum class AsteroidFilter { TODAY, WEEK, SAVED }

class MainViewModel(application: Application) : ViewModel() {


    private val database = AsteroidDatabase.getDatabase(application)
    private val asteroidRepository = AsteroidsRepository(database)


    init {

        viewModelScope.launch {
            asteroidRepository.getPictureOfDay()
            asteroidRepository.refreshAsteroids()
        }
    }

    val asteroids= asteroidRepository.asteroidsWithFilter


    private val _pictureOfDay = asteroidRepository.pictureOfDay
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay


    fun onShowSavedAsteroidMenuClicked() {
        asteroidRepository.updateFilter( AsteroidFilter.SAVED)
    }

    fun onShowTodayAsteroidMenuClicked() {
        asteroidRepository.updateFilter( AsteroidFilter.TODAY)
    }

    fun onShowWeekAsteroidMenuClicked() {
        asteroidRepository.updateFilter( AsteroidFilter.WEEK)
    }

}