package com.saheralsous.dogs.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saheralsous.dogs.model.DogBreed
import com.saheralsous.dogs.model.DogDatabase
import com.saheralsous.dogs.model.DogsApiService
import com.saheralsous.dogs.util.NotificationsHelper
import com.saheralsous.dogs.util.SharedPreferencesHelper
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.NumberFormatException

class ListViewModel(application: Application) : BaseViewModel(application) {
    /** we extend it to [BaseViewModel]
     * to apply the functions in it here.
     */

    private var prefHelper = SharedPreferencesHelper(getApplication())

    private var refreshTime =
        5 * 60 * 1000 * 1000 * 1000L //we want it 5 mins. stored in nano seconds.(stored as long)

    private val dogsService = DogsApiService() //Repository

    private val disposable = CompositeDisposable() //Disposable to wait for the data and forget
    // about it once its work is done.

    /**
     * live data variables
     */

    val dogs = MutableLiveData<List<DogBreed>>() //getting the data from the source in
    val dogsLoadError = MutableLiveData<Boolean>() //in case there is a load error = true
    val loading =
        MutableLiveData<Boolean>() // in case it is loading = true --> spinner will be spinning

    fun refresh() {
        /**
         * check the preferences cache duration value
         */
        checkCacheDuration()

        /**
         * over here we decide if we refresh or get info from API
         */
        val updateTime = prefHelper.getUpdateTime()

        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            fetchFromDatabase()
        } else {
            fetchFromRemote()
        }
    }

    private fun checkCacheDuration(){ //video 58
        val cachePreference = prefHelper.getCacheDuration()
        try {
            val cachePreferenceInt = cachePreference?.toInt() ?: 5 * 60 // we set the value in seconds that is why we need to convert them to minutes
            refreshTime = cachePreferenceInt.times(1000 * 1000 * 1000L) //updating the settings based on the value set by user

        }catch (e : NumberFormatException){
            e.printStackTrace()
        }
    }

    /**
     * getting data when we request the refresh function from the cached data
     */
    fun refreshByPassCache(){
        fetchFromRemote()
    }

    private fun fetchFromDatabase() {
        loading.value = true
        launch {
            val dogs = DogDatabase(getApplication()).dogDao().getAllDogs()
            dogsRetrieved(dogs)
            println("Dogs from db= $dogs")
            Toast.makeText(getApplication(), "Dogs retrieved from DataBase", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun fetchFromRemote() {
        loading.value = true
        disposable.add( //adding the data to disposable
            dogsService.getDogs() //calling the call data function and return a single
                .subscribeOn(Schedulers.newThread()) //create the call on a separate thread instead of main
                .observeOn(AndroidSchedulers.mainThread()) //the result of the process above needs to be displayed on the main thread.
                .subscribeWith(object :
                    DisposableSingleObserver<List<DogBreed>>() { //observer object that check the Single result.
                    override fun onSuccess(t: List<DogBreed>) {//what do we do on success?
//we store the data once we get them. then display them.
                        storeDogsLocally(t)
                        println("Dogs from API = $t")
                        Toast.makeText(
                            getApplication(),
                            "Dogs retrieved from endPoint",
                            Toast.LENGTH_SHORT
                        ).show()
                        /**
                         * run the notification once the list of dogs are recieved from API
                         */
                        NotificationsHelper(getApplication()).createNotification()

                    }

                    override fun onError(e: Throwable) { //what do we do on failure.
                        dogsLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }
                })
        )

    }

    /**
     * creating function to store data into db
     */

    private fun dogsRetrieved(dogList: List<DogBreed>) {
        dogs.value = dogList
        dogsLoadError.value = false
        loading.value = false
    }

    private fun storeDogsLocally(dogList: List<DogBreed>) {
        /**if we want to access db we need to do it in a background thread, therefore we create
         * another view model [BaseViewModel], to create all the Coroutine Jobs over there.
         */
        launch {
            val dao = DogDatabase(getApplication()).dogDao()
            dao.deleteAllDogs() // delete first to get a fresh list every run

            val result = dao.insertAll(*dogList.toTypedArray())
            /**
             * we expand the list into individual element using the * sign before the list name
             * because we used varag in dogDao.
             */
//            dogList.forEach {
//                dogList[0].uuid = result[0].toInt()
//            }
            var i = 0
             while (i < dogList.size) {
                 dogList[i].uuid = result[i].toInt()
                 i++
             }
            /**
             * var i = 0
             * while (i < doglist.size){
             * dogList[i].uuid = result[i].toint
             * i++
             * }
             */
            dogsRetrieved(dogList)
        }

        /**
         * saving the information of the moment when we have updated the database with the dog info we retrieved.
         * we will use this info when we retrieve or refresh the datalist
         */
        prefHelper.saveUpdateTime(System.nanoTime())
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
