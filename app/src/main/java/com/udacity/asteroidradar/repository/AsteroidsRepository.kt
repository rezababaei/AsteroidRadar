package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.NetworkModules
import com.udacity.asteroidradar.api.PictureOfDay
import com.udacity.asteroidradar.api.util.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.util.getToday
import com.udacity.asteroidradar.api.util.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.ui.main.AsteroidFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

/**
 * Repository for fetching the devbyte videos from network and storing them on disk.
 */
class AsteroidsRepository(private val database: AsteroidDatabase) {

    private val _filterType = MutableLiveData(AsteroidFilter.SAVED)

    /**
     * A list of asteroids that can be shown on the screen.
     */
    private val _asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAllAsteroids()) {
            it.asDomainModel()
        }


    val asteroidsWithFilter: LiveData<List<Asteroid>> = Transformations.switchMap(_filterType)
    {
        when (it) {
            AsteroidFilter.WEEK ->
                Transformations.map(_asteroids) { asteroidList ->
                    asteroidList.filter { asteroid -> asteroid.closeApproachDate > getToday() }
                }

            AsteroidFilter.TODAY ->
                Transformations.map(_asteroids) { asteroidList ->
                    asteroidList.filter { asteroid -> asteroid.closeApproachDate == getToday() }
                }

            AsteroidFilter.SAVED -> _asteroids
            else -> throw IllegalArgumentException("")
        }
    }

    fun updateFilter(filterType: AsteroidFilter) {
        _filterType.value = filterType
    }


    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay


    suspend fun getPictureOfDay() {
        withContext(Dispatchers.IO) {
            try {
                val pictureOfDay = NetworkModules.asteroidApi.getPictureOfDay()
                _pictureOfDay.postValue(pictureOfDay)
            } catch (e: Exception) {
                Timber.e("Error: $e")
            }
        }
    }

    /**
     * Refresh the asteroids stored in the offline cache.
     *
     * To actually load the asteroids for use, observe [asteroids]
     */
    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val dates = getNextSevenDaysFormattedDates()
                val asteroids = NetworkModules.asteroidApi.getAsteroids(dates.first(), dates.last())
                val jsObj = JSONObject(asteroids)

                val list = parseAsteroidsJsonResult(jsObj)
                database.asteroidDao.insertAll(*list.asDatabaseModel())
                database.asteroidDao.removeOldAsteroids(dates.first())
            } catch (e: Exception) {
                Timber.e("Error: $e")
            }
        }
    }

}