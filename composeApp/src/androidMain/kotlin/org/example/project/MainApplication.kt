package org.example.project

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.lang.ref.WeakReference

class MainApplication : Application() {

    companion object {
        // Variable privada de solo lectura que expone el Activity si está disponible
        val currentActivity: Activity?
            get() = _currentActivity?.get()

        // Variable privada que mantiene la referencia débil
        private var _currentActivity: WeakReference<Activity>? = null
    }

    override fun onCreate() {
        super.onCreate()

        // Registrar un listener para cambios de estado de los activities
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                _currentActivity  = WeakReference(activity)
            }

            override fun onActivityStarted(activity: Activity) {
                _currentActivity  = WeakReference(activity)
            }

            override fun onActivityResumed(activity: Activity) {
                _currentActivity  = WeakReference(activity)
            }

            override fun onActivityPaused(activity: Activity) {
                // No eliminamos la referencia aquí para evitar problemas en configuraciones
            }

            override fun onActivityStopped(activity: Activity) {
                // No eliminamos la referencia aquí para evitar problemas en configuraciones
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {
                if (_currentActivity?.get() === activity) {
                    _currentActivity?.clear()
                }
            }
        })
    }
}