# feature-flag-remover

Let's remove unused feature flag-related code!

This repository has only APIs.
Removal logic itself is written in [Plugin repository](https://github.com/T45K/feature-flag-remover-plugin).

## Requirement

- Kotlin 2.2.21+
- Java 21+
- Gradle 9.2+

## Setup

```kotlin:build.gradle.kts
plugins {
  kotlin("jvm") version "2.2.21"
  id("io.github.t45k.feature-flag-remover") version "1.0.2"
}

repositories {
  mavenCentral()
  maven { url = uri("https://jitpack.io") }
}

dependencies {
  implementation("com.github.t45k:feature-flag-remover:1.0.2")
}
```

You need to add `jitpack` URL to `repositories` block because `feature-flag-remover` is distributed
by [Jitpack](https://jitpack.io/#T45K/feature-flag-remover).

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
        beforeRelease() // You want to call beforeReleases instead of afterRelease until the feature is release
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

You can use multiple feature names in a signle annotation when the target is required by all the multiple features.

```kotlin
fun main() {
    @RemoveAfterRelease("feature", "feature2")
    val isReleased = true
}
```

When you try to `feature` feature flag, this code will be

```kotlin
fun main() {
    @RemoveAfterRelease("feature2")
    val isReleased = true
}
```

### RemoveElseClauseAfterRelease

Please use `RemoveAfterRelease` when you want to remove else-clause of annotated if-statement though keeping
then-clause.

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

You can execute such removal by `./gradlew removeFeatureFlag --feature feature_name --no-configuration-cache` command.
Currently, this command doesn't support configuration cache.

you can refer to [test code](https://github.com/T45K/feature-flag-remover-plugin/tree/master/src/test/resources)
