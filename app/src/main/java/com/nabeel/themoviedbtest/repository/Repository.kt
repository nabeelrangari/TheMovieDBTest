package com.nabeel.themoviedbtest.repository

import android.content.Context
import com.nabeel.themoviedbtest.data.network.NetworkService
import com.nabeel.themoviedbtest.data.network.Networking
import com.nabeel.themoviedbtest.util.AppConstant

class Repository constructor(context: Context) {
    private val api: NetworkService = Networking.getApiService(context, false)

    suspend fun getUpcomingMovies(page: Int) = api.getUpcomingMovies(AppConstant.API_KEY, page)

    suspend fun getPopularMovies(page: Int) = api.getPopularMovies(AppConstant.API_KEY, page)

    suspend fun getTopRatedMovies(page: Int) = api.getTopRatedMovies(AppConstant.API_KEY, page)

    suspend fun getMovieDetails(id: String) = api.getMovieDetails(id, AppConstant.API_KEY)

}