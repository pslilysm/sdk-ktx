package pers.pslilysm.sdk_library.util

import pers.pslilysm.sdk_library.Singleton
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.atomic.AtomicInteger

/**
 * A global executors holder
 *
 * @author pslilysm
 * @since 1.0.0
 */
object GlobalExecutors {
    private val sIONum = AtomicInteger()
    private val sComputeNum = AtomicInteger()
    private val sGlobalIOExecutor: Singleton<ScheduledExecutorService> =
        object : Singleton<ScheduledExecutorService>() {
            override fun create(): ScheduledExecutorService {
                return Executors.newScheduledThreadPool(
                    Runtime.getRuntime().availableProcessors() * 5
                ) { r: Runnable? -> Thread(r, "g-io-" + sIONum.incrementAndGet() + "-thread") }
            }
        }
    private val sGlobalComputeExecutor: Singleton<ScheduledExecutorService> =
        object : Singleton<ScheduledExecutorService>() {
            override fun create(): ScheduledExecutorService {
                return Executors.newScheduledThreadPool(
                    Runtime.getRuntime().availableProcessors()
                ) { r: Runnable? ->
                    Thread(
                        r,
                        "g-compute-" + sComputeNum.incrementAndGet() + "-thread"
                    )
                }
            }
        }

    /**
     * @return a global io executor, the core pool size is `cpu cores * 5`
     */
    @kotlin.jvm.JvmStatic
    fun io(): ScheduledExecutorService {
        return sGlobalIOExecutor.getInstance()
    }

    /**
     * @return a global compute executor, the core pool size is cpu cores
     */
    @kotlin.jvm.JvmStatic
    fun compute(): ScheduledExecutorService {
        return sGlobalComputeExecutor.getInstance()
    }
}