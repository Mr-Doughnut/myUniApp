package com.example.myuniapp

import android.app.Application

class MyUniApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: MyUniApp
            private set
    }
}
