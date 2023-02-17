package io.opentelemetry.issue7837.api

import com.bybutter.sisyphus.longrunning.Operation
import com.bybutter.sisyphus.middleware.grpc.RpcServiceImpl
import com.bybutter.sisyphus.rpc.Code
import com.bybutter.sisyphus.rpc.StatusException
import com.google.showcase.v1beta1.BlockRequest
import com.google.showcase.v1beta1.BlockResponse
import com.google.showcase.v1beta1.Echo
import com.google.showcase.v1beta1.EchoRequest
import com.google.showcase.v1beta1.EchoResponse
import com.google.showcase.v1beta1.ExpandRequest
import com.google.showcase.v1beta1.PagedExpandLegacyMappedResponse
import com.google.showcase.v1beta1.PagedExpandLegacyRequest
import com.google.showcase.v1beta1.PagedExpandRequest
import com.google.showcase.v1beta1.PagedExpandResponse
import com.google.showcase.v1beta1.WaitRequest
import io.opentelemetry.issue7837.component.AccessManager
import kotlinx.coroutines.flow.Flow
import org.slf4j.LoggerFactory

@RpcServiceImpl
class EchoApi : Echo() {
    private val logger = LoggerFactory.getLogger(EchoApi::class.java)

    override suspend fun echo(input: EchoRequest): EchoResponse {
        val auth = AccessManager.currentAuth()

        logger.info("Auth as '$auth'")

        AccessManager.withServiceAccount {
            val serviceAccount = AccessManager.currentAuth()
            logger.info("Auth as '$serviceAccount' in withServiceAccount block.")

            if (serviceAccount != "service-account") {
                throw StatusException(Code.PERMISSION_DENIED, "Service account is required.")
            }
        }

        return EchoResponse {
            content = input.content
        }
    }

    override fun expand(input: ExpandRequest): Flow<EchoResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun collect(input: Flow<EchoRequest>): EchoResponse {
        TODO("Not yet implemented")
    }

    override fun chat(input: Flow<EchoRequest>): Flow<EchoResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun pagedExpand(input: PagedExpandRequest): PagedExpandResponse {
        TODO("Not yet implemented")
    }

    override suspend fun pagedExpandLegacy(input: PagedExpandLegacyRequest): PagedExpandResponse {
        TODO("Not yet implemented")
    }

    override suspend fun pagedExpandLegacyMapped(input: PagedExpandRequest): PagedExpandLegacyMappedResponse {
        TODO("Not yet implemented")
    }

    override suspend fun wait(input: WaitRequest): Operation {
        TODO("Not yet implemented")
    }

    override suspend fun block(input: BlockRequest): BlockResponse {
        TODO("Not yet implemented")
    }
}