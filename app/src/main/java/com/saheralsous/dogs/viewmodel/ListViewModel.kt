package com.saheralsous.dogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saheralsous.dogs.model.DogBreed

class ListViewModel : ViewModel() {
    /**
     * live data variables
     */

    val dogs = MutableLiveData<List<DogBreed>>() //getting the data from the source in
    val dogsLoadError = MutableLiveData<Boolean>() //in case there is a load error = true
    val loading =
        MutableLiveData<Boolean>() // in case it is loading = true --> spinner will be spinning

    fun refresh() {
        val dog1 = DogBreed("1", "Corgi", "15 years", "BreedGroup", "bredFor", "termperament", "")
        val dog2 = DogBreed("2", "Labrador", "10 years", "BreedGroup", "bredFor", "termperament", "")
        val dog3 = DogBreed("3", "Rotwailer", "20 years", "BreedGroup", "bredFor", "termperament", "")

        val dogList = arrayListOf(dog1, dog2, dog3)

        //passing data to the LiveData values
        dogs.value = dogList
        dogsLoadError.value = false
        loading.value = false
    }
}