package com.star.notes.app

import android.app.Application

/**
 * Created by Farhod Tohirov on 08-Nov-20
 */

class App : Application(){

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: App
    }
}