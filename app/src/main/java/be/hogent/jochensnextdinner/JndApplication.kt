package be.hogent.jochensnextdinner

import android.app.Application
import be.hogent.jochensnextdinner.data.AppContainer
import be.hogent.jochensnextdinner.data.DefaultAppContainer

class JndApplication : Application() {
    lateinit var appContainer: AppContainer
    override fun onCreate() {
        super.onCreate()
        appContainer = DefaultAppContainer(context = applicationContext)
    }
}