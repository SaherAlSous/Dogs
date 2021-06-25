package com.saheralsous.dogs.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(application: Application) : AndroidViewModel(application), CoroutineScope { // we use application context(safer)

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
    // we will have a runnin job that retains the result to main thread

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }


}