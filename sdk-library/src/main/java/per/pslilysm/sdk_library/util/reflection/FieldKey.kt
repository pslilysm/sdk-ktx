package per.pslilysm.sdk_library.util.reflection

import java.util.*

class FieldKey private constructor(var clazz: Class<*>?, var filedName: String?) {
    private var inUse = false
    private var next: FieldKey? = null
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val fieldKey = o as FieldKey
        return clazz == fieldKey.clazz &&
                filedName == fieldKey.filedName
    }

    override fun hashCode(): Int {
        return Objects.hash(clazz, filedName)
    }

    fun markInUse() {
        inUse = true
    }

    fun recycle() {
        check(!inUse) { "$this is in use, can't recycle" }
        clazz = null
        filedName = null
        synchronized(FieldKey::class.java) {
            if (poolSize < MAX_POOL_SIZE) {
                next = pool
                pool = this
                poolSize++
            }
        }
    }

    companion object {
        private const val MAX_POOL_SIZE = 10
        private var pool: FieldKey? = null
        private var poolSize = 0
        fun obtain(clazz: Class<*>?, filedName: String?): FieldKey {
            synchronized(FieldKey::class.java) {
                if (pool != null) {
                    val fk = pool
                    pool = fk!!.next
                    fk.next = null
                    poolSize--
                    fk.clazz = clazz
                    fk.filedName = filedName
                    return fk
                }
            }
            return FieldKey(clazz, filedName)
        }
    }
}