package com.saheralsous.dogs.view

import android.view.View

/**
 * creating a click listener to apply it in [item_dog] layout.
 * this needs a textview that stores the uuid to be used by the listener.
 */

interface DogClickListener {

    fun onDogClicked(v: View)
}