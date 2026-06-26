package com.kostovskiapps.minibreedsapp.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface DogApiService {
    @GET("breeds/list/all")
    suspend fun getBreeds(): DogBreedResponse

    companion object {
        private const val BASE_URL = "https://dog.ceo/api/"

        val instance: DogApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DogApiService::class.java)
        }
    }
}