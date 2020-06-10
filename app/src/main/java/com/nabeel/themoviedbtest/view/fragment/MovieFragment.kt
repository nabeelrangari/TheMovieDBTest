package com.nabeel.themoviedbtest.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nabeel.themoviedbtest.R
import com.nabeel.themoviedbtest.base.BaseFragment
import com.nabeel.themoviedbtest.data.database.entity.Upcoming
import com.nabeel.themoviedbtest.data.network.Status
import com.nabeel.themoviedbtest.model.Genre
import com.nabeel.themoviedbtest.model.Result
import com.nabeel.themoviedbtest.model.SpokenLanguage
import com.nabeel.themoviedbtest.util.Communicate
import com.nabeel.themoviedbtest.util.OnLoadMoreListener
import com.nabeel.themoviedbtest.util.RecyclerViewLoadMoreScroll
import com.nabeel.themoviedbtest.view.activity.HomeActivity
import com.nabeel.themoviedbtest.view.adapter.MovieListAdapter
import com.nabeel.themoviedbtest.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.fragment_movie.*
import java.util.*
import kotlin.collections.ArrayList


class MovieFragment(context: Context) : BaseFragment<MovieViewModel>() {

    override fun providerVMClass(): Class<MovieViewModel> = MovieViewModel::class.java
    private lateinit var rootView: View
    private var movieList: ArrayList<Result?> = ArrayList()
    private lateinit var searchMovieList: ArrayList<Result?>
    private var searchTitleList: ArrayList<String> = ArrayList()
    private var genreList: ArrayList<Genre> = ArrayList()
    private var sLanguageList: ArrayList<SpokenLanguage> = ArrayList()

    private var loadMoreMovieList: ArrayList<Result?> = ArrayList()

    private lateinit var recyclerView: RecyclerView
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var scrollListener: RecyclerViewLoadMoreScroll

    private lateinit var adapter: MovieListAdapter
    private var categoryStr: String = ""
    private var page: Int = 1
    private var totalPages: Int = 0

    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_movie, parent, false)
        recyclerView = rootView.findViewById(R.id.recyclerView)
        mLayoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.setHasFixedSize(true)
        adapter = MovieListAdapter(categoryStr, movieList, requireActivity())
        recyclerView.adapter = adapter
        setRVScrollListener()
        setData()
        return rootView
    }

    companion object {
        fun newInstance(category: String, context: Context) =
            MovieFragment(context).apply {
                this.categoryStr = category
            }
    }

    private fun setRVScrollListener() {
        scrollListener = RecyclerViewLoadMoreScroll(mLayoutManager as GridLayoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
                if (page <= totalPages)
                    loadMore(page)
            }
        })

        recyclerView.addOnScrollListener(scrollListener)
    }

    override fun setListeners() {
        super.setListeners()

        (activity as HomeActivity?)?.setOnSearchListener(object : Communicate {
            override fun getSearchQuery(query: String) {
                Log.e("Fragment", "query - $query")
                if (query.isNotEmpty())
                    searchMovie(query)
                else
                    adapter.setSearchMovieList(movieList)
            }
        })
    }

    private fun searchMovie(query: String) {
        searchMovieList = ArrayList()
        searchTitleList = ArrayList()
        for (item in movieList) {
            val title = item?.title?.toLowerCase(Locale.getDefault())
            if (!searchTitleList.contains(title)) {
                if (title!!.contains(query)) {
                    searchTitleList.add(title)
                    searchMovieList.add(item)
                }
            }

        }
        Log.e("movies", "itemsCells - ${searchMovieList.size}")
        if (searchMovieList.size > 0) {
            adapter.setSearchMovieList(searchMovieList)
        }
    }

    private fun setData() {
        mViewModel?.getMovies(page, categoryStr)?.observe(this, Observer { networkResource ->
            when (networkResource.status) {
                Status.LOADING -> {
                    Log.e("TAG", "Loading data from network")
                    progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    val topRatedMovies = networkResource.data
                    topRatedMovies.let {
                        Log.e("movies", "result size - " + topRatedMovies?.results?.size)
                        Log.e("movies", "page - $page")
                        progressBar.visibility = View.GONE

                        topRatedMovies?.results?.let { it1 ->
                            page++
                            totalPages = topRatedMovies.totalPages!!
                            movieList.addAll(it1)
                            Log.e("movies", "movieList size - " + movieList.size)

                            adapter.addData(movieList)
                            addAllMovies(movieList)
                        }
                    }
                }
                Status.ERROR -> {
                    Log.e("TAG", "Error loading data from network")
                    progressBar.visibility = View.GONE
                    getAllMovies()
                }
            }
        })
    }

    private fun addAllMovies(movieList: ArrayList<Result?>) {
        val upcomingList: MutableList<Upcoming> = ArrayList()
        for (item in movieList) {
            val upcoming = Upcoming(
                0,
                item?.id!!,
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
                categoryStr
            )
            upcomingList.add((upcoming))
        }

        mViewModel?.addAllMovie(upcomingList)?.observe(this, Observer {
            Log.e("Fragment", "upcomingList size - ${upcomingList.size}")

        })
    }

    private fun getAllMovies() {
        mViewModel?.getAllMovies()?.observe(this, Observer {
            Log.e("Fragment", "upcomingList size - ${it.size}")
            for (item in it) {
                if (item.category == categoryStr) {
                    if (item.genres == null) {
                        genreList = ArrayList()
                    } else {
                        genreList = item.genres!!
                    }
                    if (item.spokenLanguages == null) {
                        sLanguageList = ArrayList()
                    } else {
                        item.spokenLanguages.let {
                            sLanguageList = item.spokenLanguages!!
                        }
                    }
                    val result = Result(
                        item.popularity,
                        item.voteCount,
                        false,
                        item.poster,
                        item.id,
                        false,
                        item.backdropPath,
                        "",
                        item.title,
                        null,
                        item.title,
                        item.voteAverage,
                        item.overview,
                        item.releaseDate,
                        item.budget!!,
                        genreList,
                        "",
                        item.revenue!!,
                        item.runtime!!,
                        sLanguageList,
                        "",
                        item.tagline,
                        item.category
                    )
                    movieList.add(result)
                }
            }
            adapter.addData(movieList)
            addAllMovies(movieList)
        })
    }

    private fun loadMore(pageNumber: Int) {
        //Add the Loading View
        adapter.addLoadingView()
        loadMoreMovieList = ArrayList()

        mViewModel?.getMovies(pageNumber, categoryStr)?.observe(this, Observer { networkResource ->
            when (networkResource.status) {
                Status.LOADING -> {
                    Log.e("TAG", "Loading data from network")
                    progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    val topRatedMovies = networkResource.data
                    topRatedMovies.let {
                        Log.e("loadMore", "result size - " + topRatedMovies?.results?.size)
                        Log.e("loadMore", "page - $page")
                        progressBar.visibility = View.GONE

                        topRatedMovies?.results?.let { it1 ->
                            page++
                            loadMoreMovieList.addAll(it1)

                            //Remove the Loading View
                            adapter.removeLoadingView()
                            //We adding the data to our main ArrayList
                            adapter.addData(loadMoreMovieList)
                            //Change the boolean isLoading to false
                            scrollListener.setLoaded()
                            //Update the recyclerView in the main thread
                            recyclerView.post {
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
                Status.ERROR -> {
                    Log.e("TAG", "Error loading data from network")
                    progressBar.visibility = View.GONE
                    scrollListener.setLoaded()
                }
            }
        })
    }

}