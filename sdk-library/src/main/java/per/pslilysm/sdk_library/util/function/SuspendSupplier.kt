package per.pslilysm.sdk_library.util.function

/**
 * Represents a suspend supplier of results.
 * There is no requirement that a new or distinct result be returned each time the supplier is invoked.
 *
 * @param R the type of results supplied by this supplier
 * @author pslilysm
 * Created on 2022/8/11 16:00
 */
interface SuspendSupplier<R> {

    /**
     * Gets a result.
     *
     * @return a result
     */
    suspend fun get(): R

}