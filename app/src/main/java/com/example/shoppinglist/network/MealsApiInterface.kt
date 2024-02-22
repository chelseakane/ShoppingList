package com.example.shoppinglist.network

import com.example.shoppinglist.models.MealsSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MealsApiInterface {
    @GET("search.php")
    suspend fun searchMeals(
        @Query("s") mealName: String
    ): Response<MealsSearchResponse>
}
