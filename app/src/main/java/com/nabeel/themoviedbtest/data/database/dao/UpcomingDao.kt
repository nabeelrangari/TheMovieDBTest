package com.nabeel.themoviedbtest.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nabeel.themoviedbtest.data.database.entity.Upcoming

@Dao
interface UpcomingDao {
    @Query("SELECT * from UPCOMING ORDER BY id DESC")
    fun getAllMovieList(): LiveData<List<Upcoming>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAllMovieToDb(req: List<Upcoming>)

    @Query("DELETE FROM Upcoming")
    fun deleteAll()

    @Update
    fun update(upcoming: Upcoming)

}