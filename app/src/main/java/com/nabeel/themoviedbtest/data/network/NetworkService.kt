package com.nabeel.themoviedbtest.data.network

import com.nabeel.themoviedbtest.model.Movies
import com.nabeel.themoviedbtest.model.Result
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkService {

    @GET("3/movie/top_rated")
    suspend fun getTopRatedMovies(@Query("api_key") api_key: String, @Query("page") page: Int): Response<Movies>

    @GET("3/movie/popular")
    suspend fun getPopularMovies(@Query("api_key") api_key: String, @Query("page") page: Int): Response<Movies>

    @GET("3/movie/upcoming")
    suspend fun getUpcomingMovies(@Query("api_key") api_key: String, @Query("page") page: Int): Response<Movies>

    @GET("3/movie/{id}")
    suspend fun getMovieDetails(@Path("id") id: String, @Query("api_key") api_key: String): Response<Result>
}