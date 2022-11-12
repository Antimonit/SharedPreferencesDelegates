@file:Suppress("KotlinConstantConditions")

package me.khol.spdelegates

import android.content.Context
import android.content.SharedPreferences
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import me.khol.spdelegates.common.edit
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@RunWith(AndroidJUnit4::class)
class SharedPreferencesDelegatesTest {

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
    fun `string delegate works properly`() {
        var string: String by preferences.string("string", "default")
        expectThat(string).isEqualTo("default")
        string = "value"
        expectThat(string).isEqualTo("value")
    }

    @Test
    fun `string set delegate works properly`() {
        var stringSet: Set<String> by preferences.stringSet("stringSet", setOf("default"))
        expectThat(stringSet).isEqualTo(setOf("default"))
        stringSet = setOf("string")
        expectThat(stringSet).isEqualTo(setOf("string"))
    }

    @Test
    fun `integer delegate works properly`() {
        var int: Int by preferences.int("int", 42)
        expectThat(int).isEqualTo(42)
        int = 21
        expectThat(int).isEqualTo(21)
    }

    @Test
    fun `long delegate works properly`() {
        var long: Long by preferences.long("long", 10L)
        expectThat(long).isEqualTo(10L)
        long = 5L
        expectThat(long).isEqualTo(5L)
    }

    @Test
    fun `float delegate works properly`() {
        var float: Float by preferences.float("float", 3.14f)
        expectThat(float).isEqualTo(3.14f)
        float = 2.71f
        expectThat(float).isEqualTo(2.71f)
    }

    @Test
    fun `boolean delegate works properly`() {
        var boolean: Boolean by preferences.boolean("boolean", true)
        expectThat(boolean).isEqualTo(true)
        boolean = false
        expectThat(boolean).isEqualTo(false)
    }

    @Test
    fun `nullable string delegate works properly with null default value`() {
        var string: String? by preferences.nullableString("nullableString")
        expectThat(string).isEqualTo(null)
        string = "value"
        expectThat(string).isEqualTo("value")
        string = null
        expectThat(string).isEqualTo(null)
    }

    @Test
    fun `nullable string set delegate works properly with null default value`() {
        var stringSet: Set<String>? by preferences.nullableStringSet("stringSet")
        expectThat(stringSet).isEqualTo(null)
        stringSet = setOf("string")
        expectThat(stringSet).isEqualTo(setOf("string"))
        stringSet = null
        expectThat(stringSet).isEqualTo(null)
    }

    @Test
    fun `nullable integer delegate works properly with null default value`() {
        var int: Int? by preferences.nullableInt("nullableInt")
        expectThat(int).isEqualTo(null)
        int = 21
        expectThat(int).isEqualTo(21)
        int = null
        expectThat(int).isEqualTo(null)
    }

    @Test
    fun `nullable long delegate works properly with null default value`() {
        var long: Long? by preferences.nullableLong("nullableLong")
        expectThat(long).isEqualTo(null)
        long = 5L
        expectThat(long).isEqualTo(5L)
        long = null
        expectThat(long).isEqualTo(null)
    }

    @Test
    fun `nullable float delegate works properly with null default value`() {
        var float: Float? by preferences.nullableFloat("nullableFloat")
        expectThat(float).isEqualTo(null)
        float = 2.71f
        expectThat(float).isEqualTo(2.71f)
        float = null
        expectThat(float).isEqualTo(null)
    }

    @Test
    fun `nullable boolean delegate works properly with null default value`() {
        var boolean: Boolean? by preferences.nullableBoolean("nullableBoolean")
        expectThat(boolean).isEqualTo(null)
        boolean = false
        expectThat(boolean).isEqualTo(false)
        boolean = null
        expectThat(boolean).isEqualTo(null)
    }
}
