package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroids: AsteroidsEntity)

    @Query("SELECT * FROM asteroids_table ORDER BY close_approach_date ASC")
    fun getAllAsteroids(): LiveData<List<AsteroidsEntity>>


    @Query("DELETE FROM asteroids_table WHERE close_approach_date < :today")
    suspend fun removeOldAsteroids(today:String)
}