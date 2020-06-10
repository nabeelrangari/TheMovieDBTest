package com.nabeel.themoviedbtest.repository

import android.content.Context
import com.nabeel.themoviedbtest.data.database.AppDatabase
import com.nabeel.themoviedbtest.data.database.entity.Upcoming
import com.nabeel.themoviedbtest.data.network.NetworkService
import com.nabeel.themoviedbtest.data.network.Networking
import com.nabeel.themoviedbtest.util.AppConstant

class Repository constructor(context: Context) {
    private val api: NetworkService = Networking.getApiService(context, false)
    private val database: AppDatabase = AppDatabase.getDatabase(context)
    private val upcoming = database.upcomingDao()

    suspend fun getUpcomingMovies(page: Int) = api.getUpcomingMovies(AppConstant.API_KEY, page)

    suspend fun getPopularMovies(page: Int) = api.getPopularMovies(AppConstant.API_KEY, page)

    suspend fun getTopRatedMovies(page: Int) = api.getTopRatedMovies(AppConstant.API_KEY, page)

    suspend fun getMovieDetails(id: String) = api.getMovieDetails(id, AppConstant.API_KEY)

    fun addAllMovies(req: List<Upcoming>) = upcoming.addAllMovieToDb(req)

    fun getAllMovies() = upcoming.getAllMovieList()

    fun updateMovie(req: Upcoming) = upcoming.update(req)
}