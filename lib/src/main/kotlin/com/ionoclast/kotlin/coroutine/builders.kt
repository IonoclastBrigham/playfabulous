package com.ionoclast.kotlin.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


fun launch(context: CoroutineContext = Dispatchers.Default, body: suspend CoroutineScope.()->Unit) = GlobalScope.launch(context, block = body)
