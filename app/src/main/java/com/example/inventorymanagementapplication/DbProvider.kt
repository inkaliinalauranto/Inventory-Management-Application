package com.example.inventorymanagementapplication

import android.content.Context
import androidx.room.Room

// Singleton object:
object DbProvider {
    lateinit var db: AccountDatabase

    // context is always required with room database.
    fun provide(context: Context) {
        db = Room.databaseBuilder(context, AccountDatabase::class.java, name = "account.db").build()
    }
}