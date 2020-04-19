package com.romellbolton.simpleyelp

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call
import retrofit2.http.Header


public interface YelpService {

    @GET("businesses/search")
    fun searchRestaurants(
        @Header("Authorization") authHeader: String,
        @Query("term") searchTerm: String,
        @Query("location") location: String
    ): Call<YelpSearchResult>
}