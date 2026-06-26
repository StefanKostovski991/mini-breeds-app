package com.kostovskiapps.minibreedsapp.data.api

import com.google.gson.annotations.SerializedName

data class DogBreedResponse(
    @SerializedName("message") val message: Map<String, List<String>>,
    @SerializedName("status") val status: String
)