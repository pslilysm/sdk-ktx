package per.pslilysm.sdk_library.extention

import androidx.collection.ArraySet
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.internal.LinkedTreeMap
import per.pslilysm.sdk_library.annotation.GsonExclude
import per.pslilysm.sdk_library.util.reflection.ReflectionUtil

/**
 * Extension for gson
 *
 * @author pslilysm
 * Created on 2023/06/29 17:02
 * @since 2.2.0
 */

private val strategy: ExclusionStrategy by lazy {
    object : ExclusionStrategy {
        override fun shouldSkipField(f: FieldAttributes): Boolean {
            return f.getAnnotation(GsonExclude::class.java) != null
        }

        override fun shouldSkipClass(clazz: Class<*>): Boolean {
            return clazz.getAnnotation(GsonExclude::class.java) != null
        }
    }
}

val prettyGson: Gson by lazy {
    GsonBuilder()
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .setExclusionStrategies(strategy)
        .create()
}

val gson: Gson by lazy {
    GsonBuilder()
        .disableHtmlEscaping()
        .setExclusionStrategies(strategy)
        .create()
}

/**
 * Serialize object to json
 *
 * @param pretty ture we'll returned a pretty json string
 * @return json string
 */
fun Any?.toJson(pretty: Boolean = false): String {
    return if (pretty) prettyGson.toJson(this) else gson.toJson(this)
}

/**
 * Deserialize json string to object
 *
 * @param tClass class of object will returned
 * @param <T>    the type of the object returned
 * @return a object deserialize of json string
 */

fun <T> String?.toObject(tClass: Class<T>): T? {
    return gson.fromJson(this, tClass)
}

/**
 * Deserialize json to [Set]
 *
 * @param tClass Set's elements class
 * @param <T>    the type of the Set's element
 * @return a empty Set if json is empty string or empty array
 */

fun <T> String?.toSet(tClass: Class<T>?): Set<T> {
    val set: MutableSet<T> = ArraySet()
    if (!isNullOrEmpty()) {
        val array = JsonParser.parseString(this).asJsonArray
        for (elem in array) {
            set.add(gson.fromJson(elem, tClass))
        }
    }
    return set
}

/**
 * Deserialize json to [List]
 *
 * @param tClass List's elements class
 * @param <T>    the type of the List's element
 * @return a empty List if json is empty string or empty array
 */

fun <T> String?.toList(tClass: Class<T>?): MutableList<T> {
    val list: MutableList<T> = ArrayList()
    if (!isNullOrEmpty()) {
        val array = JsonParser.parseString(this).asJsonArray
        for (elem in array) {
            list.add(gson.fromJson(elem, tClass))
        }
    }
    return list
}

/**
 * Deserialize json to [Map]
 *
 * @param <V>> the type of the Map's value
 * @return a empty Map if json is empty
 */

fun <V> String?.toMap(vClass: Class<V>?): MutableMap<String, V> {
    val result: MutableMap<String, V> = LinkedTreeMap()
    if (isNullOrEmpty()) {
        return result
    }
    val jsonObject = gson.fromJson(this, JsonObject::class.java)
    try {
        val members = ReflectionUtil.getFieldValue<LinkedTreeMap<String, JsonElement>>(
            jsonObject,
            "members"
        )
        members!!.forEach { (s: String, jsonElement: JsonElement?) ->
            result[s] = gson.fromJson(jsonElement, vClass)
        }
    } catch (e: ReflectiveOperationException) {
        // never happen
        throw RuntimeException()
    }
    return result
}

/**
 * Pretty a json string
 *
 * @return a prettied json string
 */

fun String?.prettyJson(): String {
    val je = JsonParser.parseString(this)
    return prettyGson.toJson(je)
}