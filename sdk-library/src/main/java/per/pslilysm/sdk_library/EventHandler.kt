package per.pslilysm.sdk_library

import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.function.Consumer

/**
 * EventHandler is designed to send event between UIComponent
 *
 * @author pslilysm
 * @since 1.0.0
 */
class EventHandler constructor(looper: Looper) :
    Handler(looper, null) {

    private val multiCallbacks: ConcurrentMap<Int, MutableList<Consumer<Message>>> = ConcurrentHashMap()

    private val emptyMutableList: MutableList<Consumer<Message>> = mutableListOf()

    fun registerEvent(lifecycleOwner: LifecycleOwner? = null, eventCode: Int, consumer: Consumer<Message>) {
        lifecycleOwner?.also {
            if (lifecycleOwner.lifecycle.currentState == Lifecycle.State.DESTROYED) {
                // ignore
                return
            }
            lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    owner.lifecycle.removeObserver(this)
                    unregisterEvent(eventCode, consumer)
                }
            })
        }
        multiCallbacks.computeIfAbsent(eventCode) { CopyOnWriteArrayList() }
            .add(consumer)
    }

    fun unregisterEvent(eventCode: Int, consumer: Consumer<Message>) {
        multiCallbacks.getOrDefault(eventCode, emptyMutableList)
            .removeIf { eventCallback: Consumer<Message> -> eventCallback == consumer }
    }

    fun unregisterAllEvent(callback: Consumer<Message>) {
        multiCallbacks.values.forEach(Consumer { eventCallbacks: MutableList<Consumer<Message>> ->
            eventCallbacks.remove(
                callback
            )
        })
    }

    /**
     * if current thread == the thread we bind, handle the event directly
     *
     * @param event to send
     */
    fun sendEventOpted(event: Message) {
        if (Looper.myLooper() == looper) {
            handleMessage(event)
            event.recycle()
        } else {
            sendMessage(event)
        }
    }

    override fun handleMessage(msg: Message) {
        multiCallbacks.getOrDefault(msg.what, emptyMutableList)
            .forEach(Consumer { consumer: Consumer<Message> ->
                consumer.accept(msg)
            })
    }

}

/**
 * A default EventHandler which bind MainLooper
 */
val defaultEventHandler by lazy {
    EventHandler(Looper.getMainLooper())
}