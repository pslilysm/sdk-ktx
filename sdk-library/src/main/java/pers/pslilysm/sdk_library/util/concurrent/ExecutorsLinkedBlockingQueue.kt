package pers.pslilysm.sdk_library.util.concurrent

import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor

/**
 * Executors dedicated LinkedBlockingQueue
 *
 * @author pslilysm
 * Created on 2022/9/27 15:56
 */
class ExecutorsLinkedBlockingQueue : LinkedBlockingQueue<Runnable?> {
    private lateinit var executor: ThreadPoolExecutor

    constructor() : super()
    constructor(capacity: Int) : super(capacity)
    constructor(c: Collection<Runnable?>?) : super(c)

    fun setExecutor(executor: ThreadPoolExecutor) {
        this.executor = executor
    }

    override fun offer(e: Runnable?): Boolean {
        Objects.requireNonNull(executor, "please call setExecutor")
        return if (executor.poolSize < executor.maximumPoolSize
            && executor.activeCount == executor.poolSize
        ) {
            // if executor's pool size < max size
            // and all thread are executing
            // we reject queue task for make executors add more workers to handle the task
            false
        } else super.offer(e)
    }
}