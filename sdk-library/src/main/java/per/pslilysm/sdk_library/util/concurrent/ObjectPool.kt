package per.pslilysm.sdk_library.util.concurrent

/**
 * A pool for store object
 *
 * @author cxd
 * Created on 2024/02/28 15:01
 * @since 2.3.0
 */
interface ObjectPool<O> {

    /**
     * Obtain a object from pool
     *
     * @return null if pool is empty
     */
    fun obtain(): O?

    /**
     * Recycle object
     *
     * @param o to recycle
     */
    fun recycle(o: O)

}