package com.example.globalexceptionhandlerlibrary

import android.content.Context
import android.content.Intent
import android.os.Process
import android.util.Log

class GlobalExceptionHandler(private val defaultUncaughtExceptionHandler: Thread.UncaughtExceptionHandler?, private val applicationContext: Context, private val activityToBeLaunched: Class<*>) :

    Thread.UncaughtExceptionHandler {
    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        // Handle the uncaught exception here
        // You can log the exception, send crash reports, etc.

        // For example, you can log the exception:
        Log.e("GlobalExceptionHandler", "Uncaught exception in thread " + thread.name, throwable)

        // You can also perform additional actions like sending crash reports to a server.

        // Finally, let the default handler handle the exception (this will cause the app to crash)
        if (defaultUncaughtExceptionHandler != null) {
            launchActivity(applicationContext, activityToBeLaunched, throwable, thread)
            defaultUncaughtExceptionHandler.uncaughtException(thread, throwable)
        }
        else {
            // If there's no default handler, exit the app gracefully
            Process.killProcess(Process.myPid())
            System.exit(1)
        }
    }

    companion object{
        fun initialize( applicationContext: Context, launchActivity: Class<*>) {

            // Get the default uncaught exception handler
            val defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()

            // Set your custom uncaught exception handler
            Thread.setDefaultUncaughtExceptionHandler(GlobalExceptionHandler(
                    defaultUncaughtExceptionHandler,applicationContext,launchActivity)
            )

        }
    }

    private fun launchActivity(
        applicationContext: Context,
        activity: Class<*>,
        exception: Throwable,
        thread: Thread
    ) {

        try {
            val intent = Intent(applicationContext, activity)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            applicationContext.startActivity(intent)

        } catch (e: Exception) {
            e.localizedMessage
        }

    }
}

