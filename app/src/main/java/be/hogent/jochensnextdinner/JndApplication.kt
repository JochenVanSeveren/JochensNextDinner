package be.hogent.jochensnextdinner

import android.app.Application
import be.hogent.jochensnextdinner.data.AppContainer
import be.hogent.jochensnextdinner.data.DefaultAppContainer

/**
 * Application class for the JochensNextDinner app.
 *
 * This class is a subclass of the Android Application class.
 * It initializes the app container when the application is created.
 */
class JndApplication : Application() {

    /**
     * The app container for the JochensNextDinner app.
     * It provides access to the repositories and other data-related classes.
     */
    lateinit var appContainer: AppContainer

    /**
     * Called when the application is starting, before any activity, service, or receiver objects have been created.
     * It initializes the app container with the application context.
     */
    override fun onCreate() {
        super.onCreate()
        appContainer = DefaultAppContainer(context = applicationContext)
    }
}