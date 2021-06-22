package com.ildus.myrecyclerview

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PackViewModel:ViewModel(){

    var packs = mutableListOf<Pack>()
    var myData:MutableLiveData<String> = MutableLiveData()

}