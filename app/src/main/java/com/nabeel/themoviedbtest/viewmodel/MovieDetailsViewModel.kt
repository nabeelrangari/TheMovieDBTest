package com.nabeel.themoviedbtest.viewmodel


import androidx.lifecycle.liveData
import com.nabeel.themoviedbtest.base.BaseViewModel
import com.nabeel.themoviedbtest.data.database.entity.Upcoming
import com.nabeel.themoviedbtest.data.network.NetworkConnectionInterceptor
import com.nabeel.themoviedbtest.data.network.Resource
import com.nabeel.themoviedbtest.util.App.Companion.repository
import kotlinx.coroutines.Dispatchers

class MovieDetailsViewModel : BaseViewModel() {

    fun movieDetails(id: String) = liveData(Dispatchers.IO) {
        try {
            emit(Resource.loading())
            val response = repository.getMovieDetails(id)
            if (response.isSuccessful) {
                emit(Resource.success(response.body()))
            } else {
                emit(Resource.error(response.errorBody().toString()))
            }
        } catch (e: NetworkConnectionInterceptor.NoConnectivityException) {
            emit(Resource.error(e.localizedMessage))
        }
    }

    fun updateMovie(input: Upcoming) = liveData(Dispatchers.IO) {
        try {
            repository.updateMovie(input)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(e.localizedMessage)
        }
    }
}