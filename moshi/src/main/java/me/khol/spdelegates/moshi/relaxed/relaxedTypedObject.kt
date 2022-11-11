package me.khol.spdelegates.moshi.relaxed

import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import me.khol.spdelegates.common.edit
import me.khol.spdelegates.moshi.defaultCommit
import me.khol.spdelegates.moshi.defaultMoshi
import me.khol.spdelegates.moshi.getTypedObject
import me.khol.spdelegates.moshi.putTypedObject
import java.lang.reflect.Type
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private typealias Preferences = SharedPreferences
private typealias Editor = SharedPreferences.Editor

inline fun <reified T> Preferences.relaxedTypedObject(
    key: String,
    defaultValue: T,
    moshi: Moshi = defaultMoshi,
    commit: Boolean = defaultCommit,
): ReadWriteProperty<Any?, T> = relaxedTypedObject(key, defaultValue, T::class.java, moshi, commit)

fun <T> Preferences.relaxedTypedObject(
    key: String,
    defaultValue: T,
    type: Type,
    moshi: Moshi = defaultMoshi,
    commit: Boolean = defaultCommit,
): ReadWriteProperty<Any?, T> = object : ReadWriteProperty<Any?, T> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): T =
        getRelaxedTypedObject(key, defaultValue, moshi, type)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = edit(commit = commit) {
        putTypedObject(key, value, type, moshi)
    }
}

inline fun <reified T> Preferences.getRelaxedTypedObject(key: String, defaultValue: T): T =
    getTypedObject(key, defaultValue, T::class.java, defaultMoshi)

fun <T> Preferences.getRelaxedTypedObject(key: String, defaultValue: T, moshi: Moshi, type: Type): T {
    return try {
        getTypedObject(key, defaultValue, type, moshi)
    } catch (t: Throwable) {
        if (t is com.squareup.moshi.JsonDataException) {
            edit {
                putString(key, null)
            }
        }
        defaultValue
    }
}
