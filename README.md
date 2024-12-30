# feature-flag-remover

Let's remove unused feature flag-related code!

## Usage

There are two annotations to mark removing targets; `RemoveAfterRelease` and `RemoveElseClauseAfterRelease`.

### RemoveAfterRelease

Please use `RemoveAfterRelease` when you want to remove whole the target statment.

```kotlin
fun main() {
  @RemoveAfterRelease("feature")
  val isReleased = true

  // You want to remove whole this if-statement
  @RemoveAfterRelease("feature")
  if (!isRelease) {
    beforeRelease() // You want to call beforeRelesae instead of afterRelease until the feature is release
    return
  }

  // You need this afterRelease call after release
  afterRelease()
}
```

This will be

```kotlin
fun main() {
  // You need this afterRelease call after release
  afterRelease()
}
```

### RemoveElseClauseAfterRelease

Please use `RemoveAfterRelease` when you want to remove else-clause of annotated if statement though keeping then-clause.

```kotlin
fun main() {
  @RemoveAfterRelease("feature")
  val isReleased = true

  // You want to keep then-clause
  @RemoveElseClausAfterRelease("feature")
  if (isRelease) {
    afterRelease()
  } else {
    beforeRelease()
  }
}
```

This will be

```kotlin
fun main() {
  afterRelease()
}
```

To remove those statements, please call `removeFeatureFlag` method.

```kotlin
removeFeatureFlagContext {
  val kotlinFile = Path("/path/to/kotlinFile")
  val featureFlagRemovedFileContent = removeFeatureFlag(kotlinFile.readText(), targetName = "feature")
  // update Kotlin file if you need
}
```

you can refer to [test code](https://github.com/T45K/feature-flag-remover/blob/master/src/test/kotlin/io/github/t45k/feature_flag_remover/api/IntegrationTest.kt)
