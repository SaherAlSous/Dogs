package com.saheralsous.dogs.model

import io.reactivex.Single
import retrofit2.http.GET

interface DogsApi {
    @GET("DevTides/DogsApi/master/dogs.json")
    fun getDogs(): Single<List<DogBreed>>
}



/**
 * API link = https://raw.githubusercontent.com/DevTides/DogsApi/master/dogs.json
 */