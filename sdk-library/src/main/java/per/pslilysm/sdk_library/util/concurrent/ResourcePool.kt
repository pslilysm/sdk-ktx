package per.pslilysm.sdk_library.util.concurrent

/**
 * The ResourcePool obtains resources or store resources
 *
 * @param R the type of resource
 * @author pslilysm
 * @since 1.0.0
 */
interface ResourcePool<R> {

    /**
     * Get a resource of the poll, this method will blocking if poll is empty until a resource was returned or
     * current thread is interrupted
     *
     * @return null if current thread is interrupted
     * @throws InterruptedException if current thread interrupted
     */
    @get:Throws(InterruptedException::class)
    val resource: R?

    /**
     * Store a resource to pool, this method will notify a waiting thread after we store it.
     *
     * @param r the resource we want to store
     */
    fun storeResource(r: R)
}