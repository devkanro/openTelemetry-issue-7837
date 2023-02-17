package io.opentelemetry.issue7837

import com.bybutter.sisyphus.starter.grpc.transcoding.EnableHttpToGrpcTranscoding
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableHttpToGrpcTranscoding
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
