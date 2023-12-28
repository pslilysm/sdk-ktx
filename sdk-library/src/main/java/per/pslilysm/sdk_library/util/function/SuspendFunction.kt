package per.pslilysm.sdk_library.util.function

/**
 * Represents a suspend function that produces a result.
 *
 * @param R return type of the function.
 * @author pslilysm
 * Created on 2022/8/11 16:00
 */
interface SuspendFunction<R> {

    /**
     * Invoke the function then return a result.
     *
     * @return the result by the function
     */
    suspend fun invoke(): R

}