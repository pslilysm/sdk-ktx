package per.pslilysm.sdk_library.util.reflection

import per.pslilysm.sdk_library.util.concurrent.CASObjectPool
import per.pslilysm.sdk_library.util.function.Linkable
import per.pslilysm.sdk_library.util.function.Recyclable
import java.util.Objects

class FieldKey private constructor(
    var clazz: Class<*>? = null,
    var filedName: String? = null
) : Linkable<FieldKey>, Recyclable {

    companion object {
        private val objectPool = CASObjectPool(newObjSupplier = {
            FieldKey()
        })

        fun Class<*>?.obtainFKey(filedName: String?): FieldKey {
            val fKey = objectPool.obtain()
            fKey.clazz = this
            fKey.filedName = filedName
            return fKey
        }
    }

    private var nextObj: FieldKey? = null

    override val next: FieldKey?
        get() = nextObj

    override fun setNext(next: FieldKey?) {
        nextObj = next
    }

    override fun recycle() {
        clazz = null
        filedName = null
        objectPool.recycle(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val fieldKey = other as FieldKey
        return clazz == fieldKey.clazz &&
                filedName == fieldKey.filedName
    }

    override fun hashCode(): Int {
        return Objects.hash(clazz, filedName)
    }

}