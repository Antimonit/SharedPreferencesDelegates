package me.khol.spdelegates.serialization

import android.content.SharedPreferences
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import me.khol.spdelegates.edit
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private typealias Preferences = SharedPreferences
private typealias Editor = SharedPreferences.Editor

@PublishedApi
internal val defaultJson = Json
@PublishedApi
internal const val defaultCommit = false

/**
 * Provides a property delegate for any type.
 * Requires [T] to be serializable by Kotlinx Serialization's JSON module.
 */
inline fun <reified T> Preferences.typedObject(
    key: String,
    defaultValue: T,
    json: Json = defaultJson,
    commit: Boolean = defaultCommit,
): ReadWriteProperty<Any?, T> =
    typedObject(key, defaultValue, json.serializersModule.serializer(), json, commit)

/**
 * Provides a property delegate for any type.
 * Requires [T] to be serializable by Kotlinx Serialization's JSON module.
 */
fun <T> Preferences.typedObject(
    key: String,
    defaultValue: T,
    serializer: KSerializer<T>,
    json: Json = defaultJson,
    commit: Boolean = defaultCommit,
): ReadWriteProperty<Any?, T> = object : ReadWriteProperty<Any?, T> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): T =
        getTypedObject(key, defaultValue, serializer, json)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) =
        edit(commit = commit) {
            putTypedObject(key, value, serializer, json)
        }
}

/**
 * Retrieves data of any type.
 * Requires [T] to be serializable by Kotlinx Serialization's JSON module.
 * Use together with [putTypedObject] with the same type to store and retrieve data.
 */
inline fun <reified T> Preferences.getTypedObject(
    key: String,
    defaultValue: T,
    json: Json = defaultJson,
): T = getTypedObject(key, defaultValue, json.serializersModule.serializer(), json)

/**
 * Retrieves data of any type.
 * Requires [T] to be serializable by Kotlinx Serialization's JSON module.
 * Use together with [putTypedObject] with the same type to store and retrieve data.
 */
fun <T> Preferences.getTypedObject(
    key: String,
    defaultValue: T,
    serializer: KSerializer<T>,
    json: Json = defaultJson,
): T = getString(key, null)?.let { json.decodeFromString(serializer, it) } ?: defaultValue

/**
 * Stores data of any type.
 * Requires [T] to be serializable by Kotlinx Serialization's JSON module.
 * Use together with [getTypedObject] with the same type to store and retrieve data.
 */
inline fun <reified T> Editor.putTypedObject(
    key: String,
    value: T,
    json: Json = defaultJson,
): Editor = putTypedObject(key, value, json.serializersModule.serializer(), json)

/**
 * Stores data of any type.
 * Requires [T] to be serializable by Kotlinx Serialization's JSON module.
 * Use together with [getTypedObject] with the same type to store and retrieve data.
 */
fun <T> Editor.putTypedObject(
    key: String,
    value: T,
    serializer: KSerializer<T>,
    json: Json,
): Editor = putString(key, json.encodeToString(serializer, value))
