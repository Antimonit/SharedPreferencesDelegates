package me.khol.spdelegates

import me.khol.spdelegates.common.Editor
import me.khol.spdelegates.common.Preferences
import me.khol.spdelegates.common.edit
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private const val defaultCommit = false

fun Preferences.string(
    key: String,
    default: String,
    commit: Boolean = defaultCommit,
) = object : ReadWriteProperty<Any?, String> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): String =
        getString(key, null) ?: default

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) =
        edit(commit = commit) {
            putString(key, value)
        }
}

fun Preferences.stringSet(
    key: String,
    default: Set<String>,
    commit: Boolean = defaultCommit,
) = object : ReadWriteProperty<Any?, Set<String>> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Set<String> =
        getStringSet(key, null) ?: default

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Set<String>) =
        edit(commit = commit) {
            putStringSet(key, value)
        }
}

fun Preferences.int(
    key: String,
    default: Int,
    commit: Boolean = defaultCommit,
): ReadWriteProperty<Any?, Int> =
    generic(key, default, Preferences::getInt, Editor::putInt, commit)

fun Preferences.long(
    key: String,
    default: Long,
    commit: Boolean = defaultCommit,
): ReadWriteProperty<Any?, Long> =
    generic(key, default, Preferences::getLong, Editor::putLong, commit)

fun Preferences.float(
    key: String,
    default: Float,
    commit: Boolean = defaultCommit,
): ReadWriteProperty<Any?, Float> =
    generic(key, default, Preferences::getFloat, Editor::putFloat, commit)

fun Preferences.boolean(
    key: String,
    default: Boolean,
    commit: Boolean = defaultCommit,
): ReadWriteProperty<Any?, Boolean> =
    generic(key, default, Preferences::getBoolean, Editor::putBoolean, commit)

fun Preferences.nullableString(
    key: String,
    commit: Boolean = defaultCommit,
): ReadWriteProperty<Any?, String?> =
    nullableGeneric(key, "", Preferences::getString, Editor::putString, commit)

fun Preferences.nullableStringSet(
    key: String,
    commit: Boolean = defaultCommit,
): ReadWriteProperty<Any?, Set<String>?> =
    nullableGeneric(key, emptySet(), Preferences::getStringSet, Editor::putStringSet, commit)

fun Preferences.nullableInt(
    key: String,
    commit: Boolean = defaultCommit,
): ReadWriteProperty<Any?, Int?> =
    nullableGeneric(key, 0, Preferences::getInt, Editor::putInt, commit)

fun Preferences.nullableLong(
    key: String,
    commit: Boolean = defaultCommit,
): ReadWriteProperty<Any?, Long?> =
    nullableGeneric(key, 0L, Preferences::getLong, Editor::putLong, commit)

fun Preferences.nullableFloat(
    key: String,
    commit: Boolean = defaultCommit,
): ReadWriteProperty<Any?, Float?> =
    nullableGeneric(key, 0f, Preferences::getFloat, Editor::putFloat, commit)

fun Preferences.nullableBoolean(
    key: String,
    commit: Boolean = defaultCommit,
): ReadWriteProperty<Any?, Boolean?> =
    nullableGeneric(key, false, Preferences::getBoolean, Editor::putBoolean, commit)

private fun <T : Any> Preferences.generic(
    key: String,
    defaultValue: T,
    getter: Preferences.(key: String, default: T) -> T,
    setter: Editor.(key: String, value: T) -> Unit,
    commit: Boolean,
) = object : ReadWriteProperty<Any?, T> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): T =
        getter(key, defaultValue)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) =
        edit(commit = commit) {
            setter(key, value)
        }
}

private fun <T : Any> Preferences.nullableGeneric(
    key: String,
    dummyValue: T, // never returned but required by the API of [SharedPreferences]
    getter: Preferences.(key: String, default: T) -> T?,
    setter: Editor.(key: String, value: T) -> Unit,
    commit: Boolean,
) = object : ReadWriteProperty<Any?, T?> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? =
        if (contains(key)) {
            getter(key, dummyValue)
        } else {
            null
        }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) =
        edit(commit = commit) {
            if (value == null) {
                remove(key)
            } else {
                setter(key, value)
            }
        }
}
