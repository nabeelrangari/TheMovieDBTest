package com.nabeel.themoviedbtest.view.activity

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.nabeel.themoviedbtest.R
import com.nabeel.themoviedbtest.base.BaseActivity
import com.nabeel.themoviedbtest.data.database.entity.Upcoming
import com.nabeel.themoviedbtest.data.network.Status
import com.nabeel.themoviedbtest.model.Genre
import com.nabeel.themoviedbtest.model.Result
import com.nabeel.themoviedbtest.util.AppConstant
import com.nabeel.themoviedbtest.util.Utils
import com.nabeel.themoviedbtest.viewmodel.MovieDetailsViewModel
import kotlinx.android.synthetic.main.activity_movie_details.*

class MovieDetailsActivity : BaseActivity<MovieDetailsViewModel>() {

    var movie: Result? = null
    var category: String = ""
    override fun layoutId(): Int = R.layout.activity_movie_details
    override fun providerVMClass(): Class<MovieDetailsViewModel> = MovieDetailsViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        category = intent.getStringExtra("category") as String
        movie = intent.getSerializableExtra("movie") as Result?
        mViewModel?.movieDetails(movie?.id.toString())?.observe(this, Observer { networkResource ->
            when (networkResource.status) {
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    val movieDetails = networkResource.data
                    movieDetails.let {
                        progressBar.visibility = View.GONE
                        setData(movieDetails!!)
                        //updateMovie(movieDetails)
                    }
                }
                Status.ERROR -> {
                    progressBar.visibility = View.GONE
                    setData(movie!!)
                }
            }
        })
    }

    private fun updateMovie(item: Result) {
        val upcoming = Upcoming(
            0,
            item.id!!,
            item.title!!,
            item.posterPath,
            item.voteCount,
            item.popularity,
            item.backdropPath,
            item.voteAverage,
            item.releaseDate,
            item.budget,
            item.revenue,
            item.runtime,
            item.tagline,
            item.overview,
            item.genres,
            item.spokenLanguages,
            item.category
        )

        mViewModel?.updateMovie(upcoming)?.observe(this, Observer {
            Log.e("Movie", "update - $it")
        })
    }

    private fun setData(movieDetails: Result) {
        toolbar.title = movieDetails.title
        toolbar.setTitleMargin(-20, 0, 0, 0)
        collapsingToolbar.title = movieDetails.title
        tv_title.text = movieDetails.title
        tagLine.text = "Tag Line : " + movieDetails.tagline
        genres.text = "Genre : " + getGenres(movieDetails)
        duration.text = "Duration : " + getDuration(movieDetails)
        tv_overview.text = getOverview(movieDetails.overview)
        rating.text = movieDetails.voteAverage.toString()
        vote.text = movieDetails.voteCount.toString()
        popularity.text = movieDetails.popularity.toString()
        releaseDate.text = "Release Date : " + movieDetails.releaseDate

        if (!movieDetails.backdropPath.isNullOrEmpty()) {
            val fullImageUrl = AppConstant.IMAGE_BASE_URL + movieDetails.backdropPath
            Glide.with(this)
                .load(fullImageUrl)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .apply(RequestOptions.centerCropTransform())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(toolbarImage)
        }

        if (!movieDetails.posterPath.isNullOrEmpty()) {
            val fullImageUrl = AppConstant.IMAGE_BASE_URL + movieDetails.posterPath
            Glide.with(this)
                .load(fullImageUrl)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .apply(RequestOptions.centerCropTransform())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(profileImage)
        }
    }

    private fun getDuration(movie: Result): String? {
        val runtime: Int = movie.runtime
        return if (runtime <= 0) "-" else resources.getQuantityString(
            R.plurals.duration,
            runtime,
            runtime
        )
    }

    private fun getOverview(overview: String?): String? {
        return if (TextUtils.isEmpty(overview)) "-" else overview
    }

    private fun getGenres(movie: Result): String? {
        var genres = "Thriller"
        if (movie.genres != null && movie.genres.size > 0) {
            for (i in movie.genres.indices) {
                val genre: Genre = movie.genres[i]
                genres += genre.name.toString() + ", "
            }
            genres = removeTrailingComma(genres)
        }
        return if (genres.isEmpty()) "-" else genres
    }

    @NonNull
    private fun removeTrailingComma(input: String): String {
        var text = input
        text = text.trim { it <= ' ' }
        if (text.endsWith(",")) {
            text = text.substring(0, text.length - 1)
        }
        return text
    }

    override fun setListeners() {
        super.setListeners()
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun networkAvailable() {

    }

    override fun networkUnavailable() {
        Utils.showSnackBar(ll_root, resources.getString(R.string.str_nointernetconn))
    }
}
