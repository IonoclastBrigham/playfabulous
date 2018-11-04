# PlayFabulous

A Kotlin-first PlayFab API client. It is built around coroutines, and eschews
any shared global data.

## Build and Usage

Build the library as normal with Gradle or IntelliJ.

Depends on Kotlin 1.3 and Coroutines lib 1.0.0.

### Integration

Instantiate a PlayFabulous and grab a handle to the target API.

```kotlin
val pf = PlayFabulous()
val loginApi = pf.loginApi
```

Once you've configured your request params, make the call. Most endpoints come in
three flavors: _async/await with explicit URL_ (not recommented for direct use),
_async/await_, and _suspending_ (the latter two being extension methods):

```kotlin
// fire off login in the background...
val pendingLogin = loginApi.loginWithEmailAsync(loginParams)

// ...doing some other stuff...

// ...now we'll suspend if necessary until login is complete or fails
val (res, err) = pendingLogin.await()
```

or...

```kotlin
// in suspending fun, continuation will run as soon as login completes...
val (res, err) = loginApi.loginWithEmail(loginParams)
```

If you don't need more explicit control over when your code might be suspended,
the latter suspending form is much more convenient and easier to read. Aside from
being able to do other things while the call is executing, the _async_ form has
the advantage that it can be called from a non-suspending context, and passed or
stored for later use from a coroutine.

### Error Handling

PlayFabulous will always return exactly one of your results, or an error. This
makes checking for errors very easy:

```kotlin
val (res, err) = someApi.someCall(callParams)
err?.let {
    handleError(err)
    return
}

// from here, res has our successful results...
```

In the future, guard blocks may be supported.

## Copyright and License

The PlayFabulous Library Copyright © 2017-2018 Ionoclast Laboratories, LLC.

It is distributable under the terms of a modified MIT License. You should have
received a copy of the license in the file LICENSE. If not, see:
  <https://github.com/IonoclastBrigham/playfabulous/blob/master/LICENSE>
