package com.example.android.core.newtwork

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

inline fun <reified T: RoomDatabase> provideDatabase(context: Context, name: String): T =
    Room.databaseBuilder(
        context,
        T::class.java,
        name
    )
        .fallbackToDestructiveMigration()
        .build()