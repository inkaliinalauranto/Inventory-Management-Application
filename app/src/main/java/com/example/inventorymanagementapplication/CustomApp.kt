package com.example.inventorymanagementapplication

import android.app.Application

class CustomApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DbProvider.provide(context = this)
    }
}