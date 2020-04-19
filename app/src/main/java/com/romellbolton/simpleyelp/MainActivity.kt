package com.romellbolton.simpleyelp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "MainActivity"
private const val BASE_URL = "https://api.yelp.com/v3/"
private const val API_KEY =
    "oRnKRjv2wKDdMTRsAf5BPsiVdRRlFydbhQvkjQ8NMVS_gV_D7YT8_7oB60G5iCqKjXgCWLHa7-a_9V4Q_w32R6T0BM_8aRKwYTMzW-aT3XbC3Fi2ZIaHQxsBXdXxW3Yx"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val restaurants = mutableListOf<YelpRestaurant>()
        val adapter = RestaurantAdapter(this, restaurants)
        rvRestaurants.adapter = adapter
        rvRestaurants.layoutManager = LinearLayoutManager(this)

        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build()

        val yelpService = retrofit.create(YelpService::class.java)
        yelpService.searchRestaurants("Bearer $API_KEY", "Deep Dish Pizza", "Chicago")
            .enqueue(object : Callback<YelpSearchResult> {
                override fun onResponse(
                    call: Call<YelpSearchResult>,
                    response: Response<YelpSearchResult>
                ) {
                    Log.i(TAG, "onFailure $response")
                    val body = response.body()
                    if (body == null) {
                        Log.w(TAG, "Did not receive valid response body from YELP API... exiting")
                        return
                    }
                    restaurants.addAll(body.restaurants)
                    adapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<YelpSearchResult>, t: Throwable) {
                    Log.i(TAG, "onResponse $t")
                }

            })
    }
}

