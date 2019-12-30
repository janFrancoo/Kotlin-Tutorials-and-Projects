package com.janfranco.kotlinfoursquareparse

import android.app.Application
import com.parse.Parse

class StarterApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG)
        Parse.initialize(Parse.Configuration.Builder(this)
            .applicationId("02x2D2uBD0f15zXBOhxliGYF773DpuJxAWL0lRig")
            .clientKey("iIT5EMq0JZuXuXamah1az2aHrq32cuQ18WCJ69HA")
            .server("https://parseapi.back4app.com/")
            .build()
        )
    }

}
