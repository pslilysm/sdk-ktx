package pers.pslilysm.sdk_library.util

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import pers.pslilysm.sdk_library.extention.checkIsMainThread
import pers.pslilysm.sdk_library.util.concurrent.GlobalExecutors
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.function.Consumer

/**
 * A MutableLiveData will not pour back the data
 *
 * @author pslilysm
 * Created on 2023/06/30 17:41
 * @since 2.2.2
 */
class NPBMutableLiveData<T> {
    private val mObserverLiveDataMap: ConcurrentMap<Observer<T>, MutableLiveData<T>> = ConcurrentHashMap()
    private var mValue: T? = null

    fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        checkIsMainThread()
        if (owner.lifecycle.currentState == Lifecycle.State.DESTROYED) {
            // ignore
            return
        }
        owner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                mObserverLiveDataMap.remove(observer as Observer<T>)
                owner.lifecycle.removeObserver(this)
            }
        })
        val mutableLiveData = MutableLiveData<T>()
        mutableLiveData.observe(owner, observer)
        mObserverLiveDataMap[observer as Observer<T>] = mutableLiveData
    }

    fun observeForever(observer: Observer<in T>) {
        checkIsMainThread()
        val mutableLiveData = MutableLiveData<T>()
        mutableLiveData.observeForever(observer as Observer<T>)
        mObserverLiveDataMap[observer] = mutableLiveData
    }

    fun getValue(): T? {
        return mValue
    }

    fun setValue(value: T?) {
        checkIsMainThread()
        mValue = value
        mObserverLiveDataMap.values.forEach(Consumer { tLiveData: MutableLiveData<T> ->
            tLiveData.setValue(
                value
            )
        })
    }

    fun postValue(value: T?) {
        GlobalExecutors.main.execute { setValue(value) }
    }
}