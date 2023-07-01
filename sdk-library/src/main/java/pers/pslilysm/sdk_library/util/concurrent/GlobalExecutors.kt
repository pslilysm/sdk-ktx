package pers.pslilysm.sdk_library.util.concurrent

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.RejectedExecutionHandler
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
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
    private val sIOExecutor by lazy {
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
        ScheduledThreadPoolExecutorWrapper(ioES)
    }
    private val sComputeExecutor by lazy {
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
        ScheduledThreadPoolExecutorWrapper(computeES)
    }

    private val lazyMainHandler by lazy { Handler(Looper.getMainLooper()) }

    private val lazyMainExecutor by lazy {
        Executor {
            lazyMainHandler.post(it)
        }
    }

    /**
     * @return a global io executor, the core pool size is `cpu cores * 5`
     */
    val io: ScheduledExecutorService get() = sIOExecutor

    /**
     * @return a global compute executor, the core pool size is cpu cores
     */
    val compute: ScheduledExecutorService get() = sComputeExecutor

    /**
     * @return a global main executor, all runnable will run in main thread
     */
    val main: Executor get() = lazyMainExecutor
}