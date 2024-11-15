package com.example.flixster
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import org.json.JSONException


private const val API_Key = "a07e22bc18f5cb106bfe4cc1f83ad8ed"

class FlixsterFragment : Fragment(), OnListFragmentInteractionListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragflix,container,false)
        val progressBar = view.findViewById<View>(R.id.progress) as ContentLoadingProgressBar
        val recyclerView = view.findViewById<View>(R.id.list_of_movies) as RecyclerView
        val context = view.context
        recyclerView.layoutManager = GridLayoutManager(context,1)
        updateAdapter(progressBar, recyclerView)
        return view
    }

    private fun updateAdapter(progressBar: ContentLoadingProgressBar, recyclerView: RecyclerView){
        progressBar.show()

        //Creates and sets up an AsyncHTTPClient()
        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api_key"] = API_Key

        client["https://api.themoviedb.org/3/movie/now_playing?&api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed",
            params,
            object: JsonHttpResponseHandler()
            {
                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    response: String?,
                    throwable: Throwable?
                ) {
                    progressBar.hide()

                    throwable?.message?.let{
                        if (response != null) {
                            Log.e("FlixsterFragment", response)
                        }
                    }
                }

                override fun onSuccess(
                    statusCode: Int,
                    headers: Headers?,
                    json: JsonHttpResponseHandler.JSON
                ) {
                    progressBar.hide()
                    Log.d("test", json.toString())

                    try {
                        // Correctly access the results array
                        val resultsJson = json.jsonObject.getJSONArray("results")
                        val moviesRawJSON: String = resultsJson.toString()

                        // Use Gson to parse the JSON into a List of Flixster objects
                        val gson = Gson()
                        val arrayMovieType = object : TypeToken<List<Flix>>() {}.type
                        val movie: List<Flix> = gson.fromJson(moviesRawJSON, arrayMovieType)

                        // Set the adapter with the parsed data
                        recyclerView.adapter = MovieAdapter(movie,this@FlixsterFragment)
                    } catch (e: JSONException) {
                        Log.e("FlixsterFragment", "JSON parsing error: ${e.message}")
                        Toast.makeText(context, "Error parsing data", Toast.LENGTH_LONG).show()
                    }
                }

            }

        ]
    }

    override fun onItemClick(item: Flix) {
        Toast.makeText(context, "Movie: " + item.title, Toast.LENGTH_LONG).show()
    }

}