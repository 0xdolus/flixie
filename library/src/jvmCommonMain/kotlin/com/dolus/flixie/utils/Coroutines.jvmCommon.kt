package com.dolus.flixie.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual val workerDispatcher: CoroutineDispatcher = Dispatchers.IO
internal actual typealias WorkerThread = androidx.annotation.WorkerThread
