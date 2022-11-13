@file:Suppress("KotlinConstantConditions", "IllegalIdentifier")

package me.khol.spdelegates.moshi

import android.content.Context
import android.content.SharedPreferences
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Types
import me.khol.spdelegates.common.edit
import me.khol.spdelegates.int
import me.khol.spdelegates.string
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import strikt.api.expectCatching
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.failedWith
import strikt.assertions.isEqualTo
import strikt.assertions.isNotEqualTo
import java.util.Date

@RunWith(AndroidJUnit4::class)
class SharedPreferencesMoshiDelegatesTest {

    private lateinit var preferences: SharedPreferences

    @Before
    fun setUp() {
        preferences = InstrumentationRegistry.getInstrumentation().targetContext
            .getSharedPreferences("test_preferences", Context.MODE_PRIVATE)
    }

    @After
    fun tearDown() {
        preferences.edit(commit = true) { clear() }
    }

    @Test
    fun `plain getTypedObject and setTypedObject works properly with typed objects`() {
        val result1: Typed? = preferences.getTypedObject("typed", null)
        expectThat(result1).isEqualTo(null)
        preferences.edit {
            putTypedObject("typed", customTyped)
        }
        val result2: Typed? = preferences.getTypedObject("typed", null)
        expectThat(result2).isEqualTo(customTyped)
    }

    @Test
    fun `plain getTypedObject and setTypedObject works properly with generic objects`() {
        val type = Types.newParameterizedType(Generic::class.java, Typed::class.java)
        val result1: Generic<Typed>? = preferences.getTypedObject("generic", null, type)
        expectThat(result1).isEqualTo(null)
        preferences.edit {
            putTypedObject("generic", customGeneric, type)
        }
        val result2: Generic<Typed>? = preferences.getTypedObject("generic", null, type)
        expectThat(result2).isEqualTo(customGeneric)
    }

    @Test
    fun `typed object delegate works properly`() {
        var typed: Typed by preferences.typedObject("typed", customTyped)
        expectThat(typed).isEqualTo(customTyped)
        typed = customTyped.copy(map = emptyMap())
        expectThat(typed).isEqualTo(customTyped.copy(map = emptyMap()))
        expectThat(typed).isNotEqualTo(customTyped)
    }

    @Test
    fun `nullable typed object delegate works properly with null default value`() {
        var typed: Typed? by preferences.typedObject("nullableTyped", null)
        expectThat(typed).isEqualTo(null)
        typed = customTyped
        expectThat(typed).isEqualTo(customTyped)
        typed = null
        expectThat(typed).isEqualTo(null)
        expectThat(typed).isNotEqualTo(customTyped)
    }

    @Test
    fun `generic typed object delegate works properly`() {
        var generic: Generic<Typed> by preferences.typedObject(
            "generic",
            customGeneric,
            Types.newParameterizedType(Generic::class.java, Typed::class.java),
            commit = false
        )
        val typedObject = customTyped.copy(map = mapOf("" to ""))
        expectThat(generic).isEqualTo(customGeneric)
        generic = customGeneric.copy(typed = typedObject)
        expectThat(generic).isEqualTo(customGeneric.copy(typed = typedObject))
        expectThat(generic).isNotEqualTo(customGeneric)
    }

    @Test
    fun `nullable generic typed object delegate works properly with null default value`() {
        var generic: Generic<Typed>? by preferences.typedObject(
            "generic",
            null,
            Types.newParameterizedType(Generic::class.java, Typed::class.java),
            commit = false
        )
        expectThat(generic).isEqualTo(null)
        generic = customGeneric
        expectThat(generic).isEqualTo(customGeneric)
        generic = null
        expectThat(generic).isEqualTo(null)
        expectThat(generic).isNotEqualTo(customGeneric)
    }

    @Test
    fun `reading a primitive type as a different type throws ClassCastException`() {
        var int by preferences.int("primitive", 42)
        val string by preferences.string("primitive", "empty")
        int = 21
        expectThat(int).isEqualTo(21)
        expectThrows<ClassCastException> {
            string
        }
    }

    @Test
    fun `writing a primitive type as a different type overwrites previously stored data`() {
        var int by preferences.int("primitive", 42)
        var string by preferences.string("primitive", "empty")
        int = 21
        expectThat(int).isEqualTo(21)
        string = "21"
        expectThat(string).isEqualTo("21")
    }

    @Test
    fun `reading a typed object as a different type throws JsonDataException`() {
        var typed: Typed? by preferences.typedObject("typed", null)
        val nested: Nested by preferences.typedObject("typed", customNested)
        typed = customTyped
        expectThat(typed).isEqualTo(customTyped)
        expectThrows<JsonDataException> {
            nested
        }
    }

    @Test
    fun `writing a typed object as a different type overwrites previously stored data`() {
        var typed: Typed? by preferences.typedObject("typed", null)
        var nested: Nested by preferences.typedObject("typed", customNested)
        typed = customTyped
        expectThat(typed).isEqualTo(customTyped)
        nested = customNested
        expectThat(nested).isEqualTo(customNested)
    }

    @Suppress("UNUSED_VALUE", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
    @Test
    fun `serializing a non-primitive type without a proper adapter throws`() {
        expectCatching<Any?> {
            var dateObject: Date? by preferences.typedObject("date", null)
            dateObject = Date(42)
        }.failedWith<IllegalArgumentException>()
    }

    @Test
    fun `serializing a non-primitive type with a proper adapter passes`() {
        val adapter: JsonAdapter<Date?> = DateJsonAdapter().nullSafe()
        var dateObject: Date? by preferences.typedObject("date", null, adapter)
        val customDateObject = Date(42)
        dateObject = customDateObject
        expectThat(dateObject).isEqualTo(customDateObject)
    }
}
