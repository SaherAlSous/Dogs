package com.saheralsous.dogs.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class SharedPreferencesHelper { //video 41
    /**
     * Implementing [SharedPreferencesHelper]
      */

    companion object{
        private const val PREF_TIME = "Pref time"
        private var prefs: SharedPreferences? = null

        @Volatile private var instance: SharedPreferencesHelper? = null
        private val LOCK = Any()

        operator fun invoke(context: Context): SharedPreferencesHelper = instance ?: synchronized(LOCK){
            instance ?: buildHelper(context).also{
                instance = it
            }
        }

        private fun buildHelper(context: Context): SharedPreferencesHelper {
            prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return SharedPreferencesHelper()
        }
    }

    /**
     * saving the time we downloaded the content. we will use it inside [ListViewModel]
     */
    fun saveUpdateTime(time: Long){
        prefs?.edit(commit = true) {
            putLong(PREF_TIME, time)
        }
    }

    fun getUpdateTime() = prefs?.getLong(PREF_TIME, 0) ?: 0 //function was created automatically in this class.

}