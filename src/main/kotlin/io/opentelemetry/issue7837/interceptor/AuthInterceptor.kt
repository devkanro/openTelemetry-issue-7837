package io.opentelemetry.issue7837.interceptor

import io.grpc.Context
import io.grpc.Contexts
import io.grpc.Metadata
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor
import io.opentelemetry.issue7837.component.AccessManager
import org.springframework.stereotype.Component

@Component
class AuthInterceptor : ServerInterceptor {
    private val AUTH_HEADER = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER)

    override fun <ReqT : Any?, RespT : Any?> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        val auth = headers.get(AUTH_HEADER) ?: "default"
        val context = Context.current().withValue(AccessManager.AUTH_KEY, auth)
        return Contexts.interceptCall(context, call, headers, next)
    }
}