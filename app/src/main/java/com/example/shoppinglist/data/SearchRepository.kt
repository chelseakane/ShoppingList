package com.example.shoppinglist.data

import com.example.shoppinglist.models.MealsSearchResponse
import com.example.shoppinglist.network.MealsApiInterface
import retrofit2.Response
import javax.inject.Inject

class SearchRepository @Inject constructor(private val apiInterface: MealsApiInterface) {
    suspend fun searchMeals(mealName: String): Response<MealsSearchResponse> {
        return apiInterface.searchMeals(mealName)
    }
}