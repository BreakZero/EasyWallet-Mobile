package com.easy.framework.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.easy.framework.model.Event
import com.easy.framework.model.RequestState

typealias StatefulLiveData<T> = LiveData<RequestState<T>>
typealias StatefulMutableLiveData<T> = MutableLiveData<RequestState<T>>

typealias EventLiveData<T> = LiveData<Event<T>>
typealias EventMutableLiveData<T> = MutableLiveData<Event<T>>
