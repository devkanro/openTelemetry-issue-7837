package io.opentelemetry.issue7837.component

import io.grpc.Context
import io.grpc.kotlin.GrpcContextElement
import kotlinx.coroutines.withContext

object AccessManager {
    val AUTH_KEY = Context.key<String>("auth")

    fun currentAuth(): String {
        return AUTH_KEY.get()
    }

    suspend fun withServiceAccount(block: suspend () -> Unit) {
        val newContext = Context.current().withValue(AUTH_KEY, "service-account")
        withContext(GrpcContextElement(newContext)) {
            block()
        }
    }
}