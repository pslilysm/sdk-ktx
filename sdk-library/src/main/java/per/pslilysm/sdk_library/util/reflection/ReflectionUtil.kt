package per.pslilysm.sdk_library.util.reflection

import per.pslilysm.sdk_library.app
import per.pslilysm.sdk_library.util.reflection.ConstructorKey.Companion.obtainCKey
import per.pslilysm.sdk_library.util.reflection.FieldKey.Companion.obtainFKey
import per.pslilysm.sdk_library.util.reflection.MethodKey.Companion.obtainMKey
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.function.Function

/**
 * A util for reflection,
 * Cache is added to Constructor Field Method, memory is changed for speed.
 *
 * @author pslilysm
 * @since 1.0.0
 */
object ReflectionUtil {

    private val constructorMap: ConcurrentMap<ConstructorKey, Constructor<*>> by lazy { ConcurrentHashMap() }
    private val fieldMap: ConcurrentMap<FieldKey, Field> by lazy { ConcurrentHashMap() }
    private val methodMap: ConcurrentMap<MethodKey, Method> by lazy { ConcurrentHashMap() }
    private val emptyParameterTypesAndArgs = emptyArray<Any>()

    /**
     * Split parameter types and args, their size of the two is always the same,
     * otherwise the `IllegalArgumentException` will throw
     *
     * @param classLoader           for load class by `String`
     * @param parameterTypesAndArgs their size of the two is always the same
     * @return A wrapped `Pair`, [Pair.first] is parameter types, [Pair.second] is args
     * @throws ClassNotFoundException when classloader load class failure
     */
    @Throws(ClassNotFoundException::class)
    private fun splitParameterTypesAndArgs(
        classLoader: ClassLoader,
        vararg parameterTypesAndArgs: Any?
    ): Pair<Array<out Class<*>?>, Array<Any?>> {
        require(parameterTypesAndArgs.size % 2 == 0) { "check your parameterTypesAndArgs length -> " + parameterTypesAndArgs.size }
        val mixedParameterTypes = Arrays.copyOf(parameterTypesAndArgs, parameterTypesAndArgs.size / 2)
        val args = Arrays.copyOfRange(parameterTypesAndArgs, parameterTypesAndArgs.size / 2, parameterTypesAndArgs.size)
        val parameterTypes: Array<Class<*>?> = arrayOfNulls(mixedParameterTypes.size)
        for (i in mixedParameterTypes.indices) {
            parameterTypes[i] = when (val pt = mixedParameterTypes[i]) {
                is String -> classLoader.loadClass(pt)
                is Class<*> -> pt
                else -> throw IllegalArgumentException("check your parameterTypes at pos " + i + ", type is " + pt!!.javaClass)
            }
        }
        return parameterTypes to args
    }

    /**
     * Find in the cache or create `Constructor` by class and parameterTypes
     *
     * @param T the type of instance's class
     * @param clazz instance's class
     * @param parameterTypes the parameter array
     * @return a new or cached constructor with given param
     */
    @Throws(ReflectiveOperationException::class)
    private fun <T> findOrCreateConstructor(
        clazz: Class<T>,
        vararg parameterTypes: Class<*>?
    ): Constructor<T> {
        val constructorKey: ConstructorKey = clazz.obtainCKey(*parameterTypes)
        val constructor = constructorMap.computeIfAbsent(constructorKey, Function {
            return@Function try {
                clazz.getDeclaredConstructor(*parameterTypes).apply { isAccessible = true }
            } catch (ex: NoSuchMethodException) {
                constructorKey.recycle()
                throw ex
            }
        }) as Constructor<T>
        constructorKey.recycle()
        return constructor
    }

    /**
     * Recursive find in the cache or create `Field` by class and fieldName
     *
     * @param clazz the class where the field is resides
     * @param fieldName filed name
     * @param originEX exception when reflection failure for the first time
     * @return a new or cached field with given param
     */
    @Throws(ReflectiveOperationException::class)
    private fun findOrCreateField(clazz: Class<*>, fieldName: String, originEX: ReflectiveOperationException? = null): Field {
        val fieldKey: FieldKey = clazz.obtainFKey(fieldName)
        var originEXCopy: ReflectiveOperationException? = originEX
        val field = fieldMap.computeIfAbsent(fieldKey, Function {
            return@Function try {
                clazz.getDeclaredField(fieldName).apply { isAccessible = true }
            } catch (ex: NoSuchFieldException) {
                if (originEXCopy == null) {
                    originEXCopy = ex
                }
                fieldKey.recycle()
                val superClazz = clazz.superclass
                if (superClazz != null && superClazz != Any::class.java) {
                    null
                } else {
                    throw originEXCopy!!
                }
            }
        }) ?: return findOrCreateField(clazz.superclass, fieldName, originEXCopy)
        fieldKey.recycle()
        return field
    }

    /**
     * Recursive find in the cache or create `Method` by class, methodName, and parameterTypes
     *
     * @param clazz the class where the method is resides
     * @param methodName method name
     * @param originEX exception when reflection failure for the first time
     * @param parameterTypes the parameter array
     * @return a new or cached method with given param
     */
    @Throws(ReflectiveOperationException::class)
    private fun findOrCreateMethod(
        clazz: Class<*>,
        methodName: String,
        originEX: ReflectiveOperationException? = null,
        vararg parameterTypes: Class<*>?
    ): Method {
        val methodKey: MethodKey = clazz.obtainMKey(methodName, *parameterTypes)
        var originEXCopy: ReflectiveOperationException? = originEX
        val method = methodMap.computeIfAbsent(methodKey, Function {
            return@Function try {
                clazz.getDeclaredMethod(methodName, *parameterTypes).apply { isAccessible = true }
            } catch (ex: NoSuchMethodException) {
                if (originEXCopy == null) {
                    originEXCopy = ex
                }
                ex.initCause(originEX)
                methodKey.recycle()
                val superClazz = clazz.superclass
                if (superClazz != null && superClazz != Any::class.java) {
                    null
                } else {
                    throw originEXCopy!!
                }
            }
        }) ?: return findOrCreateMethod(clazz.superclass, methodName, originEXCopy, *parameterTypes)
        methodKey.recycle()
        return method
    }

    /**
     * New instance
     *
     * @param T the type of instance
     * @param className instance's class name
     * @param classLoader is for load class via class name in the ptAndArgs
     * @param ptAndArgs the first half is the class name or class, and the second half is args
     * @return a new instance with given param
     */
    @Throws(ReflectiveOperationException::class)
    fun <T> newInstance(
        className: String?,
        classLoader: ClassLoader = app.classLoader,
        vararg ptAndArgs: Any? = emptyParameterTypesAndArgs
    ): T {
        return newInstance(clazz = classLoader.loadClass(className), ptAndArgs = ptAndArgs) as T
    }

    /**
     * New instance
     *
     * @param T the type of instance
     * @param clazz instance's clazz
     * @param classLoader is for load class via class name in the ptAndArgs
     * @param ptAndArgs the first half is the class name or class, and the second half is the args
     * @return a new instance with given param
     */
    @Throws(ReflectiveOperationException::class)
    fun <T> newInstance(
        clazz: Class<T>,
        classLoader: ClassLoader = clazz.classLoader ?: app.classLoader,
        vararg ptAndArgs: Any? = emptyParameterTypesAndArgs
    ): T {
        val splitPtAndArgs = splitParameterTypesAndArgs(classLoader, *ptAndArgs)
        return findOrCreateConstructor(clazz, *splitPtAndArgs.first).newInstance(*splitPtAndArgs.second)
    }

    /**
     * Get field value
     *
     * @param T the type of field
     * @param fieldName field's name
     * @return the field's value in the object
     */
    @Throws(ReflectiveOperationException::class)
    fun <T> Any.getFieldValue(fieldName: String): T? {
        return findOrCreateField(this.javaClass, fieldName)[this] as T
    }

    /**
     * Set field value
     *
     * @param fieldName field's name
     * @param fieldValue field's value
     */
    @Throws(ReflectiveOperationException::class)
    fun Any.setFieldValue(fieldName: String, fieldValue: Any?) {
        findOrCreateField(this.javaClass, fieldName)[this] = fieldValue
    }

    /**
     * Invoke method
     *
     * @param T the type returned by the method call
     * @param methodName method name
     * @param ptAndArgs The first half is the class or class name parameters required for function calls, and the second half is the args
     * @return result of method call
     */
    @Throws(ReflectiveOperationException::class)
    fun <T> Any.invokeMethod(methodName: String, vararg ptAndArgs: Any? = emptyParameterTypesAndArgs): T? {
        val clazz: Class<*> = this.javaClass
        val classLoader = clazz.classLoader ?: app.classLoader
        val ptAndArgs = splitParameterTypesAndArgs(classLoader, *ptAndArgs)
        return findOrCreateMethod(clazz, methodName, null, *ptAndArgs.first)
            .invoke(this, *ptAndArgs.second) as T
    }

    /**
     * Get static field value
     *
     * @param T the type of field
     * @param className class name
     * @param fieldName filed name
     * @return the static field's value in the class
     */
    @Throws(ReflectiveOperationException::class)
    fun <T> getStaticFieldValue(className: String, fieldName: String): T? {
        return getStaticFieldValue(app.classLoader.loadClass(className), fieldName)
    }

    /**
     * Get static field value
     *
     * @param T the type of field
     * @param clazz class
     * @param fieldName filed name
     * @return the static field's value in the class
     */
    @Throws(ReflectiveOperationException::class)
    fun <T> getStaticFieldValue(clazz: Class<*>, fieldName: String): T? {
        return findOrCreateField(clazz, fieldName)[null] as T
    }

    /**
     * Set static filed value
     *
     * @param className the class name of the class where the field is resides
     * @param fieldName field's name
     * @param fieldValue field's value
     */
    @Throws(ReflectiveOperationException::class)
    fun setStaticFiledValue(className: String, fieldName: String, fieldValue: Any?) {
        setStaticFiledValue(app.classLoader.loadClass(className), fieldName, fieldValue)
    }

    /**
     * Set static filed value
     *
     * @param clazz the class where the field is resides
     * @param fieldName field's name
     * @param fieldValue field's value
     */
    @Throws(ReflectiveOperationException::class)
    fun setStaticFiledValue(clazz: Class<*>, fieldName: String, fieldValue: Any?) {
        findOrCreateField(clazz, fieldName)[null] = fieldValue
    }

    /**
     * Invoke static method
     *
     * @param T the type returned by the method call
     * @param className the class name of the class where the method is resides
     * @param methodName method name
     * @param ptAndArgs the first half is the class or class name parameters required for function calls, and the second half is the args
     * @return result of method call
     */
    @Throws(ReflectiveOperationException::class)
    fun <T : Any> invokeStaticMethod(
        className: String,
        methodName: String,
        vararg ptAndArgs: Any? = emptyParameterTypesAndArgs
    ): T? {
        return invokeStaticMethod(app.classLoader.loadClass(className), methodName, ptAndArgs)
    }

    /**
     * Invoke static method
     *
     * @param T the type returned by the method call
     * @param clazz the class where the method is resides
     * @param methodName method name
     * @param ptAndArgs the first half is the class or class name parameters required for function calls, and the second half is the args
     * @return result of method call
     */
    @Throws(ReflectiveOperationException::class)
    fun <T> invokeStaticMethod(
        clazz: Class<*>,
        methodName: String,
        vararg ptAndArgs: Any? = emptyParameterTypesAndArgs
    ): T? {
        val classLoader = clazz.classLoader ?: app.classLoader
        val splitPtAndArgs = splitParameterTypesAndArgs(classLoader, *ptAndArgs)
        return findOrCreateMethod(clazz, methodName, null, *splitPtAndArgs.first)
            .invoke(null, *splitPtAndArgs.second) as T
    }
}