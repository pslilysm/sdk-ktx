package pers.pslilysm.sdk_library.util.reflection

import java.util.*

class MethodKey private constructor(
    var clazz: Class<*>?,
    var methodName: String?,
    var parameterTypes: Array<out Class<*>?>
) {
    private var inUse = false
    private var next: MethodKey? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val methodKey = other as MethodKey
        return clazz == methodKey.clazz &&
                methodName == methodKey.methodName &&
                parameterTypes.contentEquals(methodKey.parameterTypes)
    }

    override fun hashCode(): Int {
        var result = Objects.hash(clazz, methodName)
        result = 31 * result + parameterTypes.contentHashCode()
        return result
    }

    fun markInUse() {
        inUse = true
    }

    fun recycle() {
        check(!inUse) { "$this is in use, can't recycle" }
        clazz = null
        methodName = null
        parameterTypes = emptyArray()
        synchronized(MethodKey::class.java) {
            if (poolSize < MAX_POOL_SIZE) {
                next = pool
                pool = this
                poolSize++
            }
        }
    }

    companion object {
        private const val MAX_POOL_SIZE = 10
        private var pool: MethodKey? = null
        private var poolSize = 0
        fun obtain(
            clazz: Class<*>,
            methodName: String,
            vararg parameterTypes: Class<*>?
        ): MethodKey {
            synchronized(MethodKey::class.java) {
                if (pool != null) {
                    val mk = pool
                    pool = mk!!.next
                    mk.next = null
                    poolSize--
                    mk.clazz = clazz
                    mk.methodName = methodName
                    mk.parameterTypes = parameterTypes
                    return mk
                }
            }
            return MethodKey(clazz, methodName, parameterTypes)
        }
    }
}