package com.ildus.myrecyclerview

import androidx.lifecycle.LiveData

import androidx.lifecycle.MutableLiveData


class DataController {


    var myLiveData:MutableLiveData<String> = MutableLiveData()

    private var instance: DataController? = null

    fun getInstance(): DataController? {
        if (instance == null) {
            instance = DataController()
            return instance
        }
        return instance
    }

    fun getData(): LiveData<String>? {
        return myLiveData
    }

}