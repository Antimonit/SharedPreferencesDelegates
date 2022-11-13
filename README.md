[![Maven Central](https://img.shields.io/maven-central/v/io.github.antimonit/shared-preferences-delegates.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.antimonit%22%20AND%20a:%22shared-preferences-delegates%22)
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
