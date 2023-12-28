package per.pslilysm.sdk_library.util

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import per.pslilysm.sdk_library.extention.checkIsMainThread
import per.pslilysm.sdk_library.util.concurrent.GlobalExecutors
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
    private val observerLiveDataMap: ConcurrentMap<Observer<T>, MutableLiveData<T>> = ConcurrentHashMap()
    private var value: T? = null

    fun observe(owner: LifecycleOwner, observer: Observer<T>) {
        checkIsMainThread()
        if (owner.lifecycle.currentState == Lifecycle.State.DESTROYED) {
            // ignore
            return
        }
        owner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                observerLiveDataMap.remove(observer)
                owner.lifecycle.removeObserver(this)
            }
        })
        val mutableLiveData = MutableLiveData<T>()
        mutableLiveData.observe(owner, observer)
        observerLiveDataMap[observer] = mutableLiveData
    }

    fun observeForever(observer: Observer<T>) {
        checkIsMainThread()
        val mutableLiveData = MutableLiveData<T>()
        mutableLiveData.observeForever(observer)
        observerLiveDataMap[observer] = mutableLiveData
    }

    fun getValue(): T? {
        return value
    }

    fun setValue(value: T?) {
        checkIsMainThread()
        this.value = value
        observerLiveDataMap.values.forEach(Consumer { tLiveData: MutableLiveData<T> ->
            tLiveData.setValue(
                value
            )
        })
    }

    fun postValue(value: T?) {
        GlobalExecutors.main.execute { setValue(value) }
    }

    fun isObserverAlive(observer: Observer<T>): Boolean {
        return observerLiveDataMap.containsKey(observer)
    }

    fun isAnyObserverAlive(): Boolean {
        return observerLiveDataMap.isNotEmpty()
    }
}