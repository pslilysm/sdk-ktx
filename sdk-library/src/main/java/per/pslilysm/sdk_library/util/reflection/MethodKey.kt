package per.pslilysm.sdk_library.util.reflection

import per.pslilysm.sdk_library.util.concurrent.CASObjectPool
import per.pslilysm.sdk_library.util.function.Linkable
import per.pslilysm.sdk_library.util.function.Recyclable
import java.util.Objects

class MethodKey private constructor(
    var clazz: Class<*>? = null,
    var methodName: String? = null,
    var parameterTypes: Array<out Class<*>?>? = null
) : Linkable<MethodKey>, Recyclable {

    companion object {
        private val objectPool = CASObjectPool(newObjSupplier = {
            MethodKey()
        })

        fun Class<*>?.obtainMKey(
            methodName: String,
            vararg parameterTypes: Class<*>?
        ): MethodKey {
            val mKey = objectPool.obtain()
            mKey.clazz = this
            mKey.methodName = methodName
            mKey.parameterTypes = parameterTypes
            return mKey
        }
    }

    private var nextObj: MethodKey? = null

    override val next: MethodKey?
        get() = nextObj

    override fun setNext(next: MethodKey?) {
        nextObj = next
    }

    override fun recycle() {
        clazz = null
        methodName = null
        parameterTypes = null
        objectPool.recycle(this)
    }

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

}