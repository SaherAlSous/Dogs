package com.saheralsous.dogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saheralsous.dogs.model.DogBreed
import com.saheralsous.dogs.model.DogsApiService
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ListViewModel : ViewModel() {

    private val dogsService = DogsApiService() //Repository

    private val  disposable = CompositeDisposable() //Disposable to wait for the data and forget
                                                    // about it once its work is done.

    /**
     * live data variables
     */

    val dogs = MutableLiveData<List<DogBreed>>() //getting the data from the source in
    val dogsLoadError = MutableLiveData<Boolean>() //in case there is a load error = true
    val loading =
        MutableLiveData<Boolean>() // in case it is loading = true --> spinner will be spinning

    fun refresh() {

        fetchFromRemote()

    }

    private fun fetchFromRemote(){
        loading.value= true
        disposable.add( //adding the data to disposable
            dogsService.getDogs() //calling the call data function and return a single
                .subscribeOn(Schedulers.newThread()) //create the call on a separate thread instead of main
                .observeOn(AndroidSchedulers.mainThread()) //the result of the process above needs to be displayed on the main thread.
                .subscribeWith(object: DisposableSingleObserver<List<DogBreed>>(){ //observer object that check the Single result.
                    override fun onSuccess(t: List<DogBreed>) {//what do we do on success?
                        dogs.value = t
                        dogsLoadError.value = false
                        loading.value = false
                    }

                    override fun onError(e: Throwable) { //what do we do on failure.
                        dogsLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }
                })
        )

    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}


/**
 * old data
 *
val dog1 = DogBreed("1", "Corgi", "15 years", "BreedGroup", "bredFor", "termperament", "")
val dog2 = DogBreed("2", "Labrador", "10 years", "BreedGroup", "bredFor", "termperament", "")
val dog3 = DogBreed("3", "Rotwailer", "20 years", "BreedGroup", "bredFor", "termperament", "")

val dogList = arrayListOf(dog1, dog2, dog3)

//passing data to the LiveData values
dogs.value = dogList
dogsLoadError.value = false
loading.value = false
 */