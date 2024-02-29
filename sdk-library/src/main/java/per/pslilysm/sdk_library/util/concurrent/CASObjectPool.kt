package per.pslilysm.sdk_library.util.concurrent

import per.pslilysm.sdk_library.util.function.Linkable
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import java.util.function.Supplier

/**
 * ObjectPool implementation via CAS
 *
 * @author cxd
 * Created on 2024/02/28 15:07
 * @since 2.3.0
 */
class CASObjectPool<O : Linkable<O>>(
    private val maxPoolSize: Int = 50,
    private val newObjSupplier: Supplier<O>
) : ObjectPool<O> {

    private var poolHead: AtomicReference<O?> = AtomicReference(null)
    private var poolSize = AtomicInteger(0)

    override fun obtain(): O {
        val head = poolHead.get()
        return if (head != null) {
            if (poolHead.compareAndSet(head, head.next)) {
                head.setNext(null)
                poolSize.decrementAndGet()
                head
            } else {
                obtain()
            }
        } else {
            newObjSupplier.get()
        }
    }

    override fun recycle(o: O) {
        while (true) {
            if (maxPoolSize > poolSize.get()) {
                val head = poolHead.get()
                if (poolHead.compareAndSet(head, o)) {
                    o.setNext(head)
                    poolSize.incrementAndGet()
                    break
                }
            } else {
                break
            }
        }
    }
}