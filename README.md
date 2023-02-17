# OpenTelemetry issue 7837 repo

## Quick Start

### Run without open telemetry

#### 1. Run the application

```shell
./gradlew bootRun
```

#### 2. Send a request

```shell
curl -X POST --location "http://localhost:8080/v1beta1/echo:echo" \
-H "Content-Type: application/json" \
-d "{
\"content\": \"hello\"
}"
```

#### 3. Check the response and log

The server will return the same content as the request.

```shell
{
    "content": "hello"
}
```

And log will be like this:

```
2023-02-17 11:44:44.792  INFO 24159 --- [-1 @coroutine#1] io.opentelemetry.issue7837.api.EchoApi   : Auth as 'default'
2023-02-17 11:44:44.795  INFO 24159 --- [-1 @coroutine#1] io.opentelemetry.issue7837.api.EchoApi   : Auth as 'service-account' in withServiceAccount block.
2023-02-17 11:44:44.801  INFO 24159 --- [-1 @coroutine#1] RPC Request                              : [OK] google.showcase.v1beta1.Echo/Echo +189.303ms
```

### Run with open telemetry

#### 1. Config the open telemetry endpoint

Change the environment variable in `build.gradle.kts`

```kotlin
tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
    jvmArgs("-javaagent:opentelemetry-javaagent.jar")
    this.environment("OTEL_EXPORTER_OTLP_ENDPOINT", "YOUR_OTLP_ENDPOINT")
    this.environment("OTEL_SERVICE_NAME", "opentelemetry-issue7837")
}
```

#### 2. Run the application

```shell
./gradlew bootRun
```

#### 3. Send a request

```shell
curl -X POST --location "http://localhost:8080/v1beta1/echo:echo" \
-H "Content-Type: application/json" \
-d "{
\"content\": \"hello\"
}"
```

#### 4. Check the response and log

The server will return the 403 status with error message.

```shell
{
  "code": 7,
  "message": "Service account is required."
}
```

And log will be like this:

```
2023-02-17 11:48:19.019  INFO 24184 --- [-1 @coroutine#1] io.opentelemetry.issue7837.api.EchoApi   : Auth as 'default'
2023-02-17 11:48:19.028  INFO 24184 --- [-1 @coroutine#1] io.opentelemetry.issue7837.api.EchoApi   : Auth as 'default' in withServiceAccount block.
2023-02-17 11:48:19.059 ERROR 24184 --- [-1 @coroutine#1] RPC Request                              : [PERMISSION_DENIED] google.showcase.v1beta1.Echo/Echo +258.930ms

com.bybutter.sisyphus.rpc.StatusException: Service account is required.
	at io.opentelemetry.issue7837.api.EchoApi$echo$2.invokeSuspend(EchoApi.kt:36) ~[main/:na]
	at io.opentelemetry.issue7837.api.EchoApi$echo$2.invoke(EchoApi.kt) ~[main/:na]
	at io.opentelemetry.issue7837.api.EchoApi$echo$2.invoke(EchoApi.kt) ~[main/:na]
	at io.opentelemetry.issue7837.component.AccessManager$withServiceAccount$2.invokeSuspend(AccessManager.kt:17) ~[main/:na]
	at io.opentelemetry.issue7837.component.AccessManager$withServiceAccount$2.invoke(AccessManager.kt) ~[main/:na]
	at io.opentelemetry.issue7837.component.AccessManager$withServiceAccount$2.invoke(AccessManager.kt) ~[main/:na]
	at kotlinx.coroutines.intrinsics.UndispatchedKt.startUndispatchedOrReturn(Undispatched.kt:89) ~[kotlinx-coroutines-core-jvm-1.6.4.jar:na]
	at kotlinx.coroutines.BuildersKt__Builders_commonKt.withContext(Builders.common.kt:169) ~[kotlinx-coroutines-core-jvm-1.6.4.jar:na]
	at kotlinx.coroutines.BuildersKt.withContext(Unknown Source) ~[kotlinx-coroutines-core-jvm-1.6.4.jar:na]
	at io.opentelemetry.issue7837.component.AccessManager.withServiceAccount(AccessManager.kt:16) ~[main/:na]
	at io.opentelemetry.issue7837.api.EchoApi.echo$suspendImpl(EchoApi.kt:31) ~[main/:na]
	at io.opentelemetry.issue7837.api.EchoApi.echo(EchoApi.kt) ~[main/:na]
	at com.google.showcase.v1beta1.Echo$bindService$1.invoke(Echo.kt:585) ~[main/:na]
	at com.google.showcase.v1beta1.Echo$bindService$1.invoke(Echo.kt:585) ~[main/:na]
	at io.grpc.kotlin.ServerCalls$unaryServerMethodDefinition$2$invoke$$inlined$map$1$2.emit(Emitters.kt:224) ~[grpc-kotlin-stub-1.3.0.jar:na]
	at kotlinx.coroutines.flow.internal.SafeCollectorKt$emitFun$1.invoke(SafeCollector.kt:15) ~[kotlinx-coroutines-core-jvm-1.6.4.jar:na]
	at kotlinx.coroutines.flow.internal.SafeCollectorKt$emitFun$1.invoke(SafeCollector.kt:15) ~[kotlinx-coroutines-core-jvm-1.6.4.jar:na]
	at kotlinx.coroutines.flow.internal.SafeCollector.emit(SafeCollector.kt:87) ~[kotlinx-coroutines-core-jvm-1.6.4.jar:na]
	at kotlinx.coroutines.flow.internal.SafeCollector.emit(SafeCollector.kt:66) ~[kotlinx-coroutines-core-jvm-1.6.4.jar:na]
	at io.grpc.kotlin.HelpersKt$singleOrStatusFlow$1$1.emit(Helpers.kt:65) ~[grpc-kotlin-stub-1.3.0.jar:na]
	at kotlinx.coroutines.flow.internal.SafeCollectorKt$emitFun$1.invoke(SafeCollector.kt:15) ~[kotlinx-coroutines-core-jvm-1.6.4.jar:na]
	at kotlinx.coroutines.flow.internal.SafeCollectorKt$emitFun$1.invoke(SafeCollector.kt:15) ~[kotlinx-coroutines-core-jvm-1.6.4.jar:na]
	at kotlinx.coroutines.flow.internal.SafeCollector.emit(SafeCollector.kt:87) ~[kotlinx-coroutines-core-jvm-1.6.4.jar:na]
	at kotlinx.coroutines.flow.internal.SafeCollector.emit(SafeCollector.kt:66) ~[kotlinx-coroutines-core-jvm-1.6.4.jar:na]
	at io.grpc.kotlin.ServerCalls$serverCallListener$requests$1.invokeSuspend(ServerCalls.kt:228) ~[grpc-kotlin-stub-1.3.0.jar:na]
	at io.grpc.kotlin.ServerCalls$serverCallListener$requests$1.invoke(ServerCalls.kt) ~[grpc-kotlin-stub-1.3.0.jar:na]
	at io.grpc.kotlin.ServerCalls$serverCallListener$requests$1.invoke(ServerCalls.kt) ~[grpc-kotlin-stub-1.3.0.jar:na]
	at kotlinx.coroutines.flow.SafeFlow.collectSafely(Builders.kt:61) ~[kotlinx-coroutines-core-jvm-1.6.4.jar:na]
	at kotlinx.coroutines.flow.AbstractFlow.collect(Flow.kt:230) ~[kotlinx-coroutines-core-jvm-1.6.4.jar:na]
	at io.grpc.kotlin.HelpersKt$singleOrStatusFlow$1.invokeSuspend(Helpers.kt:62) ~[grpc-kotlin-stub-1.3.0.jar:na]
	at io.grpc.kotlin.HelpersKt$singleOrStatusFlow$1.invoke(Helpers.kt) ~[grpc-kotlin-stub-1.3.0.jar:na]
	at io.grpc.kotlin.HelpersKt$singleOrStatusFlow$1.invoke(Helpers.kt) ~[grpc-kotlin-stub-1.3.0.jar:na]
	at kotlinx.coroutines.flow.SafeFlow.collectSafely(Builders.kt:61) ~[kotlinx-coroutines-core-jvm-1.6.4.jar:na]
	at kotlinx.coroutines.flow.AbstractFlow.collect(Flow.kt:230) ~[kotlinx-coroutines-core-jvm-1.6.4.jar:na]
	at io.grpc.kotlin.ServerCalls$unaryServerMethodDefinition$2$invoke$$inlined$map$1.collect(SafeCollector.common.kt:113) ~[grpc-kotlin-stub-1.3.0.jar:na]
	at io.grpc.kotlin.ServerCalls$serverCallListener$rpcJob$1.invokeSuspend(ServerCalls.kt:244) ~[grpc-kotlin-stub-1.3.0.jar:na]
	at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:33) ~[kotlin-stdlib-1.7.10.jar:1.7.10-release-333(1.7.10)]
	at kotlinx.coroutines.DispatchedTask.run(DispatchedTask.kt:106) ~[kotlinx-coroutines-core-jvm-1.6.4.jar:na]
	at kotlinx.coroutines.scheduling.CoroutineScheduler.runSafely(CoroutineScheduler.kt:570) ~[kotlinx-coroutines-core-jvm-1.6.4.jar:na]
	at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.executeTask(CoroutineScheduler.kt:750) ~[kotlinx-coroutines-core-jvm-1.6.4.jar:na]
	at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.runWorker(CoroutineScheduler.kt:677) ~[kotlinx-coroutines-core-jvm-1.6.4.jar:na]
	at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.run(CoroutineScheduler.kt:664) ~[kotlinx-coroutines-core-jvm-1.6.4.jar:na]
```