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
    private lateinit var mExecutor: ThreadPoolExecutor

    constructor() : super()
    constructor(capacity: Int) : super(capacity)
    constructor(c: Collection<Runnable?>?) : super(c)

    fun setExecutor(executor: ThreadPoolExecutor) {
        this.mExecutor = executor
    }

    override fun offer(e: Runnable?): Boolean {
        Objects.requireNonNull(mExecutor, "please call setExecutor")
        return if (mExecutor.poolSize < mExecutor.maximumPoolSize
            && mExecutor.activeCount == mExecutor.poolSize
        ) {
            // if executor's pool size < max size
            // and all thread are executing
            // we reject queue task for make executors add more workers to handle the task
            false
        } else super.offer(e)
    }
}