package com.andresuryana.dicodingstories.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

object FlowTestUtil {

    suspend fun <T> Flow<T>.getOrAwaitValue(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ): T {
        var value: T? = null
        val latch = CountDownLatch(1)

        val collectJob = withContext(Dispatchers.Main) {
            launch {
                this@getOrAwaitValue
                    .flowOn(Dispatchers.Main)
                    .collect {
                        value = it
                        latch.countDown()
                    }
            }
        }

        // Wait for the value to be emitted or timeout
        return coroutineScope {
            if (!withContext(Dispatchers.IO) {
                    latch.await(time, timeUnit)
                }) {
                collectJob.cancel()
                throw TimeoutException("Flow value was not emitted within the timeout period")
            }
            @Suppress("UNCHECKED_CAST")
            value as T
        }
    }
}