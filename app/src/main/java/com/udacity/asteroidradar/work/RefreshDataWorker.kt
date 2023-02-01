package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidDatabase.Companion.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

     companion object {
         const val WORK_NAME = "RefreshDataWorker"
     }

    /**
     * This task runs one time a day,
     * when the device is connected to wifi, idle and charging
     */
     override suspend fun doWork(): Result {
         val database = getDatabase(applicationContext)
         val asteroidRepository = AsteroidsRepository(database)
         return try {
             asteroidRepository.refreshAsteroids()
             Result.success()
         }catch (e: HttpException){
             Result.failure()
         }
     }


 }