package com.saheralsous.dogs.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * defining the tables that should be part of this database
 * this is the object that will access the db for us (repository)
 */
@Database(entities = arrayOf(DogBreed::class), version = 1) //video 38
abstract class DogDatabase : RoomDatabase() {

    abstract fun dogDao(): DogDao

    companion object{
        @Volatile private var instance: DogDatabase? = null
        private val LOCK = Any()

        /**
         * organizing threads to db. only a single thread can access the db at a time.
         * in case other threads are trying to access they will be synchronized.
         * if db is null. we will build it.
         * the operator operates the threads that wants to access the db.
         */
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also{
                instance = it
            }
        }
        /**
         * building the db
         */
        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, //we want application context because it might be null if the user rotated the device
            DogDatabase::class.java,
            "dogdatabase"
        ).build()
    }

}