package com.sun.freshfarm.database

import kotlin.String


interface GetDataCallBack<T> {
    fun onDataReceived(data: T)
    fun onCanceled(message: String)
}

interface SaveDataCallBack {
    fun onSuccess(message: String)
    fun onCanceled(message: String)
}