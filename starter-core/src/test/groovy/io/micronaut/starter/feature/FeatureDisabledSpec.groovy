package io.micronaut.starter.feature

import io.micronaut.context.ApplicationContext
import io.micronaut.core.util.StringUtils
import io.micronaut.starter.build.maven.GroovyMavenPlusPlugin
import io.micronaut.starter.feature.agorapulse.gru.GruHttp
import io.micronaut.starter.feature.agorapulse.permissions.Permissions
import io.micronaut.starter.feature.agorapulse.slack.Slack
import io.micronaut.starter.feature.agorapulse.worker.Worker
import io.micronaut.starter.feature.aop.AOP
import io.micronaut.starter.feature.architecture.Arm
import io.micronaut.starter.feature.architecture.X86
import io.micronaut.starter.feature.asciidoctor.Asciidoctor
import io.micronaut.starter.feature.aws.AmazonApiGateway
import io.micronaut.starter.feature.aws.AmazonApiGatewayHttp
import io.micronaut.starter.feature.aws.AmazonCloudWatchLogging
import io.micronaut.starter.feature.aws.AmazonCognito
import io.micronaut.starter.feature.aws.AwsLambdaEventsSerde
import io.micronaut.starter.feature.aws.AwsLambdaS3EventNotification
import io.micronaut.starter.feature.aws.AwsLambdaScheduledEvent
import io.micronaut.starter.feature.aws.AwsLambdaSnapstart
import io.micronaut.starter.feature.aws.AwsV2Sdk
import io.micronaut.starter.feature.aws.Cdk
import io.micronaut.starter.feature.aws.DynamoDb
import io.micronaut.starter.feature.aws.LambdaFunctionUrl
import io.micronaut.starter.feature.awsalexa.AwsAlexa
import io.micronaut.starter.feature.awslambdacustomruntime.AwsLambdaCustomRuntime
import io.micronaut.starter.feature.awsparameterstore.AwsParameterStore
import io.micronaut.starter.feature.awssecretsmanager.AwsSecretsManager
import io.micronaut.starter.feature.azure.AzureCosmosDbFeature
import io.micronaut.starter.feature.azure.AzureKeyVaultFeature
import io.micronaut.starter.feature.azure.AzureLogging
import io.micronaut.starter.feature.azure.AzureTracing
import io.micronaut.starter.feature.build.Develocity
import io.micronaut.starter.feature.build.Kapt
import io.micronaut.starter.feature.build.MicronautAot
import io.micronaut.starter.feature.build.MicronautBuildPlugin
import io.micronaut.starter.feature.build.MicronautDevelocity
import io.micronaut.starter.feature.build.gradle.Gradle
import io.micronaut.starter.feature.build.gradle.JavaGradlePlugin
import io.micronaut.starter.feature.build.maven.EnforcerPlugin
import io.micronaut.starter.feature.build.maven.Maven
import io.micronaut.starter.feature.buildless.Buildless
import io.micronaut.starter.feature.cache.Caffeine
import io.micronaut.starter.feature.cache.Coherence
import io.micronaut.starter.feature.cache.EHCache
import io.micronaut.starter.feature.cache.Hazelcast
import io.micronaut.starter.feature.cache.Infinispan
import io.micronaut.starter.feature.camunda.ExternalWorker
import io.micronaut.starter.feature.camunda.Platform7
import io.micronaut.starter.feature.camunda.Zeebe
import io.micronaut.starter.feature.chatbots.basecamp.BasecampAwsChatBot
import io.micronaut.starter.feature.chatbots.basecamp.BasecampAzureChatBot
import io.micronaut.starter.feature.chatbots.basecamp.BasecampGcpChatBot
import io.micronaut.starter.feature.chatbots.basecamp.BasecampHttpChatBot
import io.micronaut.starter.feature.chatbots.telegram.TelegramAwsChatBot
import io.micronaut.starter.feature.chatbots.telegram.TelegramAzureChatBot
import io.micronaut.starter.feature.chatbots.telegram.TelegramGcpChatBot
import io.micronaut.starter.feature.chatbots.telegram.TelegramHttpChatBot
import io.micronaut.starter.feature.ci.workflows.aws.AWSCiWorkflowFeature
import io.micronaut.starter.feature.ci.workflows.gcp.GoogleCloudCiWorkflowFeature
import io.micronaut.starter.feature.ci.workflows.github.GithubCiWorkflowFeature
import io.micronaut.starter.feature.ci.workflows.gitlab.GitlabCiWorkflowFeature
import io.micronaut.starter.feature.ci.workflows.oci.OCICiWorkflowFeature
import io.micronaut.starter.feature.coherence.CoherenceData
import io.micronaut.starter.feature.coherence.CoherenceDistributedConfiguration
import io.micronaut.starter.feature.coherence.CoherenceFeature

import io.micronaut.starter.feature.coherence.CoherenceSessionStore
import io.micronaut.starter.feature.config.Config4k
import io.micronaut.starter.feature.config.Toml
import io.micronaut.starter.feature.config.Yaml
import io.micronaut.starter.feature.consul.Consul
import io.micronaut.starter.feature.crac.Crac
import io.micronaut.starter.feature.database.Cassandra
import io.micronaut.starter.feature.database.Data
import io.micronaut.starter.feature.database.DataAzureCosmosFeature
import io.micronaut.starter.feature.database.DataHibernateReactive
import io.micronaut.starter.feature.database.DataJdbc
import io.micronaut.starter.feature.database.DataJpa
import io.micronaut.starter.feature.database.DataMongo
import io.micronaut.starter.feature.database.DataMongoReactive
import io.micronaut.starter.feature.database.DataSpringJdbcFeature
import io.micronaut.starter.feature.database.H2
import io.micronaut.starter.feature.database.HibernateJpa
import io.micronaut.starter.feature.database.HibernateJpaModelgen
import io.micronaut.starter.feature.database.HibernateReactiveJpa
import io.micronaut.starter.feature.database.JAsyncSQLFeature
import io.micronaut.starter.feature.database.Jooq
import io.micronaut.starter.feature.database.MariaDB
import io.micronaut.starter.feature.database.MongoReactive
import io.micronaut.starter.feature.database.MongoSync
import io.micronaut.starter.feature.database.MySQL
import io.micronaut.starter.feature.database.Neo4jBolt
import io.micronaut.starter.feature.database.Oracle
import io.micronaut.starter.feature.database.PostgreSQL
import io.micronaut.starter.feature.database.SQLServer
import io.micronaut.starter.feature.database.TestContainers
import io.micronaut.starter.feature.database.jdbc.Dbcp
import io.micronaut.starter.feature.database.jdbc.Hikari
import io.micronaut.starter.feature.database.jdbc.Ucp
import io.micronaut.starter.feature.database.r2dbc.DataR2dbc
import io.micronaut.starter.feature.database.r2dbc.R2dbc
import io.micronaut.starter.feature.dekorate.DekorateHalkyon
import io.micronaut.starter.feature.dekorate.DekorateJaeger
import io.micronaut.starter.feature.dekorate.DekorateKnative
import io.micronaut.starter.feature.dekorate.DekorateKubernetes
import io.micronaut.starter.feature.dekorate.DekorateOpenshift
import io.micronaut.starter.feature.dekorate.DekoratePrometheus
import io.micronaut.starter.feature.dekorate.DekorateServiceCatalog
import io.micronaut.starter.feature.dev.ControlPanel
import io.micronaut.starter.feature.discovery.DiscoveryClient
import io.micronaut.starter.feature.discovery.DiscoveryConsul
import io.micronaut.starter.feature.discovery.DiscoveryCore
import io.micronaut.starter.feature.discovery.DiscoveryKubernetes
import io.micronaut.starter.feature.discovery.Eureka
import io.micronaut.starter.feature.distributedconfig.DistributedConfigConsul
import io.micronaut.starter.feature.distributedconfig.KubernetesConfig
import io.micronaut.starter.feature.eclipsestore.EclipseStore
import io.micronaut.starter.feature.eclipsestore.EclipseStoreRest
import io.micronaut.starter.feature.elasticsearch.Elasticsearch
import io.micronaut.starter.feature.email.AmazonSesEmailFeature
import io.micronaut.starter.feature.email.JavamailFeature
import io.micronaut.starter.feature.email.MailjetEmailFeature
import io.micronaut.starter.feature.email.PostmarkEmailFeature
import io.micronaut.starter.feature.email.SendGridEmailFeature
import io.micronaut.starter.feature.email.TemplateEmailFeature
import io.micronaut.starter.feature.function.awslambda.ApiGatewayV2AwsLambdaHandlerProvider
import io.micronaut.starter.feature.function.awslambda.AwsLambda
import io.micronaut.starter.feature.function.awslambda.DefaultAwsLambdaHandlerProvider
import io.micronaut.starter.feature.function.awslambda.FunctionAwsLambdaHandlerProvider
import io.micronaut.starter.feature.function.azure.AzureHttpFunction
import io.micronaut.starter.feature.function.azure.AzureRawFunction
import io.micronaut.starter.feature.function.gcp.GoogleCloudEventsFunction
import io.micronaut.starter.feature.function.gcp.GoogleCloudFunction
import io.micronaut.starter.feature.function.gcp.GoogleCloudRawFunction
import io.micronaut.starter.feature.function.oraclefunction.OracleFunction
import io.micronaut.starter.feature.function.oraclefunction.OracleRawFunction
import io.micronaut.starter.feature.gcp.GoogleLogging
import io.micronaut.starter.feature.gcp.secretsmanager.GoogleSecretManager
import io.micronaut.starter.feature.github.workflows.azure.AzureContainerInstanceGraalWorkflow
import io.micronaut.starter.feature.github.workflows.azure.AzureContainerInstanceJavaWorkflow
import io.micronaut.starter.feature.github.workflows.docker.DockerRegistryWorkflow
import io.micronaut.starter.feature.github.workflows.docker.GraalVMDockerRegistryWorkflow
import io.micronaut.starter.feature.github.workflows.gcloud.GoogleCloudRunGraalWorkflow
import io.micronaut.starter.feature.github.workflows.gcloud.GoogleCloudRunJavaWorkflow
import io.micronaut.starter.feature.github.workflows.oci.OracleFunctionsGraalWorkflow
import io.micronaut.starter.feature.github.workflows.oci.OracleFunctionsJavaWorkflow
import io.micronaut.starter.feature.graallanguages.Graalpy
import io.micronaut.starter.feature.graalvm.GraalVM
import io.micronaut.starter.feature.graphql.GraphQL
import io.micronaut.starter.feature.grpc.Grpc
import io.micronaut.starter.feature.guice.MicronautGuice
import io.micronaut.starter.feature.httpclient.HttpClientJdk
import io.micronaut.starter.feature.jaxrs.JaxRs
import io.micronaut.starter.feature.jaxrs.JaxRsSecurity
import io.micronaut.starter.feature.jdbi.JdbiFeature
import io.micronaut.starter.feature.jib.Jib
import io.micronaut.starter.feature.jmx.Jmx
import io.micronaut.starter.feature.jobrunr.JobRunrFeature
import io.micronaut.starter.feature.json.JacksonDatabindFeature
import io.micronaut.starter.feature.json.JsonPath
import io.micronaut.starter.feature.json.JsonSchemaFeature
import io.micronaut.starter.feature.json.JsonSchemaValidationFeature
import io.micronaut.starter.feature.json.JsonSmart
import io.micronaut.starter.feature.json.SerializationBsonFeature
import io.micronaut.starter.feature.json.SerializationJacksonFeature
import io.micronaut.starter.feature.json.SerializationJsonpFeature
import io.micronaut.starter.feature.k8s.Kubernetes
import io.micronaut.starter.feature.k8s.KubernetesClient
import io.micronaut.starter.feature.k8s.KubernetesInformer
import io.micronaut.starter.feature.k8s.KubernetesReactorClient
import io.micronaut.starter.feature.knative.Knative
import io.micronaut.starter.feature.kotlin.KotlinExtensionFunctions
import io.micronaut.starter.feature.kotlin.Ktor
import io.micronaut.starter.feature.lang.groovy.Groovy
import io.micronaut.starter.feature.lang.groovy.GroovyApplication
import io.micronaut.starter.feature.lang.groovy.module.DateUtilGroovyModule
import io.micronaut.starter.feature.lang.groovy.module.DatetimeGroovyModule
import io.micronaut.starter.feature.lang.groovy.module.GinqGroovyModule
import io.micronaut.starter.feature.lang.groovy.module.JsonGroovyModule
import io.micronaut.starter.feature.lang.groovy.module.SqlGroovyModule
import io.micronaut.starter.feature.lang.groovy.module.TomlGroovyModule
import io.micronaut.starter.feature.lang.groovy.module.XmlGroovyModule
import io.micronaut.starter.feature.lang.groovy.module.YamlGroovyModule
import io.micronaut.starter.feature.lang.java.Java
import io.micronaut.starter.feature.lang.java.JavaApplication
import io.micronaut.starter.feature.lang.kotlin.Kotlin
import io.micronaut.starter.feature.lang.kotlin.KotlinApplication
import io.micronaut.starter.feature.langchain4j.embeddedstore.ElasticSearchLangchain4jEmbeddedStore
import io.micronaut.starter.feature.langchain4j.embeddedstore.MongoDbLangchain4jEmbeddedStore
import io.micronaut.starter.feature.langchain4j.embeddedstore.Neo4jLangchain4jEmbeddedStore
import io.micronaut.starter.feature.langchain4j.embeddedstore.OpenSearchLangchain4jEmbeddedStore
import io.micronaut.starter.feature.langchain4j.embeddedstore.OracleLangchain4jEmbeddedStore
import io.micronaut.starter.feature.langchain4j.embeddedstore.PgVectorLangchain4jEmbeddedStore
import io.micronaut.starter.feature.langchain4j.embeddedstore.QdrantLangchain4jEmbeddedStore
import io.micronaut.starter.feature.langchain4j.languagemodels.AnthropicLangchain4jLanguageModel
import io.micronaut.starter.feature.langchain4j.languagemodels.AzureLangchain4jLanguageModel
import io.micronaut.starter.feature.langchain4j.languagemodels.BedrockLangchain4jLanguageModel
import io.micronaut.starter.feature.langchain4j.languagemodels.GoogleAiGeminiLangchain4jLanguageModel
import io.micronaut.starter.feature.langchain4j.languagemodels.HuggingFaceLangchain4jLanguageModel
import io.micronaut.starter.feature.langchain4j.languagemodels.MistralAiLangchain4jLanguageModel
import io.micronaut.starter.feature.langchain4j.languagemodels.OllamaLangchain4jLanguageModel
import io.micronaut.starter.feature.langchain4j.languagemodels.OpenAiLangchain4jLanguageModel
import io.micronaut.starter.feature.langchain4j.languagemodels.VertexAiGeminiLangchain4jLanguageModel
import io.micronaut.starter.feature.langchain4j.languagemodels.VertexAiLangchain4jLanguageModel
import io.micronaut.starter.feature.logging.LiquibaseSlf4j
import io.micronaut.starter.feature.logging.Log4j2
import io.micronaut.starter.feature.logging.Logback
import io.micronaut.starter.feature.logging.SimpleLogging
import io.micronaut.starter.feature.logging.Slf4jJulBridge
import io.micronaut.starter.feature.logging.Slf4jSimpleLogger
import io.micronaut.starter.feature.messaging.jms.ActiveMqArtemis
import io.micronaut.starter.feature.messaging.jms.ActiveMqClassic
import io.micronaut.starter.feature.messaging.jms.JmsCore
import io.micronaut.starter.feature.messaging.jms.OracleAdvancedQueuing
import io.micronaut.starter.feature.messaging.jms.SQS
import io.micronaut.starter.feature.messaging.kafka.Kafka
import io.micronaut.starter.feature.messaging.kafka.KafkaStreams
import io.micronaut.starter.feature.messaging.mqtt.Mqtt
import io.micronaut.starter.feature.messaging.mqtt.MqttHiveMq
import io.micronaut.starter.feature.messaging.mqtt.MqttV3
import io.micronaut.starter.feature.messaging.nats.Nats
import io.micronaut.starter.feature.messaging.pubsub.PubSub
import io.micronaut.starter.feature.messaging.rabbitmq.RabbitMQ
import io.micronaut.starter.feature.micrometer.AppOptics
import io.micronaut.starter.feature.micrometer.Atlas
import io.micronaut.starter.feature.micrometer.AzureMonitor
import io.micronaut.starter.feature.micrometer.CloudWatch
import io.micronaut.starter.feature.micrometer.Core
import io.micronaut.starter.feature.micrometer.DataDog
import io.micronaut.starter.feature.micrometer.Dynatrace
import io.micronaut.starter.feature.micrometer.Elastic
import io.micronaut.starter.feature.micrometer.Ganglia
import io.micronaut.starter.feature.micrometer.Graphite
import io.micronaut.starter.feature.micrometer.Humio
import io.micronaut.starter.feature.micrometer.Influx
import io.micronaut.starter.feature.micrometer.Kairos
import io.micronaut.starter.feature.micrometer.MicrometerAnnotations
import io.micronaut.starter.feature.micrometer.MicrometerObservation
import io.micronaut.starter.feature.micrometer.MicrometerObservationHttp
import io.micronaut.starter.feature.micrometer.NewRelic
import io.micronaut.starter.feature.micrometer.OracleCloud
import io.micronaut.starter.feature.micrometer.Prometheus
import io.micronaut.starter.feature.micrometer.PrometheusPushGateway
import io.micronaut.starter.feature.micrometer.Signalfx
import io.micronaut.starter.feature.micrometer.Stackdriver
import io.micronaut.starter.feature.micrometer.Statsd
import io.micronaut.starter.feature.micrometer.Wavefront
import io.micronaut.starter.feature.migration.Flyway
import io.micronaut.starter.feature.migration.Liquibase
import io.micronaut.starter.feature.multitenancy.Multitenancy
import io.micronaut.starter.feature.netflix.Archaius
import io.micronaut.starter.feature.netflix.Hystrix
import io.micronaut.starter.feature.netflix.Ribbon
import io.micronaut.starter.feature.objectstorage.ObjectStorageAws
import io.micronaut.starter.feature.objectstorage.ObjectStorageAzure
import io.micronaut.starter.feature.objectstorage.ObjectStorageGcp
import io.micronaut.starter.feature.objectstorage.ObjectStorageLocal
import io.micronaut.starter.feature.objectstorage.ObjectStorageOracleCloud
import io.micronaut.starter.feature.opensearch.OpenSearchAmazon
import io.micronaut.starter.feature.opensearch.OpenSearchHttpClient5
import io.micronaut.starter.feature.opensearch.OpenSearchRestClient
import io.micronaut.starter.feature.opentelemetry.OpenTelemetry
import io.micronaut.starter.feature.opentelemetry.OpenTelemetryAnnotations
import io.micronaut.starter.feature.opentelemetry.OpenTelemetryExporterGoogleCloudTrace
import io.micronaut.starter.feature.opentelemetry.OpenTelemetryExporterJaeger
import io.micronaut.starter.feature.opentelemetry.OpenTelemetryExporterLogging
import io.micronaut.starter.feature.opentelemetry.OpenTelemetryExporterOtlp
import io.micronaut.starter.feature.opentelemetry.OpenTelemetryExporterZipkin
import io.micronaut.starter.feature.opentelemetry.OpenTelemetryGoogleCloudTrace
import io.micronaut.starter.feature.opentelemetry.OpenTelemetryGrpc
import io.micronaut.starter.feature.opentelemetry.OpenTelemetryHttp
import io.micronaut.starter.feature.opentelemetry.OpenTelemetryJaeger
import io.micronaut.starter.feature.opentelemetry.OpenTelemetryXray
import io.micronaut.starter.feature.opentelemetry.OpenTelemetryZipkin
import io.micronaut.starter.feature.oraclecloud.OracleCloudAutonomousDatabase
import io.micronaut.starter.feature.oraclecloud.OracleCloudLogging
import io.micronaut.starter.feature.oraclecloud.OracleCloudMicronautNettyClient
import io.micronaut.starter.feature.oraclecloud.OracleCloudSdk
import io.micronaut.starter.feature.oraclecloud.OracleCloudVault
import io.micronaut.starter.feature.other.AnnotationApi
import io.micronaut.starter.feature.other.AppName
import io.micronaut.starter.feature.other.HibernateValidator
import io.micronaut.starter.feature.other.HttpClientTest
import io.micronaut.starter.feature.other.HttpSession
import io.micronaut.starter.feature.other.Management
import io.micronaut.starter.feature.other.OpenApi
import io.micronaut.starter.feature.other.OpenApiAdoc
import io.micronaut.starter.feature.other.OpenApiExplorer
import io.micronaut.starter.feature.other.OpenRewrite
import io.micronaut.starter.feature.other.ProjectLombok
import io.micronaut.starter.feature.other.RapiDoc
import io.micronaut.starter.feature.other.Readme
import io.micronaut.starter.feature.other.Redoc
import io.micronaut.starter.feature.other.ShadePlugin
import io.micronaut.starter.feature.other.SwaggerUI
import io.micronaut.starter.feature.picocli.Picocli
import io.micronaut.starter.feature.picocli.lang.groovy.PicocliGroovyApplication
import io.micronaut.starter.feature.picocli.lang.java.PicocliJavaApplication
import io.micronaut.starter.feature.picocli.lang.kotlin.PicocliKotlinApplication
import io.micronaut.starter.feature.picocli.test.junit.PicocliJunit
import io.micronaut.starter.feature.picocli.test.kotest.PicocliKoTest
import io.micronaut.starter.feature.picocli.test.spock.PicocliSpock
import io.micronaut.starter.feature.problemjson.ProblemJson
import io.micronaut.starter.feature.reactor.Reactor
import io.micronaut.starter.feature.reactor.ReactorHttpClient
import io.micronaut.starter.feature.redis.RedisLettuce
import io.micronaut.starter.feature.reloading.Jrebel
import io.micronaut.starter.feature.retry.Retry
import io.micronaut.starter.feature.rss.Rss
import io.micronaut.starter.feature.rss.RssItunes
import io.micronaut.starter.feature.rxjava.RxJava3
import io.micronaut.starter.feature.rxjava.RxJava3HttpClient
import io.micronaut.starter.feature.security.Security
import io.micronaut.starter.feature.security.SecurityAnnotations
import io.micronaut.starter.feature.security.SecurityCsrf
import io.micronaut.starter.feature.security.SecurityJWT
import io.micronaut.starter.feature.security.SecurityLdap
import io.micronaut.starter.feature.security.SecurityOAuth2
import io.micronaut.starter.feature.security.SecuritySession
import io.micronaut.starter.feature.server.HttpPoja
import io.micronaut.starter.feature.server.Jetty
import io.micronaut.starter.feature.server.Netty
import io.micronaut.starter.feature.server.Tomcat
import io.micronaut.starter.feature.server.Undertow
import io.micronaut.starter.feature.sourcegen.SourcegenJava
import io.micronaut.starter.feature.spring.Spring
import io.micronaut.starter.feature.spring.SpringBoot
import io.micronaut.starter.feature.spring.SpringDataJdbc
import io.micronaut.starter.feature.spring.SpringDataJpa
import io.micronaut.starter.feature.spring.SpringWeb
import io.micronaut.starter.feature.stackdriver.CloudTrace
import io.micronaut.starter.feature.staticResources.StaticResourceFeature
import io.micronaut.starter.feature.test.AssertJ
import io.micronaut.starter.feature.test.Awaitility
import io.micronaut.starter.feature.test.Hamcrest
import io.micronaut.starter.feature.test.Junit
import io.micronaut.starter.feature.test.JunitParams
import io.micronaut.starter.feature.test.JunitPlatformSuiteEngine
import io.micronaut.starter.feature.test.KoTest
import io.micronaut.starter.feature.test.LocalStack
import io.micronaut.starter.feature.test.MicronautRestAssured
import io.micronaut.starter.feature.test.MockServerClient
import io.micronaut.starter.feature.test.Mockito
import io.micronaut.starter.feature.test.Mockk
import io.micronaut.starter.feature.test.Spock
import io.micronaut.starter.feature.testresources.TestResources
import io.micronaut.starter.feature.tracing.Jaeger
import io.micronaut.starter.feature.tracing.Zipkin
import io.micronaut.starter.feature.validator.MicronautHttpValidation
import io.micronaut.starter.feature.validator.MicronautValidationFeature
import io.micronaut.starter.feature.vertx.VertxMySql
import io.micronaut.starter.feature.vertx.VertxPg
import io.micronaut.starter.feature.view.Freemarker
import io.micronaut.starter.feature.view.Handlebars
import io.micronaut.starter.feature.view.JTE
import io.micronaut.starter.feature.view.Pebble
import io.micronaut.starter.feature.view.React
import io.micronaut.starter.feature.view.Rocker
import io.micronaut.starter.feature.view.Soy
import io.micronaut.starter.feature.view.Thymeleaf
import io.micronaut.starter.feature.view.Velocity
import io.micronaut.starter.feature.view.ViewsFieldset
import io.micronaut.starter.feature.view.ViewsFieldsetTck
import io.micronaut.starter.feature.websocket.Websocket
import io.micronaut.starter.feature.xml.JacksonXml
import io.micronaut.starter.springboot.SpringBootGradlePlugin
import io.micronaut.starter.springboot.SpringBootJava
import io.micronaut.starter.springboot.SpringBootMavenPlugin
import io.micronaut.starter.springboot.SpringBootStarter
import io.micronaut.starter.springboot.SpringBootStarterWeb
import io.micronaut.starter.springboot.SpringDependencyManagementGradlePlugin
import io.micronaut.starter.feature.config.Properties
import spock.lang.Specification

class FeatureDisabledSpec extends Specification {

    void 'test #feature feature is disabled with #config'(String feature, String config, Class clazz) {
        given:
        Map<String, Object> configuration = [:]
        configuration.put(config, StringUtils.FALSE)
        ApplicationContext ctx = ApplicationContext.run(configuration)

        expect:
        !ctx.containsBean(clazz)

        cleanup:
        ctx.close()

        where:
        feature                            | config                                                               | clazz
        'app-name' | 'micronaut.starter.feature.app.name.enabled' | AppName
        'aws-lambda-handler-default' | 'micronaut.starter.feature.aws.lambda.handler.default.enabled' | DefaultAwsLambdaHandlerProvider
        'jdbc-ucp' | 'micronaut.starter.feature.jdbc.ucp.enabled' | Ucp
        'jdbc-hikari' | 'micronaut.starter.feature.jdbc.hikari.enabled' | Hikari
        'mockk' | 'micronaut.starter.feature.mockk.enabled' | Mockk
        'jdbc-tomcat' | 'micronaut.starter.feature.jdbc.tomcat.enabled' | io.micronaut.starter.feature.database.jdbc.Tomcat
        'jdbc-dbcp' | 'micronaut.starter.feature.jdbc.dbcp.enabled' | Dbcp
        'data-r2dbc' | 'micronaut.starter.feature.data.r2dbc.enabled' | DataR2dbc
        'github-workflow-oracle-cloud-functions-graalvm' | 'micronaut.starter.feature.github.workflow.oracle.cloud.functions.graalvm.enabled' | OracleFunctionsGraalWorkflow
        'swagger-ui' | 'micronaut.starter.feature.swagger.ui.enabled' | SwaggerUI
        'github-workflow-graal-docker-registry' | 'micronaut.starter.feature.github.workflow.graal.docker.registry.enabled' | GraalVMDockerRegistryWorkflow
        'camunda-zeebe' | 'micronaut.starter.feature.camunda.zeebe.enabled' | Zeebe
        'jax-rs' | 'micronaut.starter.feature.jax.rs.enabled' | JaxRs
        'crac' | 'micronaut.starter.feature.crac.enabled' | Crac
        'agorapulse-micronaut-worker' | 'micronaut.starter.feature.agorapulse.micronaut.worker.enabled' | Worker
        'camunda-external-worker' | 'micronaut.starter.feature.camunda.external.worker.enabled' | ExternalWorker
        'config-kubernetes' | 'micronaut.starter.feature.config.kubernetes.enabled' | KubernetesConfig
        'mariadb' | 'micronaut.starter.feature.mariadb.enabled' | MariaDB
        'micrometer-appoptics' | 'micronaut.starter.feature.micrometer.appoptics.enabled' | AppOptics
        'java-gradle-plugin' | 'micronaut.starter.feature.java.gradle.plugin.enabled' | JavaGradlePlugin
        'jackson-databind' | 'micronaut.starter.feature.jackson.databind.enabled' | JacksonDatabindFeature
        'views-pebble' | 'micronaut.starter.feature.views.pebble.enabled' | Pebble
        'langchain4j-store-pgvector' | 'micronaut.starter.feature.langchain4j.store.pgvector.enabled' | PgVectorLangchain4jEmbeddedStore
        'spring' | 'micronaut.starter.feature.spring.enabled' | Spring
        'kotlin-extension-functions' | 'micronaut.starter.feature.kotlin.extension.functions.enabled' | KotlinExtensionFunctions
        'rabbitmq' | 'micronaut.starter.feature.rabbitmq.enabled' | RabbitMQ
        'aws-secrets-manager' | 'micronaut.starter.feature.aws.secrets.manager.enabled' | AwsSecretsManager
        'views-react' | 'micronaut.starter.feature.views.react.enabled' | React
        'asciidoctor' | 'micronaut.starter.feature.asciidoctor.enabled' | Asciidoctor
        'undertow-server' | 'micronaut.starter.feature.undertow.server.enabled' | Undertow
        'aws-lambda-s3-event-notification' | 'micronaut.starter.feature.aws.lambda.s3.event.notification.enabled' | AwsLambdaS3EventNotification
        'micrometer-oracle-cloud' | 'micronaut.starter.feature.micrometer.oracle.cloud.enabled' | OracleCloud
        'agorapulse-micronaut-console' | 'micronaut.starter.feature.agorapulse.micronaut.console.enabled' | Console
        'groovy-maven-plus-plugin' | 'micronaut.starter.feature.groovy.maven.plus.plugin.enabled' | GroovyMavenPlusPlugin
        'chatbots-basecamp-http' | 'micronaut.starter.feature.chatbots.basecamp.http.enabled' | BasecampHttpChatBot
        'cache-caffeine' | 'micronaut.starter.feature.cache.caffeine.enabled' | Caffeine
        'github-workflow-docker-registry' | 'micronaut.starter.feature.github.workflow.docker.registry.enabled' | DockerRegistryWorkflow
        'cache-ehcache' | 'micronaut.starter.feature.cache.ehcache.enabled' | EHCache
        'langchain4j-store-opensearch' | 'micronaut.starter.feature.langchain4j.store.opensearch.enabled' | OpenSearchLangchain4jEmbeddedStore
        'kubernetes-client' | 'micronaut.starter.feature.kubernetes.client.enabled' | KubernetesClient
        'arm' | 'micronaut.starter.feature.arm.enabled' | Arm
        'tomcat-server' | 'micronaut.starter.feature.tomcat.server.enabled' | Tomcat
        'object-storage-gcp' | 'micronaut.starter.feature.object.storage.gcp.enabled' | ObjectStorageGcp
        'sqlserver' | 'micronaut.starter.feature.sqlserver.enabled' | SQLServer
        'email-sendgrid' | 'micronaut.starter.feature.email.sendgrid.enabled' | SendGridEmailFeature
        'micrometer-graphite' | 'micronaut.starter.feature.micrometer.graphite.enabled' | Graphite
        'github-workflow-azure-container-instance' | 'micronaut.starter.feature.github.workflow.azure.container.instance.enabled' | AzureContainerInstanceJavaWorkflow
        'kafka' | 'micronaut.starter.feature.kafka.enabled' | Kafka
        'kubernetes' | 'micronaut.starter.feature.kubernetes.enabled' | Kubernetes
        'micrometer-influx' | 'micronaut.starter.feature.micrometer.influx.enabled' | Influx
        'buildless' | 'micronaut.starter.feature.buildless.enabled' | Buildless
        'tracing-opentelemetry-xray' | 'micronaut.starter.feature.tracing.opentelemetry.xray.enabled' | OpenTelemetryXray
        'views-soy' | 'micronaut.starter.feature.views.soy.enabled' | Soy
        'spring-data-jdbc' | 'micronaut.starter.feature.spring.data.jdbc.enabled' | SpringDataJdbc
        'micrometer-observation' | 'micronaut.starter.feature.micrometer.observation.enabled' | MicrometerObservation
        'micrometer-wavefront' | 'micronaut.starter.feature.micrometer.wavefront.enabled' | Wavefront
        'control-panel' | 'micronaut.starter.feature.control.panel.enabled' | ControlPanel
        'github-workflow-google-cloud-run-graalvm' | 'micronaut.starter.feature.github.workflow.google.cloud.run.graalvm.enabled' | GoogleCloudRunGraalWorkflow
        'sql-jdbi' | 'micronaut.starter.feature.sql.jdbi.enabled' | JdbiFeature
        'object-storage-local' | 'micronaut.starter.feature.object.storage.local.enabled' | ObjectStorageLocal
        'data-hibernate-reactive' | 'micronaut.starter.feature.data.hibernate.reactive.enabled' | DataHibernateReactive
        'http-client' | 'micronaut.starter.feature.http.client.enabled' | io.micronaut.starter.feature.other.HttpClient
        'oracle-cloud-vault' | 'micronaut.starter.feature.oracle.cloud.vault.enabled' | OracleCloudVault
        'google-cloud-function-cloudevents' | 'micronaut.starter.feature.google.cloud.function.cloudevents.enabled' | GoogleCloudEventsFunction
        'spring-boot' | 'micronaut.starter.feature.spring.boot.enabled' | SpringBoot
        'mongo-sync' | 'micronaut.starter.feature.mongo.sync.enabled' | MongoSync
        'google-cloud-workflow-ci' | 'micronaut.starter.feature.google.cloud.workflow.ci.enabled' | GoogleCloudCiWorkflowFeature
        'oracle-cloud-atp' | 'micronaut.starter.feature.oracle.cloud.atp.enabled' | OracleCloudAutonomousDatabase
        'postgres' | 'micronaut.starter.feature.postgres.enabled' | PostgreSQL
        'oracle' | 'micronaut.starter.feature.oracle.enabled' | Oracle
        'security' | 'micronaut.starter.feature.security.enabled' | Security
        'coherence-session' | 'micronaut.starter.feature.coherence.session.enabled' | CoherenceSessionStore
        'tracing-opentelemetry-gcp' | 'micronaut.starter.feature.tracing.opentelemetry.gcp.enabled' | OpenTelemetryGoogleCloudTrace
        'azure-function' | 'micronaut.starter.feature.azure.function.enabled' | AzureRawFunction
        'localstack' | 'micronaut.starter.feature.localstack.enabled' | LocalStack
        'http-session' | 'micronaut.starter.feature.http.session.enabled' | HttpSession
        'gcp-pubsub' | 'micronaut.starter.feature.gcp.pubsub.enabled' | PubSub
        'openapi-adoc' | 'micronaut.starter.feature.openapi.adoc.enabled' | OpenApiAdoc
        'security-csrf' | 'micronaut.starter.feature.security.csrf.enabled' | SecurityCsrf
        'views-freemarker' | 'micronaut.starter.feature.views.freemarker.enabled' | Freemarker
        'azure-cosmos-db' | 'micronaut.starter.feature.azure.cosmos.db.enabled' | AzureCosmosDbFeature
        'jib' | 'micronaut.starter.feature.jib.enabled' | Jib
        'jooq' | 'micronaut.starter.feature.jooq.enabled' | Jooq
        'serialization-jackson' | 'micronaut.starter.feature.serialization.jackson.enabled' | SerializationJacksonFeature
        'langchain4j-store-oracle' | 'micronaut.starter.feature.langchain4j.store.oracle.enabled' | OracleLangchain4jEmbeddedStore
        'tracing-jaeger' | 'micronaut.starter.feature.tracing.jaeger.enabled' | Jaeger
        'chatbots-basecamp-gcp-function' | 'micronaut.starter.feature.chatbots.basecamp.gcp.function.enabled' | BasecampGcpChatBot
        'jms-activemq-artemis' | 'micronaut.starter.feature.jms.activemq.artemis.enabled' | ActiveMqArtemis
        'amazon-api-gateway-http' | 'micronaut.starter.feature.amazon.api.gateway.http.enabled' | AmazonApiGatewayHttp
        'github-workflow-azure-container-instance-graalvm' | 'micronaut.starter.feature.github.workflow.azure.container.instance.graalvm.enabled' | AzureContainerInstanceGraalWorkflow
        'data-azure-cosmos' | 'micronaut.starter.feature.data.azure.cosmos.enabled' | DataAzureCosmosFeature
        'micrometer-observation-http' | 'micronaut.starter.feature.micrometer.observation.http.enabled' | MicrometerObservationHttp
        'views-thymeleaf' | 'micronaut.starter.feature.views.thymeleaf.enabled' | Thymeleaf
        'flyway' | 'micronaut.starter.feature.flyway.enabled' | Flyway
        'x86' | 'micronaut.starter.feature.x86.enabled' | X86
        'mockito' | 'micronaut.starter.feature.mockito.enabled' | Mockito
        'dekorate-openshift' | 'micronaut.starter.feature.dekorate.openshift.enabled' | DekorateOpenshift
        'mysql' | 'micronaut.starter.feature.mysql.enabled' | MySQL
        'aws-lambda-custom-runtime' | 'micronaut.starter.feature.aws.lambda.custom.runtime.enabled' | AwsLambdaCustomRuntime
        'langchain4j-hugging-face' | 'micronaut.starter.feature.langchain4j.hugging.face.enabled' | HuggingFaceLangchain4jLanguageModel
        'micrometer-atlas' | 'micronaut.starter.feature.micrometer.atlas.enabled' | Atlas
        'validation' | 'micronaut.starter.feature.validation.enabled' | MicronautValidationFeature
        'sourcegen-generator' | 'micronaut.starter.feature.sourcegen.generator.enabled' | SourcegenJava
        'object-storage-oracle-cloud' | 'micronaut.starter.feature.object.storage.oracle.cloud.enabled' | ObjectStorageOracleCloud
        'tracing-opentelemetry-grpc' | 'micronaut.starter.feature.tracing.opentelemetry.grpc.enabled' | OpenTelemetryGrpc
        'multi-tenancy' | 'micronaut.starter.feature.multi.tenancy.enabled' | Multitenancy
        'dekorate-servicecatalog' | 'micronaut.starter.feature.dekorate.servicecatalog.enabled' | DekorateServiceCatalog
        'langchain4j-store-mongodb-atlas' | 'micronaut.starter.feature.langchain4j.store.mongodb.atlas.enabled' | MongoDbLangchain4jEmbeddedStore
        'mqtt' | 'micronaut.starter.feature.mqtt.enabled' | Mqtt
        'micrometer-prometheus-pushgateway' | 'micronaut.starter.feature.micrometer.prometheus.pushgateway.enabled' | PrometheusPushGateway
        'hibernate-jpamodelgen' | 'micronaut.starter.feature.hibernate.jpamodelgen.enabled' | HibernateJpaModelgen
        'cache-infinispan' | 'micronaut.starter.feature.cache.infinispan.enabled' | Infinispan
        'micrometer-prometheus' | 'micronaut.starter.feature.micrometer.prometheus.enabled' | Prometheus
        'groovy-toml' | 'micronaut.starter.feature.groovy.toml.enabled' | TomlGroovyModule
        'coherence-distributed-configuration' | 'micronaut.starter.feature.coherence.distributed.configuration.enabled' | CoherenceDistributedConfiguration
        'aws-lambda-handler-apigateway-v2' | 'micronaut.starter.feature.aws.lambda.handler.apigateway.v2.enabled' | ApiGatewayV2AwsLambdaHandlerProvider
        'jms-sqs' | 'micronaut.starter.feature.jms.sqs.enabled' | SQS
        'email-template' | 'micronaut.starter.feature.email.template.enabled' | TemplateEmailFeature
        'openapi' | 'micronaut.starter.feature.openapi.enabled' | OpenApi
        'azure-key-vault' | 'micronaut.starter.feature.azure.key.vault.enabled' | AzureKeyVaultFeature
        'data' | 'micronaut.starter.feature.data.enabled' | Data
        'langchain4j-openai' | 'micronaut.starter.feature.langchain4j.openai.enabled' | OpenAiLangchain4jLanguageModel
        'dekorate-halkyon' | 'micronaut.starter.feature.dekorate.halkyon.enabled' | DekorateHalkyon
        'spring-dependency-management-gradle-plugin' | 'micronaut.starter.feature.spring.dependency.management.gradle.plugin.enabled' | SpringDependencyManagementGradlePlugin
        'redis-lettuce' | 'micronaut.starter.feature.redis.lettuce.enabled' | RedisLettuce
        'mongo-reactive' | 'micronaut.starter.feature.mongo.reactive.enabled' | MongoReactive
        'assertj' | 'micronaut.starter.feature.assertj.enabled' | AssertJ
        'groovy-dateutil' | 'micronaut.starter.feature.groovy.dateutil.enabled' | DateUtilGroovyModule
        'security-ldap' | 'micronaut.starter.feature.security.ldap.enabled' | SecurityLdap
        'spring-boot-starter' | 'micronaut.starter.feature.spring.boot.starter.enabled' | SpringBootStarter
        'micrometer-azure-monitor' | 'micronaut.starter.feature.micrometer.azure.monitor.enabled' | AzureMonitor
        'email-javamail' | 'micronaut.starter.feature.email.javamail.enabled' | JavamailFeature
        'jrebel' | 'micronaut.starter.feature.jrebel.enabled' | Jrebel
        'annotation-api' | 'micronaut.starter.feature.annotation.api.enabled' | AnnotationApi
        'security-annotations' | 'micronaut.starter.feature.security.annotations.enabled' | SecurityAnnotations
        'opensearch-restclient' | 'micronaut.starter.feature.opensearch.restclient.enabled' | OpenSearchRestClient
        'aws-v2-sdk' | 'micronaut.starter.feature.aws.v2.sdk.enabled' | AwsV2Sdk
        'aws-lambda-scheduled-event' | 'micronaut.starter.feature.aws.lambda.scheduled.event.enabled' | AwsLambdaScheduledEvent
        'groovy-json' | 'micronaut.starter.feature.groovy.json.enabled' | JsonGroovyModule
        'micrometer-ganglia' | 'micronaut.starter.feature.micrometer.ganglia.enabled' | Ganglia
        'data-jpa' | 'micronaut.starter.feature.data.jpa.enabled' | DataJpa
        'tracing-opentelemetry-exporter-jaeger' | 'micronaut.starter.feature.tracing.opentelemetry.exporter.jaeger.enabled' | OpenTelemetryExporterJaeger
        'data-mongodb-reactive' | 'micronaut.starter.feature.data.mongodb.reactive.enabled' | DataMongoReactive
        'chatbots-telegram-lambda' | 'micronaut.starter.feature.chatbots.telegram.lambda.enabled' | TelegramAwsChatBot
        'discovery-kubernetes' | 'micronaut.starter.feature.discovery.kubernetes.enabled' | DiscoveryKubernetes
        'tracing-zipkin' | 'micronaut.starter.feature.tracing.zipkin.enabled' | Zipkin
        'jetty-server' | 'micronaut.starter.feature.jetty.server.enabled' | Jetty
        'maven-enforcer-plugin' | 'micronaut.starter.feature.maven.enforcer.plugin.enabled' | EnforcerPlugin
        'dynamodb' | 'micronaut.starter.feature.dynamodb.enabled' | DynamoDb
        'develocity' | 'micronaut.starter.feature.develocity.enabled' | Develocity
        'micrometer-jmx' | 'micronaut.starter.feature.micrometer.jmx.enabled' | io.micronaut.starter.feature.micrometer.Jmx
        'nats' | 'micronaut.starter.feature.nats.enabled' | Nats
        'amazon-api-gateway' | 'micronaut.starter.feature.amazon.api.gateway.enabled' | AmazonApiGateway
        'micrometer-cloudwatch' | 'micronaut.starter.feature.micrometer.cloudwatch.enabled' | CloudWatch
        'kapt' | 'micronaut.starter.feature.kapt.enabled' | Kapt
        'oracle-cloud-logging' | 'micronaut.starter.feature.oracle.cloud.logging.enabled' | OracleCloudLogging
        'discovery-client' | 'micronaut.starter.feature.discovery.client.enabled' | DiscoveryClient
        'rss' | 'micronaut.starter.feature.rss.enabled' | Rss
        'picocli' | 'micronaut.starter.feature.picocli.enabled' | Picocli
        'azure-logging' | 'micronaut.starter.feature.azure.logging.enabled' | AzureLogging
        'azure-tracing' | 'micronaut.starter.feature.azure.tracing.enabled' | AzureTracing
        'amazon-cloudwatch-logging' | 'micronaut.starter.feature.amazon.cloudwatch.logging.enabled' | AmazonCloudWatchLogging
        'github-workflow-ci' | 'micronaut.starter.feature.github.workflow.ci.enabled' | GithubCiWorkflowFeature
        'eclipsestore' | 'micronaut.starter.feature.eclipsestore.enabled' | EclipseStore
        'gitlab-workflow-ci' | 'micronaut.starter.feature.gitlab.workflow.ci.enabled' | GitlabCiWorkflowFeature
        'logback' | 'micronaut.starter.feature.logback.enabled' | Logback
        'camunda-platform7' | 'micronaut.starter.feature.camunda.platform7.enabled' | Platform7
        'discovery-eureka' | 'micronaut.starter.feature.discovery.eureka.enabled' | Eureka
        'groovy-yaml' | 'micronaut.starter.feature.groovy.yaml.enabled' | YamlGroovyModule
        'vertx-pg-client' | 'micronaut.starter.feature.vertx.pg.client.enabled' | VertxPg
        'snapstart' | 'micronaut.starter.feature.snapstart.enabled' | AwsLambdaSnapstart
        'reactor-http-client' | 'micronaut.starter.feature.reactor.http.client.enabled' | ReactorHttpClient
        'spring-boot-starter-web' | 'micronaut.starter.feature.spring.boot.starter.web.enabled' | SpringBootStarterWeb
        'spring-web' | 'micronaut.starter.feature.spring.web.enabled' | SpringWeb
        'gcp-cloud-trace' | 'micronaut.starter.feature.gcp.cloud.trace.enabled' | CloudTrace
        'netflix-hystrix' | 'micronaut.starter.feature.netflix.hystrix.enabled' | Hystrix
        'micrometer-datadog' | 'micronaut.starter.feature.micrometer.datadog.enabled' | DataDog
        'h2' | 'micronaut.starter.feature.h2.enabled' | H2
        'gcp-secrets-manager' | 'micronaut.starter.feature.gcp.secrets.manager.enabled' | GoogleSecretManager
        'spring-boot-maven-plugin' | 'micronaut.starter.feature.spring.boot.maven.plugin.enabled' | SpringBootMavenPlugin
        'hibernate-reactive-jpa' | 'micronaut.starter.feature.hibernate.reactive.jpa.enabled' | HibernateReactiveJpa
        'elasticsearch' | 'micronaut.starter.feature.elasticsearch.enabled' | Elasticsearch
        'oracle-cloud-sdk' | 'micronaut.starter.feature.oracle.cloud.sdk.enabled' | OracleCloudSdk
        'json-schema' | 'micronaut.starter.feature.json.schema.enabled' | JsonSchemaFeature
        'jul-to-slf4j' | 'micronaut.starter.feature.jul.to.slf4j.enabled' | Slf4jJulBridge
        'reactor' | 'micronaut.starter.feature.reactor.enabled' | Reactor
        'kafka-streams' | 'micronaut.starter.feature.kafka.streams.enabled' | KafkaStreams
        'micronaut-test-rest-assured' | 'micronaut.starter.feature.micronaut.test.rest.assured.enabled' | MicronautRestAssured
        'opensearch-amazon' | 'micronaut.starter.feature.opensearch.amazon.enabled' | OpenSearchAmazon
        'micronaut-http-validation' | 'micronaut.starter.feature.micronaut.http.validation.enabled' | MicronautHttpValidation
        'jms-core' | 'micronaut.starter.feature.jms.core.enabled' | JmsCore
        'jms-oracle-aq' | 'micronaut.starter.feature.jms.oracle.aq.enabled' | OracleAdvancedQueuing
        'graalpy' | 'micronaut.starter.feature.graalpy.enabled' | Graalpy
        'langchain4j-bedrock' | 'micronaut.starter.feature.langchain4j.bedrock.enabled' | BedrockLangchain4jLanguageModel
        'tracing-opentelemetry-exporter-gcp' | 'micronaut.starter.feature.tracing.opentelemetry.exporter.gcp.enabled' | OpenTelemetryExporterGoogleCloudTrace
        'slf4j-simple-logger' | 'micronaut.starter.feature.slf4j.simple.logger.enabled' | Slf4jSimpleLogger
        'slf4j-simple' | 'micronaut.starter.feature.slf4j.simple.enabled' | SimpleLogging
        'tracing-opentelemetry-exporter-otlp' | 'micronaut.starter.feature.tracing.opentelemetry.exporter.otlp.enabled' | OpenTelemetryExporterOtlp
        'test-resources' | 'micronaut.starter.feature.test.resources.enabled' | TestResources
        'spring-data-jpa' | 'micronaut.starter.feature.spring.data.jpa.enabled' | SpringDataJpa
        'groovy-xml' | 'micronaut.starter.feature.groovy.xml.enabled' | XmlGroovyModule
        'data-spring-jdbc' | 'micronaut.starter.feature.data.spring.jdbc.enabled' | DataSpringJdbcFeature
        'micrometer-stackdriver' | 'micronaut.starter.feature.micrometer.stackdriver.enabled' | Stackdriver
        'jasync-sql' | 'micronaut.starter.feature.jasync.sql.enabled' | JAsyncSQLFeature
        'langchain4j-anthropic' | 'micronaut.starter.feature.langchain4j.anthropic.enabled' | AnthropicLangchain4jLanguageModel
        'guice' | 'micronaut.starter.feature.guice.enabled' | MicronautGuice
        'r2dbc' | 'micronaut.starter.feature.r2dbc.enabled' | R2dbc
        'aws-parameter-store' | 'micronaut.starter.feature.aws.parameter.store.enabled' | AwsParameterStore
        'eclipsestore-rest' | 'micronaut.starter.feature.eclipsestore.rest.enabled' | EclipseStoreRest
        'views-velocity' | 'micronaut.starter.feature.views.velocity.enabled' | Velocity
        'graalvm' | 'micronaut.starter.feature.graalvm.enabled' | GraalVM
        'config-consul' | 'micronaut.starter.feature.config.consul.enabled' | DistributedConfigConsul
        'mqtt-hivemq' | 'micronaut.starter.feature.mqtt.hivemq.enabled' | MqttHiveMq
        'tracing-opentelemetry' | 'micronaut.starter.feature.tracing.opentelemetry.enabled' | OpenTelemetry
        'google-cloud-function' | 'micronaut.starter.feature.google.cloud.function.enabled' | GoogleCloudRawFunction
        'views-jte' | 'micronaut.starter.feature.views.jte.enabled' | JTE
        'tracing-opentelemetry-exporter-logging' | 'micronaut.starter.feature.tracing.opentelemetry.exporter.logging.enabled' | OpenTelemetryExporterLogging
        'aws-lambda-handler-function' | 'micronaut.starter.feature.aws.lambda.handler.function.enabled' | FunctionAwsLambdaHandlerProvider
        'serialization-bson' | 'micronaut.starter.feature.serialization.bson.enabled' | SerializationBsonFeature
        'dekorate-jaeger' | 'micronaut.starter.feature.dekorate.jaeger.enabled' | DekorateJaeger
        'tracing-opentelemetry-zipkin' | 'micronaut.starter.feature.tracing.opentelemetry.zipkin.enabled' | OpenTelemetryZipkin
        'oracle-function' | 'micronaut.starter.feature.oracle.function.enabled' | OracleRawFunction
        'dekorate-prometheus' | 'micronaut.starter.feature.dekorate.prometheus.enabled' | DekoratePrometheus
        'graphql' | 'micronaut.starter.feature.graphql.enabled' | GraphQL
        'netflix-ribbon' | 'micronaut.starter.feature.netflix.ribbon.enabled' | Ribbon
        'rapidoc' | 'micronaut.starter.feature.rapidoc.enabled' | RapiDoc
        'micrometer-kairos' | 'micronaut.starter.feature.micrometer.kairos.enabled' | Kairos
        'views-fieldset' | 'micronaut.starter.feature.views.fieldset.enabled' | ViewsFieldset
        'langchain4j-store-elasticsearch' | 'micronaut.starter.feature.langchain4j.store.elasticsearch.enabled' | ElasticSearchLangchain4jEmbeddedStore
        'jax-rs-security' | 'micronaut.starter.feature.jax.rs.security.enabled' | JaxRsSecurity
        'jmx' | 'micronaut.starter.feature.jmx.enabled' | Jmx
        'kubernetes-reactor-client' | 'micronaut.starter.feature.kubernetes.reactor.client.enabled' | KubernetesReactorClient
        'junit-platform-suite-engine' | 'micronaut.starter.feature.junit.platform.suite.engine.enabled' | JunitPlatformSuiteEngine
        'cache-coherence' | 'micronaut.starter.feature.cache.coherence.enabled' | Coherence
        'lombok' | 'micronaut.starter.feature.lombok.enabled' | ProjectLombok
        'management' | 'micronaut.starter.feature.management.enabled' | Management
        'tracing-opentelemetry-annotations' | 'micronaut.starter.feature.tracing.opentelemetry.annotations.enabled' | OpenTelemetryAnnotations
        'http-poja' | 'micronaut.starter.feature.http.poja.enabled' | HttpPoja
        'jobrunr-jobrunr' | 'micronaut.starter.feature.jobrunr.jobrunr.enabled' | JobRunrFeature
        'redoc' | 'micronaut.starter.feature.redoc.enabled' | Redoc
        'retry' | 'micronaut.starter.feature.retry.enabled' | Retry
        'langchain4j-googleai-gemini' | 'micronaut.starter.feature.langchain4j.googleai.gemini.enabled' | GoogleAiGeminiLangchain4jLanguageModel
        'junit-params' | 'micronaut.starter.feature.junit.params.enabled' | JunitParams
        'agorapulse-micronaut-permissions' | 'micronaut.starter.feature.agorapulse.micronaut.permissions.enabled' | Permissions
        'jackson-xml' | 'micronaut.starter.feature.jackson.xml.enabled' | JacksonXml
        'tracing-opentelemetry-jaeger' | 'micronaut.starter.feature.tracing.opentelemetry.jaeger.enabled' | OpenTelemetryJaeger
        'langchain4j-store-neo4j' | 'micronaut.starter.feature.langchain4j.store.neo4j.enabled' | Neo4jLangchain4jEmbeddedStore
        'rxjava3' | 'micronaut.starter.feature.rxjava3.enabled' | RxJava3
        'kubernetes-informer' | 'micronaut.starter.feature.kubernetes.informer.enabled' | KubernetesInformer
        'langchain4j-store-qdrant' | 'micronaut.starter.feature.langchain4j.store.qdrant.enabled' | QdrantLangchain4jEmbeddedStore
        'langchain4j-vertexai-gemini' | 'micronaut.starter.feature.langchain4j.vertexai.gemini.enabled' | VertexAiGeminiLangchain4jLanguageModel
        'json-schema-validation' | 'micronaut.starter.feature.json.schema.validation.enabled' | JsonSchemaValidationFeature
        'groovy-ginq' | 'micronaut.starter.feature.groovy.ginq.enabled' | GinqGroovyModule
        'consul' | 'micronaut.starter.feature.consul.enabled' | Consul
        'dekorate-kubernetes' | 'micronaut.starter.feature.dekorate.kubernetes.enabled' | DekorateKubernetes
        'openrewrite' | 'micronaut.starter.feature.openrewrite.enabled' | OpenRewrite
        'netty-server' | 'micronaut.starter.feature.netty.server.enabled' | Netty
        'data-mongodb' | 'micronaut.starter.feature.data.mongodb.enabled' | DataMongo
        'groovy-datetime' | 'micronaut.starter.feature.groovy.datetime.enabled' | DatetimeGroovyModule
        'micronaut-aop' | 'micronaut.starter.feature.micronaut.aop.enabled' | AOP
        'langchain4j-vertexai' | 'micronaut.starter.feature.langchain4j.vertexai.enabled' | VertexAiLangchain4jLanguageModel
        'langchain4j-ollama' | 'micronaut.starter.feature.langchain4j.ollama.enabled' | OllamaLangchain4jLanguageModel
        'dekorate-knative' | 'micronaut.starter.feature.dekorate.knative.enabled' | DekorateKnative
        'micrometer-humio' | 'micronaut.starter.feature.micrometer.humio.enabled' | Humio
        'views-rocker' | 'micronaut.starter.feature.views.rocker.enabled' | Rocker
        'json-smart' | 'micronaut.starter.feature.json.smart.enabled' | JsonSmart
        'chatbots-telegram-azure-function' | 'micronaut.starter.feature.chatbots.telegram.azure.function.enabled' | TelegramAzureChatBot
        'openapi-explorer' | 'micronaut.starter.feature.openapi.explorer.enabled' | OpenApiExplorer
        'grpc' | 'micronaut.starter.feature.grpc.enabled' | Grpc
        'neo4j-bolt' | 'micronaut.starter.feature.neo4j.bolt.enabled' | Neo4jBolt
        'chatbots-basecamp-lambda' | 'micronaut.starter.feature.chatbots.basecamp.lambda.enabled' | BasecampAwsChatBot
        'oracle-cloud-httpclient-netty' | 'micronaut.starter.feature.oracle.cloud.httpclient.netty.enabled' | OracleCloudMicronautNettyClient
        'aws-lambda' | 'micronaut.starter.feature.aws.lambda.enabled' | AwsLambda
        'security-jwt' | 'micronaut.starter.feature.security.jwt.enabled' | SecurityJWT
        'amazon-cognito' | 'micronaut.starter.feature.amazon.cognito.enabled' | AmazonCognito
        'github-workflow-google-cloud-run' | 'micronaut.starter.feature.github.workflow.google.cloud.run.enabled' | GoogleCloudRunJavaWorkflow
        'websocket' | 'micronaut.starter.feature.websocket.enabled' | Websocket
        'aws-alexa' | 'micronaut.starter.feature.aws.alexa.enabled' | AwsAlexa
        'rss-itunes-podcast' | 'micronaut.starter.feature.rss.itunes.podcast.enabled' | RssItunes
        'tracing-opentelemetry-exporter-zipkin' | 'micronaut.starter.feature.tracing.opentelemetry.exporter.zipkin.enabled' | OpenTelemetryExporterZipkin
        'views-fieldset-tck' | 'micronaut.starter.feature.views.fieldset.tck.enabled' | ViewsFieldsetTck
        'azure-function-http' | 'micronaut.starter.feature.azure.function.http.enabled' | AzureHttpFunction
        'log4j2' | 'micronaut.starter.feature.log4j2.enabled' | Log4j2
        'langchain4j-azure' | 'micronaut.starter.feature.langchain4j.azure.enabled' | AzureLangchain4jLanguageModel
        'problem-json' | 'micronaut.starter.feature.problem.json.enabled' | ProblemJson
        'micrometer' | 'micronaut.starter.feature.micrometer.enabled' | Core
        'rxjava3-http-client' | 'micronaut.starter.feature.rxjava3.http.client.enabled' | RxJava3HttpClient
        'knative' | 'micronaut.starter.feature.knative.enabled' | Knative
        'vertx-mysql-client' | 'micronaut.starter.feature.vertx.mysql.client.enabled' | VertxMySql
        'object-storage-aws' | 'micronaut.starter.feature.object.storage.aws.enabled' | ObjectStorageAws
        'agorapulse-micronaut-slack' | 'micronaut.starter.feature.agorapulse.micronaut.slack.enabled' | Slack
        'oracle-function-http' | 'micronaut.starter.feature.oracle.function.http.enabled' | OracleFunction
        'gcp-logging' | 'micronaut.starter.feature.gcp.logging.enabled' | GoogleLogging
        'opensearch-httpclient5' | 'micronaut.starter.feature.opensearch.httpclient5.enabled' | OpenSearchHttpClient5
        'http-client-jdk' | 'micronaut.starter.feature.http.client.jdk.enabled' | HttpClientJdk
        'data-jdbc' | 'micronaut.starter.feature.data.jdbc.enabled' | DataJdbc
        'views-handlebars' | 'micronaut.starter.feature.views.handlebars.enabled' | Handlebars
        'github-workflow-oracle-cloud-functions' | 'micronaut.starter.feature.github.workflow.oracle.cloud.functions.enabled' | OracleFunctionsJavaWorkflow
        'static-resources' | 'micronaut.starter.feature.static.resources.enabled' | StaticResourceFeature
        'discovery-core' | 'micronaut.starter.feature.discovery.core.enabled' | DiscoveryCore
        'tracing-opentelemetry-http' | 'micronaut.starter.feature.tracing.opentelemetry.http.enabled' | OpenTelemetryHttp
        'chatbots-telegram-http' | 'micronaut.starter.feature.chatbots.telegram.http.enabled' | TelegramHttpChatBot
        'micronaut-aot' | 'micronaut.starter.feature.micronaut.aot.enabled' | MicronautAot
        'jms-activemq-classic' | 'micronaut.starter.feature.jms.activemq.classic.enabled' | ActiveMqClassic
        'micrometer-elastic' | 'micronaut.starter.feature.micrometer.elastic.enabled' | Elastic
        'aws-lambda-events-serde' | 'micronaut.starter.feature.aws.lambda.events.serde.enabled' | AwsLambdaEventsSerde
        'email-mailjet' | 'micronaut.starter.feature.email.mailjet.enabled' | MailjetEmailFeature
        'cassandra' | 'micronaut.starter.feature.cassandra.enabled' | Cassandra
        'micronaut-develocity' | 'micronaut.starter.feature.micronaut.develocity.enabled' | MicronautDevelocity
        'ksp' | 'micronaut.starter.feature.ksp.enabled' | KotlinSymbolProcessing
        'groovy-sql' | 'micronaut.starter.feature.groovy.sql.enabled' | SqlGroovyModule
        'micrometer-dynatrace' | 'micronaut.starter.feature.micrometer.dynatrace.enabled' | Dynatrace
        'oracle-cloud-devops-build-ci' | 'micronaut.starter.feature.oracle.cloud.devops.build.ci.enabled' | OCICiWorkflowFeature
        'awaitility' | 'micronaut.starter.feature.awaitility.enabled' | Awaitility
        'email-postmark' | 'micronaut.starter.feature.email.postmark.enabled' | PostmarkEmailFeature
        'liquibase' | 'micronaut.starter.feature.liquibase.enabled' | Liquibase
        'hamcrest' | 'micronaut.starter.feature.hamcrest.enabled' | Hamcrest
        'google-cloud-function-http' | 'micronaut.starter.feature.google.cloud.function.http.enabled' | GoogleCloudFunction
        'chatbots-telegram-gcp-function' | 'micronaut.starter.feature.chatbots.telegram.gcp.function.enabled' | TelegramGcpChatBot
        'micrometer-signalfx' | 'micronaut.starter.feature.micrometer.signalfx.enabled' | Signalfx
        'coherence' | 'micronaut.starter.feature.coherence.enabled' | CoherenceFeature
        'agorapulse-gru-http' | 'micronaut.starter.feature.agorapulse.gru.http.enabled' | GruHttp
        'netflix-archaius' | 'micronaut.starter.feature.netflix.archaius.enabled' | Archaius
        'liquibase-slf4j' | 'micronaut.starter.feature.liquibase.slf4j.enabled' | LiquibaseSlf4j
        'hibernate-jpa' | 'micronaut.starter.feature.hibernate.jpa.enabled' | HibernateJpa
        'aws-codebuild-workflow-ci' | 'micronaut.starter.feature.aws.codebuild.workflow.ci.enabled' | AWSCiWorkflowFeature
        'cache-hazelcast' | 'micronaut.starter.feature.cache.hazelcast.enabled' | Hazelcast
        'object-storage-azure' | 'micronaut.starter.feature.object.storage.azure.enabled' | ObjectStorageAzure
        'chatbots-basecamp-azure-function' | 'micronaut.starter.feature.chatbots.basecamp.azure.function.enabled' | BasecampAzureChatBot
        'langchain4j-mistralai' | 'micronaut.starter.feature.langchain4j.mistralai.enabled' | MistralAiLangchain4jLanguageModel
        'micrometer-annotation' | 'micronaut.starter.feature.micrometer.annotation.enabled' | MicrometerAnnotations
        'hibernate-validator' | 'micronaut.starter.feature.hibernate.validator.enabled' | HibernateValidator
        'mockserver-client-java' | 'micronaut.starter.feature.mockserver.client.java.enabled' | MockServerClient
        'micrometer-new-relic' | 'micronaut.starter.feature.micrometer.new.relic.enabled' | NewRelic
        'serialization-jsonp' | 'micronaut.starter.feature.serialization.jsonp.enabled' | SerializationJsonpFeature
        'email-amazon-ses' | 'micronaut.starter.feature.email.amazon.ses.enabled' | AmazonSesEmailFeature
        'mqttv3' | 'micronaut.starter.feature.mqttv3.enabled' | MqttV3
        'discovery-consul' | 'micronaut.starter.feature.discovery.consul.enabled' | DiscoveryConsul
        'coherence-data' | 'micronaut.starter.feature.coherence.data.enabled' | CoherenceData
        'json-path' | 'micronaut.starter.feature.json.path.enabled' | JsonPath
        'aws-lambda-function-url' | 'micronaut.starter.feature.aws.lambda.function.url.enabled' | LambdaFunctionUrl
        'micrometer-statsd' | 'micronaut.starter.feature.micrometer.statsd.enabled' | Statsd
        'security-oauth2' | 'micronaut.starter.feature.security.oauth2.enabled' | SecurityOAuth2
        'security-session' | 'micronaut.starter.feature.security.session.enabled' | SecuritySession
        'aws-cdk' | 'micronaut.starter.feature.aws.cdk.enabled' | Cdk
        'picocli-groovy-application' | 'micronaut.starter.feature.picocli.groovy.application.enabled' | PicocliGroovyApplication
        'groovy' | 'micronaut.starter.feature.groovy.enabled' | Groovy
        'picocli-kotlin-application' | 'micronaut.starter.feature.picocli.kotlin.application.enabled' | PicocliKotlinApplication
        'picocli-java-application' | 'micronaut.starter.feature.picocli.java.application.enabled' | PicocliJavaApplication
        'java' | 'micronaut.starter.feature.java.enabled' | Java
        'kotlin-application' | 'micronaut.starter.feature.kotlin.application.enabled' | KotlinApplication
        'ktor' | 'micronaut.starter.feature.ktor.enabled' | Ktor
        'groovy-application' | 'micronaut.starter.feature.groovy.application.enabled' | GroovyApplication
        'kotlin' | 'micronaut.starter.feature.kotlin.enabled' | Kotlin
        'springboot-java' | 'micronaut.starter.feature.springboot.java.enabled' | SpringBootJava
        'java-application' | 'micronaut.starter.feature.java.application.enabled' | JavaApplication
        'kotest' | 'micronaut.starter.feature.kotest.enabled' | KoTest
        'junit' | 'micronaut.starter.feature.junit.enabled' | Junit
        'shade' | 'micronaut.starter.feature.shade.enabled' | ShadePlugin
        'micronaut-build' | 'micronaut.starter.feature.micronaut.build.enabled' | MicronautBuildPlugin
        'springboot-gradle-plugin' | 'micronaut.starter.feature.springboot.gradle.plugin.enabled' | SpringBootGradlePlugin
        'http-client-test' | 'micronaut.starter.feature.http.client.test.enabled' | HttpClientTest
        'spock' | 'micronaut.starter.feature.spock.enabled' | Spock
        'testcontainers' | 'micronaut.starter.feature.testcontainers.enabled' | TestContainers
        'picocli-kotest' | 'micronaut.starter.feature.picocli.kotest.enabled' | PicocliKoTest
        'picocli-spock' | 'micronaut.starter.feature.picocli.spock.enabled' | PicocliSpock
        'picocli-junit' | 'micronaut.starter.feature.picocli.junit.enabled' | PicocliJunit
        'gradle' | 'micronaut.starter.feature.gradle.enabled' | Gradle
        'maven' | 'micronaut.starter.feature.maven.enabled' | Maven
        'config4k' | 'micronaut.starter.feature.config4k.enabled' | Config4k
        'properties' | 'micronaut.starter.feature.properties.enabled' | Properties
        'readme' | 'micronaut.starter.feature.readme.enabled' | Readme
        'yaml' | 'micronaut.starter.feature.yaml.enabled' | Yaml
        'toml' | 'micronaut.starter.feature.toml.enabled' | Toml

    }
}
