package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.api.PictureOfDay
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*

enum class AsteroidFilters { TODAY, WEEK, SAVED }

class MainViewModel(application: Application) : ViewModel() {


    private val database = AsteroidDatabase.getDatabase(application)
    private val asteroidRepository = AsteroidsRepository(database)
    private val filter = MutableLiveData(AsteroidFilters.SAVED)


    init {

        viewModelScope.launch {
            asteroidRepository.getPictureOfDay()
            asteroidRepository.refreshAsteroids()
        }
    }

    private val _pictureOfDay = asteroidRepository.pictureOfDay
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay


    // when the filter changes this transformation happens automatically
    private val _asteroids = Transformations.switchMap(filter) {
        when (it) {
            AsteroidFilters.TODAY -> asteroidRepository.todayAsteroids
            AsteroidFilters.WEEK -> asteroidRepository.weekAsteroids
            else -> asteroidRepository.asteroids
        }
    }
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids


    fun onShowSavedAsteroidMenuClicked() {
        filter.value = AsteroidFilters.SAVED
    }


    fun onShowTodayAsteroidMenuClicked() {
        filter.value = AsteroidFilters.TODAY
    }

    fun onShowWeekAsteroidMenuClicked() {
        filter.value = AsteroidFilters.WEEK
    }

}