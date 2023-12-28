package per.pslilysm.sdk_library

import android.os.Handler
import android.os.Looper
import android.os.Message
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
class EventHandler constructor(looper: Looper, callback: Callback? = null) :
    Handler(looper, callback) {
    private val multiCallbacks: ConcurrentMap<Int, MutableList<EventCallback>> =
        ConcurrentHashMap()

    private val emptyMutableList: MutableList<EventCallback> = mutableListOf()

    fun registerEvent(eventCode: Int, callback: EventCallback) {
        multiCallbacks.computeIfAbsent(eventCode) { CopyOnWriteArrayList() }
            .add(callback)
    }

    fun unregisterEvent(eventCode: Int, callback: EventCallback) {
        multiCallbacks.getOrDefault(eventCode, emptyMutableList)
            .removeIf { eventCallback: EventCallback -> eventCallback === callback }
    }

    fun unregisterAllEvent(callback: EventCallback) {
        multiCallbacks.values.forEach(Consumer { eventCallbacks: MutableList<EventCallback> ->
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
            .forEach(Consumer { eventCallback: EventCallback -> eventCallback.handleEvent(msg) })
    }

    interface EventCallback {
        fun handleEvent(msg: Message)
    }

    companion object {

        /**
         * A default EventHandler which bind MainLooper
         */
        val default by lazy {
            EventHandler(Looper.getMainLooper())
        }

    }
}