@file:Suppress("IllegalIdentifier")

package me.khol.spdelegates.moshi

import android.annotation.SuppressLint
import com.squareup.moshi.Moshi
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
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

    @SuppressLint("CheckResult")
    @Suppress("UNUSED_VALUE", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
    @Test
    fun `custom moshi`() {
        val type: Type = Typed::class.java
        val moshi: Moshi = spyk(Moshi.Builder().build())
        var typed: Typed? by preferences.typedObject("typed", null, moshi)
        typed = customTyped
        verify {
            moshi.adapter<Typed>(type)
        }
        var typed2: Typed? by preferences.typedObject("typed2", null, moshi, false)
        typed2 = customTyped
    }
}
