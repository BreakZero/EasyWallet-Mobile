package com.dougie.framework.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dougie.framework.model.Event
import com.dougie.framework.model.RequestState

typealias StatefulLiveData<T> = LiveData<RequestState<T>>
typealias StatefulMutableLiveData<T> = MutableLiveData<RequestState<T>>

typealias EventLiveData<T> = LiveData<Event<T>>
typealias EventMutableLiveData<T> = MutableLiveData<Event<T>>
