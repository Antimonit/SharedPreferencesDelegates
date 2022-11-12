package me.khol.spdelegates.moshi

import android.content.SharedPreferences
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import me.khol.spdelegates.common.edit
import java.lang.reflect.Type
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private typealias Preferences = SharedPreferences
private typealias Editor = SharedPreferences.Editor

@PublishedApi
internal val defaultMoshi = Moshi.Builder().build()
@PublishedApi
internal const val defaultCommit = false

/**
 * Provides a property delegate for any Type.
 * Requires [T] to be serializable by Moshi.
 */
inline fun <reified T> Preferences.typedObject(
    key: String,
    defaultValue: T,
    moshi: Moshi = defaultMoshi,
    commit: Boolean = defaultCommit,
): ReadWriteProperty<Any?, T> = typedObject(key, defaultValue, T::class.java, moshi, commit)

/**
 * Provides a property delegate for any Type.
 * Requires [T] to be serializable by Moshi.
 */
fun <T> Preferences.typedObject(
    key: String,
    defaultValue: T,
    type: Type,
    moshi: Moshi = defaultMoshi,
    commit: Boolean = defaultCommit,
): ReadWriteProperty<Any?, T> = typedObject(key, defaultValue, moshi.adapter(type), commit)

/**
 * Provides a property delegate for any Type.
 * Requires [T] to be serializable by Moshi.
 */
fun <T> Preferences.typedObject(
    key: String,
    defaultValue: T,
    adapter: JsonAdapter<T>,
    commit: Boolean = defaultCommit,
): ReadWriteProperty<Any?, T> = object : ReadWriteProperty<Any?, T> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): T =
        getTypedObject(key, defaultValue, adapter)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) =
        edit(commit = commit) {
            putTypedObject(key, value, adapter)
        }
}

/**
 * Retrieves data of any Type.
 * Requires [T] to be serializable by Moshi.
 * Use together with [putTypedObject] with the same type to store and retrieve data.
 */
inline fun <reified T> Preferences.getTypedObject(
    key: String,
    defaultValue: T,
    moshi: Moshi = defaultMoshi,
): T = getTypedObject(key, defaultValue, T::class.java, moshi)

/**
 * Retrieves data of any Type.
 * Requires [T] to be serializable by Moshi.
 * Use together with [putTypedObject] with the same type to store and retrieve data.
 */
fun <T> Preferences.getTypedObject(
    key: String,
    defaultValue: T,
    type: Type,
    moshi: Moshi = defaultMoshi,
): T = getTypedObject(key, defaultValue, moshi.adapter(type))

/**
 * Retrieves data of any Type.
 * Requires [T] to be serializable by Moshi.
 * Use together with [putTypedObject] with the same type to store and retrieve data.
 */
fun <T> Preferences.getTypedObject(
    key: String,
    defaultValue: T,
    adapter: JsonAdapter<T>,
): T = getString(key, null)?.let(adapter::fromJson) ?: defaultValue

/**
 * Stores data of any Type.
 * Requires [T] to be serializable by Moshi.
 * Use together with [getTypedObject] with the same Type to store and retrieve data.
 */
inline fun <reified T> Editor.putTypedObject(
    key: String,
    value: T,
    moshi: Moshi = defaultMoshi,
): Editor = putTypedObject(key, value, T::class.java, moshi)

/**
 * Stores data of any Type.
 * Requires [T] to be serializable by Moshi.
 * Use together with [getTypedObject] with the same type to store and retrieve data.
 */
fun <T> Editor.putTypedObject(
    key: String,
    value: T,
    type: Type,
    moshi: Moshi = defaultMoshi,
): Editor = putTypedObject(key, value, moshi.adapter(type))

/**
 * Stores data of any Type.
 * Requires [T] to be serializable by Moshi.
 * Use together with [getTypedObject] with the same type to store and retrieve data.
 */
fun <T> Editor.putTypedObject(
    key: String,
    value: T,
    adapter: JsonAdapter<T>,
): Editor = putString(key, adapter.toJson(value))
