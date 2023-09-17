[![Maven Central](https://img.shields.io/maven-central/v/io.github.antimonit/spdelegates.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.antimonit%22%20AND%20a:%22spdelegates%22)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![CircleCI](https://img.shields.io/circleci/build/gh/Antimonit/SharedPreferencesDelegates?label=CircleCI)](https://circleci.com/gh/Antimonit/SharedPreferencesDelegates)
[![codecov](https://codecov.io/gh/Antimonit/SharedPreferencesDelegates/graph/badge.svg?token=ZK4VJZSPFI)](https://codecov.io/gh/Antimonit/SharedPreferencesDelegates)

# SharedPreferences delegates

A collection of type-safe delegates for `SharedPreferences` to cut down on the unnecessary 
boilerplate.

E.g. instead of writing this:
```kotlin
var serverEnvironment: String
    get() = sharedPreferences.getString(KEY_SERVER_ENVIRONMENT, null) ?: "BETA"
    set(value) {
        sharedPreferences.edit().putString(KEY_SERVER_ENVIRONMENT, value).apply()
    }

private const val KEY_SERVER_ENVIRONMENT = "server_environment"
```

we can just write this:

```kotlin
var serverEnvironment: String
    by sharedPreferences.string("server_environment", "BETA")
```

## Supported types

All types natively supported by `SharedPreferences` have their own delegates, namely:
* `Int`
* `Long`
* `Float`
* `Boolean`
* `String?`
* `Set<String>?`

In addition to all the types above, this library adds support for nullable primitive types

* `Int?`
* `Long?`
* `Float?`
* `Boolean?`

and non-nullable `String` and `Set<String>` types:
* `String`
* `Set<String>`

## Composite data

`SharedPreferences` natively does not support storing complex structures and that, in my opinion,
was a sane decision. `SharedPreferences` was not designed for such use cases as it has no mechanism
for migrating data from one primitive type to another, let alone complex composite data.

In cases where you need to store more complex structures, a database solution like Room with proper
support for migrations should be used instead. It might require you more setup upfront but will 
most likely save your sanity later. You have been warned.

Storing of complex structures is supported by two separate artifacts that utilize a 3rd party 
serialization library.

### Moshi
Classes to be stored in `SharedPreferences` must be annotated with 
`@JsonClass(generateAdapter = true)`.

```kotlin
@JsonClass(generateAdapter = true)
data class Composite(
    val text: String,
    val number: Int,
)

var composite: Composite
    by preferences.typedObject("composite", defaultComposite)

var nullableComposite: Composite?
    by preferences.typedObject("nullableComposite", null)
```

Due to type erasure, an explicit `Type` has to be passed when used with generic classes. 
```kotlin
@JsonClass(generateAdapter = true)
internal data class Generic<out T>(
    val value: T,
)

val type = Types.newParameterizedType(Generic::class.java, Composite::class.java)

var generic: Generic<Composite>
    by preferences.typedObject("generic", defaultGeneric, type)

var nullableGeneric: Generic<Composite>?
    by preferences.typedObject("nullableGeneric", null, type)
```

### Kotlin Serialization
Classes to be stored in `SharedPreferences` must be annotated with `@Serializable`.

```kotlin
@Serializable
data class Composite(
    val text: String,
    val number: Int,
)

var composite: Composite
    by preferences.typedObject("composite", defaultComposite)

var nullableComposite: Composite?
    by preferences.typedObject("nullableComposite", null)
```

Generic classes need no special handling

```kotlin
@Serializable
data class Generic<out T>(
    val value: T,
)

var generic: Generic<Composite>
    by preferences.typedObject("generic", defaultGeneric)

var nullableGeneric: Generic<Composite>?
    by preferences.typedObject("nullableGeneric", null)
```

## Download

The library is available from the MavenCentral repository:

```kotlin
// only basic types
implementation("io.github.antimonit:spdelegates:1.0.1")

// basic types + custom types using Moshi
implementation("io.github.antimonit:spdelegates-moshi:1.0.1")
// basic types + custom types using kotlinx.serialization
implementation("io.github.antimonit:spdelegates-serialization:1.0.1")
```
 