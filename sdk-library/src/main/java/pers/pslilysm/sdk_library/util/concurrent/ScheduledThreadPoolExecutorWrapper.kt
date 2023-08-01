package pers.pslilysm.sdk_library.util.concurrent

import java.util.*
import java.util.concurrent.*

/**
 * Task scheduling executor wrapper
 *
 * @param executor The executor that actually executes task
 * @param threadFactory   For task scheduling executor
 * @author pslilysm
 * Created on 2022/9/27 15:56
 */
class ScheduledThreadPoolExecutorWrapper(executor: ExecutorService, threadFactory: ThreadFactory) :
    ScheduledExecutorService {
    /**
     * The base task scheduling executor
     */
    private val taskSchedulingES: ScheduledExecutorService

    /**
     * The base task execution executor
     */
    private val taskExecutionES: ExecutorService

    constructor(executorService: ExecutorService) : this(
        executorService,
        Executors.defaultThreadFactory()
    )

    init {
        taskSchedulingES = ScheduledThreadPoolExecutor(
            1,
            Objects.requireNonNull(threadFactory, "threadFactory can not be null")
        )
        taskExecutionES = Objects.requireNonNull(executor, "executor can not be null")
    }

    private fun wrapRunnable(command: Runnable): Runnable {
        return Runnable { execute(command) }
    }

    private fun <V> wrapCallable(callable: Callable<V>): Callable<V> {
        return Callable { this.submit(callable).get() }
    }

    override fun schedule(command: Runnable, delay: Long, unit: TimeUnit): ScheduledFuture<*> {
        return taskSchedulingES.schedule(wrapRunnable(command), delay, unit)
    }

    override fun <V> schedule(
        callable: Callable<V>,
        delay: Long,
        unit: TimeUnit
    ): ScheduledFuture<V> {
        // warn!! if the callable will block, then task scheduling will block so.
        // invoke this method with caution!
        return taskSchedulingES.schedule(wrapCallable(callable), delay, unit)
    }

    override fun scheduleAtFixedRate(
        command: Runnable,
        initialDelay: Long,
        period: Long,
        unit: TimeUnit
    ): ScheduledFuture<*> {
        return taskSchedulingES.scheduleAtFixedRate(
            wrapRunnable(command),
            initialDelay,
            period,
            unit
        )
    }

    override fun scheduleWithFixedDelay(
        command: Runnable,
        initialDelay: Long,
        delay: Long,
        unit: TimeUnit
    ): ScheduledFuture<*> {
        return taskSchedulingES.scheduleWithFixedDelay(
            wrapRunnable(command),
            initialDelay,
            delay,
            unit
        )
    }

    override fun shutdown() {
        taskSchedulingES.shutdown()
        taskExecutionES.shutdown()
    }

    override fun shutdownNow(): List<Runnable> {
//        List<Runnable> runnableList = mBase.shutdownNow();
//        runnableList.addAll(mWrapper.shutdownNow());
//        return runnableList;
        throw UnsupportedOperationException("not impl yet")
    }

    override fun isShutdown(): Boolean {
        return taskSchedulingES.isShutdown && taskExecutionES.isShutdown
    }

    override fun isTerminated(): Boolean {
        return taskSchedulingES.isTerminated && taskExecutionES.isTerminated
    }

    @Throws(InterruptedException::class)
    override fun awaitTermination(timeout: Long, unit: TimeUnit): Boolean {
//        long timeoutNano = unit.toNanos(timeout);
//        long l = System.nanoTime();
//        boolean result;
//        boolean baseRet = mBase.awaitTermination(timeout, unit);
//        return mBase.isTerminated() && mWrapper.isTerminated();
        throw UnsupportedOperationException("not impl yet")
    }

    override fun <T> submit(task: Callable<T>): Future<T> {
        return taskExecutionES.submit(task)
    }

    override fun <T> submit(task: Runnable, result: T): Future<T> {
        return taskExecutionES.submit(task, result)
    }

    override fun submit(task: Runnable): Future<*> {
        return taskExecutionES.submit(task)
    }

    @Throws(InterruptedException::class)
    override fun <T> invokeAll(tasks: Collection<Callable<T>?>): List<Future<T>> {
        return taskExecutionES.invokeAll(tasks)
    }

    @Throws(InterruptedException::class)
    override fun <T> invokeAll(
        tasks: Collection<Callable<T>?>,
        timeout: Long,
        unit: TimeUnit
    ): List<Future<T>> {
        return taskExecutionES.invokeAll(tasks, timeout, unit)
    }

    @Throws(ExecutionException::class, InterruptedException::class)
    override fun <T> invokeAny(tasks: Collection<Callable<T>?>): T {
        return taskExecutionES.invokeAny(tasks)
    }

    @Throws(ExecutionException::class, InterruptedException::class, TimeoutException::class)
    override fun <T> invokeAny(tasks: Collection<Callable<T>?>, timeout: Long, unit: TimeUnit): T {
        return taskExecutionES.invokeAny(tasks, timeout, unit)
    }

    override fun execute(command: Runnable) {
        taskExecutionES.execute(command)
    }
}