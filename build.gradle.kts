plugins {
    application
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.spring") version "1.7.10"
    id("com.bybutter.sisyphus.protobuf") version "1.6.0"
    id("org.springframework.boot") version "2.7.2"
}

group = "io.opentelemetry.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api("com.bybutter.sisyphus.starter:sisyphus-grpc-server-starter:1.6.0")
    api("com.bybutter.sisyphus.starter:sisyphus-grpc-transcoding-starter:1.6.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain {
        this.languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
    // jvmArgs("-javaagent:opentelemetry-javaagent.jar")
    // this.environment("OTEL_EXPORTER_OTLP_ENDPOINT", "YOUR_OTLP_ENDPOINT")
    this.environment("OTEL_SERVICE_NAME", "opentelemetry-issue7837")
}