package com.saheralsous.dogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saheralsous.dogs.model.DogBreed

class DetailViewModel :ViewModel() {

    val dogLiveData = MutableLiveData<DogBreed>()

    fun fetch(){
        val dog = DogBreed("1", "Corgi", "15 years", "BreedGroup", "bredFor", "termperament", "")
        dogLiveData.value = dog
    }
}