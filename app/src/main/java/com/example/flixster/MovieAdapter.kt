package com.example.flixster

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler

class MovieAdapter(
    private val movies: List<Flix>,
    private val mListener: OnListFragmentInteractionListener?
    ): RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        var mItem: Flix? = null
        val imageView: ImageView = mView.findViewById(R.id.movie_image)
        val titleTextView: TextView = mView.findViewById(R.id.title_movie)
        val descriptionTextView: TextView = mView.findViewById(R.id.movie_description)

        override fun toString(): String {
            return "${titleTextView.text}"
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]

        holder.mItem = movie
        holder.titleTextView.text = movie.title
        holder.descriptionTextView.text = movie.description

        Glide.with(holder.mView).load("https://image.tmdb.org/t/p/w500/${movie.posterPath}")
            .into(holder.imageView)
        movie.posterPath?.let { Log.d("ImageUrl", it) }

        holder.mView.setOnClickListener{
            holder.mItem?.let { movie -> mListener?.onItemClick(movie) }
        }
    }

    override fun getItemCount() : Int {
        return movies.size
    }
}
