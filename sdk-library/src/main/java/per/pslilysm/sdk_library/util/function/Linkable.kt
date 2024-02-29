package per.pslilysm.sdk_library.util.function

/**
 * A `Linkable` is a object that can be linked.
 * The setNext method is invoked to link another object.
 * The next val is to get linked object
 *
 * @author cxd
 * Created on 2024/02/28 15:02
 * @since 2.3.0
 */
interface Linkable<O> {

    /**
     * Get the linked object
     */
    val next: O?

    /**
     * Set the linked object
     *
     * @param next the object you wanna to link
     */
    fun setNext(next: O?)

}