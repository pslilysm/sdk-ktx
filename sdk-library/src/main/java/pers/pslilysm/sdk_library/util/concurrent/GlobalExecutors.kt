package pers.pslilysm.sdk_library.util.concurrent;

import pers.pslilysm.sdk_library.Singleton
import java.util.concurrent.*
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
                val corePoolSize = 1
                val maxPoolSize = Runtime.getRuntime().availableProcessors() * 10
                val keepAliveTimeSeconds = 2
                val maxQueueSize = maxPoolSize * 0xFF
                val workQueue = ExecutorsLinkedBlockingQueue(maxQueueSize)
                val threadFactory =
                    ThreadFactory { r: Runnable? ->
                        Thread(
                            r,
                            "g-io-" + sIONum.incrementAndGet() + "-thread"
                        )
                    }
                val rejectedExecutionHandler =
                    RejectedExecutionHandler { r: Runnable, executor: ThreadPoolExecutor ->
                        if (workQueue.size < maxQueueSize) {
                            executor.execute(r)
                        } else {
                            throw RejectedExecutionException(
                                "Task " + r.toString() +
                                        " rejected from " +
                                        executor.toString()
                            )
                        }
                    }
                val ioES = ThreadPoolExecutor(
                    corePoolSize,
                    maxPoolSize,
                    keepAliveTimeSeconds.toLong(), TimeUnit.SECONDS,
                    workQueue,
                    threadFactory,
                    rejectedExecutionHandler
                )
                workQueue.setExecutor(ioES)
                return ScheduledThreadPoolExecutorWrapper(ioES)
            }
        }
    private val sGlobalComputeExecutor: Singleton<ScheduledExecutorService> =
        object : Singleton<ScheduledExecutorService>() {
            override fun create(): ScheduledExecutorService {
                val corePoolSize = 1
                val maxPoolSize = Runtime.getRuntime().availableProcessors()
                val keepAliveTimeSeconds = 2
                val maxQueueSize = maxPoolSize * 0xF
                val workQueue = ExecutorsLinkedBlockingQueue(maxQueueSize)
                val threadFactory =
                    ThreadFactory { r: Runnable? ->
                        Thread(
                            r,
                            "g-compute-" + sComputeNum.incrementAndGet() + "-thread"
                        )
                    }
                val rejectedExecutionHandler =
                    RejectedExecutionHandler { r: Runnable, executor: ThreadPoolExecutor ->
                        if (workQueue.size < maxQueueSize) {
                            executor.execute(r)
                        } else {
                            throw RejectedExecutionException(
                                "Task " + r.toString() +
                                        " rejected from " +
                                        executor.toString()
                            )
                        }
                    }
                val computeES = ThreadPoolExecutor(
                    corePoolSize,
                    maxPoolSize,
                    keepAliveTimeSeconds.toLong(), TimeUnit.SECONDS,
                    workQueue,
                    threadFactory,
                    rejectedExecutionHandler
                )
                workQueue.setExecutor(computeES)
                return ScheduledThreadPoolExecutorWrapper(computeES)
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