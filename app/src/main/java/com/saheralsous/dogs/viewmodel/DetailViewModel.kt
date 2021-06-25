package com.saheralsous.dogs.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saheralsous.dogs.model.DogBreed
import com.saheralsous.dogs.model.DogDatabase
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) :BaseViewModel(application) {

    val dogLiveData = MutableLiveData<DogBreed>()

    fun fetch(uuid: Int){
        launch {
            val dog = DogDatabase(getApplication()).dogDao().getDog(uuid)
            dogLiveData.value = dog
        }
    }
}