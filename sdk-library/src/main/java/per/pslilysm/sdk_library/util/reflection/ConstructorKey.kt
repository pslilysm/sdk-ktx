package per.pslilysm.sdk_library.util.reflection

import per.pslilysm.sdk_library.util.concurrent.CASObjectPool
import per.pslilysm.sdk_library.util.function.Linkable
import per.pslilysm.sdk_library.util.function.Recyclable
import java.util.Objects

class ConstructorKey private constructor(
    private var clazz: Class<*>? = null,
    private var parameterTypes: Array<out Class<*>?> = emptyArray()
) : Linkable<ConstructorKey>, Recyclable {

    companion object {
        private val objectPool = CASObjectPool(newObjSupplier = {
            ConstructorKey()
        })

        fun Class<*>?.obtainCKey(vararg parameterTypes: Class<*>?): ConstructorKey {
            val cKey = objectPool.obtain()
            cKey.clazz = this
            cKey.parameterTypes = parameterTypes
            return cKey
        }
    }

    private var nextObj: ConstructorKey? = null

    override val next: ConstructorKey?
        get() = nextObj

    override fun setNext(next: ConstructorKey?) {
        nextObj = next
    }

    override fun recycle() {
        clazz = null
        parameterTypes = emptyArray()
        objectPool.recycle(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as ConstructorKey
        return clazz == that.clazz &&
                parameterTypes.contentEquals(that.parameterTypes)
    }

    override fun hashCode(): Int {
        var result = Objects.hash(clazz)
        result = 31 * result + parameterTypes.contentHashCode()
        return result
    }

}