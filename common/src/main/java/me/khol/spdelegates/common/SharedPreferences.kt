package me.khol.spdelegates.common

import android.annotation.SuppressLint
import android.content.SharedPreferences

// Copied from `androidx.core:core-ktx` library to avoid pulling in a 3rd-party library
// that might potentially cause version resolution conflicts on client side

@SuppressLint("ApplySharedPref")
inline fun SharedPreferences.edit(
    commit: Boolean = false,
    action: SharedPreferences.Editor.() -> Unit
) {
    val editor = edit()
    action(editor)
    if (commit) {
        editor.commit()
    } else {
        editor.apply()
    }
}
