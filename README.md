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
