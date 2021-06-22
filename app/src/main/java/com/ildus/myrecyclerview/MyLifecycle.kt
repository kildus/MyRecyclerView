package com.ildus.myrecyclerview

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class MyLifecycle:LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start(){
        Log.d(TAG, "Lifecycle.Event.ON_START")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop(){
        Log.d(TAG, "Lifecycle.Event.ON_STOP")
    }
}