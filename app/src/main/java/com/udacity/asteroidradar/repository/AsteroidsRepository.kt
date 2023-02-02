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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

/**
 * Repository for fetching the devbyte videos from network and storing them on disk.
 */
class AsteroidsRepository(private val database: AsteroidDatabase) {


    /**
     * A list of asteroids that can be shown on the screen.
     */
    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDatabaseDao.getAllAsteroids()) {
            it.asDomainModel()
        }

    /**
     * A list of today's asteroids that can be shown on the screen.
     */
    val todayAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(asteroids) { asteroidList ->
            asteroidList.filter { asteroid ->
                asteroid.closeApproachDate == getToday()
            }
        }

    /**
     * A list of week's asteroids that can be shown on the screen.
     */
    val weekAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(asteroids) { asteroidList ->
            asteroidList.filter { asteroid ->
                asteroid.closeApproachDate > getToday()
            }
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
                database.asteroidDatabaseDao.insertAll(*list.asDatabaseModel())

                database.asteroidDatabaseDao.removeOldAsteroids(dates.first())
            } catch (e: Exception) {
                Timber.e("Error: $e")
            }
        }
    }

}