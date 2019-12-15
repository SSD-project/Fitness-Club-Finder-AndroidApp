package com.wust.ssd.fitnessclubfinder.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.wust.ssd.fitnessclubfinder.App
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection


object AppInjector {
    fun init(app: App) {
        DaggerAppComponent
            .builder()
            .application(app)
            .build()
            .inject(app)

        app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, bundle: Bundle?) =
                handleActivity(activity)


            override fun onActivityStarted(activity: Activity) {}

            override fun onActivityResumed(activity: Activity) {}

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {}

            override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}

            override fun onActivityDestroyed(activity: Activity) {}
        })
    }

    /**
     * Automated fragment injector
     * @param activity provided by ActivityLifecycleCallbacks()
     */
    private fun handleActivity(activity: Activity) {

        if (activity is Injectable)
            AndroidInjection.inject(activity)


//        if (activity is FragmentActivity) {
//            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
//                object : FragmentManager.FragmentLifecycleCallbacks() {
//                    override fun onFragmentCreated(
//                        fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?
//                    ) {
//                        AndroidSupportInjection.inject(f)
//                    }
//                }, true
//            )
//        }

    }
}