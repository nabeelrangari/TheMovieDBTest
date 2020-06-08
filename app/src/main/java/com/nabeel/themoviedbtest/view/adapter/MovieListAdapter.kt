package com.nabeel.themoviedbtest.view.adapter

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.nabeel.themoviedbtest.R
import com.nabeel.themoviedbtest.model.Result
import com.nabeel.themoviedbtest.util.AppConstant
import com.nabeel.themoviedbtest.view.activity.MovieDetailsActivity
import kotlinx.android.synthetic.main.item_list_grid_movie.view.*
import kotlinx.android.synthetic.main.progress_loading.view.*

class MovieListAdapter(var movieList: ArrayList<Result?>, var activity: Activity) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var mContext: Context

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
        return if (viewType == AppConstant.VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_grid_movie, parent, false)
            ItemViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(mContext).inflate(R.layout.progress_loading, parent, false)
            view.progressbar.indeterminateDrawable.setColorFilter(
                Color.WHITE,
                PorterDuff.Mode.MULTIPLY
            )
            LoadingViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == AppConstant.VIEW_TYPE_ITEM) {

            val movie = movieList[position]

            Glide.with(mContext)
                .load(AppConstant.IMAGE_BASE_URL + movie?.posterPath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_placeholder)
                .placeholder(R.drawable.ic_placeholder)
                .into(holder.itemView.ic_poster)

            holder.itemView.setOnClickListener {
                val options = ActivityOptions.makeSceneTransitionAnimation(activity)
                val intent = Intent(mContext, MovieDetailsActivity::class.java)
                intent.putExtra("id", movie?.id)
                mContext.startActivity(intent, options.toBundle())
            }

        }
    }

    fun addData(dataViews: ArrayList<Result?>) {
        this.movieList.addAll(movieList.size, dataViews)
        notifyDataSetChanged()
    }

    fun setSearchMovieList(dataViews: ArrayList<Result?>) {
        this.movieList = dataViews
        notifyDataSetChanged()
    }

    fun addLoadingView() {
        //add loading item
        Handler().post {
            movieList.add(null)
            notifyItemInserted(movieList.size - 1)
        }
    }

    fun removeLoadingView() {
        //Remove loading item
        if (movieList.size != 0) {
            movieList.removeAt(movieList.size - 1)
            notifyItemRemoved(movieList.size)
        }
    }

}