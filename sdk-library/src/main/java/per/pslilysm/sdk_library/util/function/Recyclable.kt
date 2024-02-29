package per.pslilysm.sdk_library.util.function

/**
 * A `Recyclable` is a object that can be recycled.
 * The recycle method is invoked to release the object
 *
 * @author pslilysm
 * @since 1.0.0
 */
interface Recyclable {

    /**
     * Recycle the object
     */
    fun recycle()

}