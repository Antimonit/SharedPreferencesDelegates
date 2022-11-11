package me.khol.spdelegates

import android.annotation.SuppressLint
import android.content.SharedPreferences

// Copied from `androidx.core:core-ktx` library

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
