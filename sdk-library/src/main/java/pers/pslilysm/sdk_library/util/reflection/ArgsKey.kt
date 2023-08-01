package pers.pslilysm.sdk_library.util.reflection

import java.util.*

class ArgsKey private constructor(args: Array<out Any>) {
    var args: Array<out Any>?
    private var next: ArgsKey? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val argsKey = other as ArgsKey
        return Arrays.equals(args, argsKey.args)
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(args)
    }

    fun recycle() {
        args = null
        synchronized(poolLock) {
            if (maxPoolSize > poolSize) {
                next = pool
                pool = this
                poolSize++
            }
        }
    }

    companion object {
        private const val maxPoolSize = 10
        private val poolLock = Any()
        private var pool: ArgsKey? = null
        private var poolSize = 0
        fun obtain(vararg args: Any): ArgsKey {
            synchronized(poolLock) {
                if (pool != null) {
                    val argsKey = pool
                    pool = argsKey!!.next
                    argsKey.next = null
                    argsKey.args = args
                    poolSize--
                    return argsKey
                }
            }
            return ArgsKey(args)
        }
    }

    init {
        this.args = args
    }
}