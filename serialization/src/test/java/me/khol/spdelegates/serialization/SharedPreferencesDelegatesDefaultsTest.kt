@file:Suppress("IllegalIdentifier")

package me.khol.spdelegates.serialization

import android.annotation.SuppressLint
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.serialization.json.Json
import me.khol.spdelegates.common.Editor
import me.khol.spdelegates.common.Preferences
import org.junit.Before
import org.junit.Test
import java.lang.reflect.Type

class SharedPreferencesDelegatesDefaultsTest {

    private lateinit var editor: Editor
    private lateinit var preferences: Preferences

    @Before
    fun setUp() {
        editor = mockk(relaxed = true)
        preferences = mockk {
            every { edit() } returns editor
        }
    }

    @Suppress("UNUSED_VALUE", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
    @Test
    fun `typed object delegate calls commit when explicitly specified`() {
        var typed: Typed? by preferences.typedObject("typed", null, commit = true)
        typed = customTyped
        verify { editor.commit() }
    }

    @Suppress("UNUSED_VALUE", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
    @Test
    fun `typed object delegate calls apply by default`() {
        var typed: Typed? by preferences.typedObject("typed", null)
        typed = customTyped
        verify { editor.apply() }
    }

//    @SuppressLint("CheckResult")
//    @Suppress("UNUSED_VALUE", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
//    @Test
//    fun `custom moshi`() {
//        val json: Json = spyk(Json)
//        var typed: Typed? by preferences.typedObject("typed", null, json)
//        typed = customTyped
//        verify {
//            json.adapter<Typed>(type)
//        }
//        var typed2: Typed? by preferences.typedObject("typed2", null, json, false)
//        typed2 = customTyped
//    }
}
