package pers.pslilysm.sdk_library.annotation

/**
 * A annotation support make field be ignore when use Gson to serialize or deserialize
 *
 * @author pslilysm
 * @since 1.0.0
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
annotation class GsonExclude