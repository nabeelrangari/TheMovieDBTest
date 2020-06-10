package com.nabeel.themoviedbtest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.nabeel.themoviedbtest.base.BaseViewModel
import com.nabeel.themoviedbtest.data.database.entity.Upcoming
import com.nabeel.themoviedbtest.data.network.NetworkConnectionInterceptor
import com.nabeel.themoviedbtest.data.network.Resource
import com.nabeel.themoviedbtest.model.Movies
import com.nabeel.themoviedbtest.util.App.Companion.repository
import com.nabeel.themoviedbtest.util.AppConstant.POPULAR
import com.nabeel.themoviedbtest.util.AppConstant.TOPRATED
import com.nabeel.themoviedbtest.util.AppConstant.UPCOMING
import kotlinx.coroutines.Dispatchers
import retrofit2.Response

class MovieViewModel : BaseViewModel() {

    fun getMovies(page: Int, category: String) = liveData(Dispatchers.IO) {
        try {
            var response: Response<Movies>? = null
            emit(Resource.loading())
            when (category) {
                UPCOMING -> {
                    response = repository.getUpcomingMovies(page)
                }
                TOPRATED -> {
                    response = repository.getTopRatedMovies(page)
                }
                POPULAR -> {
                    response = repository.getPopularMovies(page)
                }
            }
            if (response!!.isSuccessful) {
                emit(Resource.success(response.body()))
            } else {
                emit(Resource.error(response.errorBody().toString()))
            }
        } catch (e: NetworkConnectionInterceptor.NoConnectivityException) {
            emit(Resource.error(e.localizedMessage))
        }
    }

    fun addAllMovie(input: List<Upcoming>) = liveData(Dispatchers.IO) {
        try {
            repository.addAllMovies(input)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(e.localizedMessage)
        }
    }

    fun getAllMovies(): LiveData<List<Upcoming>> =
        liveData(Dispatchers.IO) {
            emitSource(repository.getAllMovies())
        }
}