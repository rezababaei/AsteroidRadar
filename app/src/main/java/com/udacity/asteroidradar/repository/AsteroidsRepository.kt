package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.NetworkModules
import com.udacity.asteroidradar.api.PictureOfDay
import com.udacity.asteroidradar.api.util.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.util.parseAsteroidsJsonResult

import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber
import java.io.IOException

/**
 * Repository for fetching the devbyte videos from network and storing them on disk.
 */
class AsteroidsRepository(private val database: AsteroidDatabase) {


    /**
     * A playlist of videos that can be shown on the screen.
     */
    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDatabaseDao.getAllAsteroids()) {
            it.asDomainModel()
        }

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay



    suspend fun getPictureOfDay() {
        withContext(Dispatchers.IO) {
            val pictureOfDay = NetworkModules.asteroidApi.getPictureOfDay()
            _pictureOfDay.postValue(pictureOfDay)
        }
    }

    /**
     * Refresh the video stored in the offline cache.
     *
     * To actually load the videos for use, observe [asteroids]
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
            } catch (e: IOException) {
                Timber.tag("TAG_MainViewModel").d("$e No Internet Connection")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}