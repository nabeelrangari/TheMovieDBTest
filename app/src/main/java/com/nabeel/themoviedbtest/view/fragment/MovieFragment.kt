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
import com.nabeel.themoviedbtest.data.network.Status
import com.nabeel.themoviedbtest.model.Result
import com.nabeel.themoviedbtest.util.OnLoadMoreListener
import com.nabeel.themoviedbtest.util.RecyclerViewLoadMoreScroll
import com.nabeel.themoviedbtest.view.adapter.MovieListAdapter
import com.nabeel.themoviedbtest.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.fragment_movie.*

class MovieFragment(context: Context) : BaseFragment<MovieViewModel>() {

    override fun providerVMClass(): Class<MovieViewModel> = MovieViewModel::class.java
    private lateinit var rootView: View
    private var movieList: ArrayList<Result?> = ArrayList()
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
        adapter = MovieListAdapter(movieList)
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

    private fun setData() {
        Log.e("TAG", "categoryStr - $categoryStr")
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
                            adapter.addData(movieList)
                        }
                    }
                }
                Status.ERROR -> {
                    Log.e("TAG", "Error loading data from network")
                    progressBar.visibility = View.GONE
                }
            }
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