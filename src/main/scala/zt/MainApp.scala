package zt

import zio.*
import zio.http.*
import zio.http.netty.NettyConfig
import zio.http.netty.client.NettyClientDriver
import zio.metrics.connectors.{MetricsConfig, prometheus}
import zio.metrics.connectors.prometheus.PrometheusPublisher
import zio.config.typesafe.TypesafeConfigProvider
import zio.logging.{LogFormat, consoleLogger}
import zio.logging.LogFormat.Pattern.Color
import zio.logging.backend.SLF4J

import java.io.File
import zt.config.ZMyConfig
import zt.routes.PrometheusPublisherRoutes
import zt.routes.MyRoutes

object MainApp extends ZIOAppDefault {
  val SERVER_PORT = 8070
  val customerFile = "customers.csv"

  private val configProvider: ConfigProvider =
    TypesafeConfigProvider.fromResourcePath(false)

  // inject custom logger
  override val bootstrap =
    Runtime.removeDefaultLoggers >>>
      Runtime.setConfigProvider(configProvider) >>> consoleLogger()

  // set-up the configuration layer
  private val zMyConfigLayer =
    ZLayer.fromZIO(ZIO.config[ZMyConfig](ZMyConfig.configDescriptor))

  val appRoutes = MyRoutes() ++ PrometheusPublisherRoutes()

  private val metricsConfig = ZLayer.succeed(MetricsConfig(5.seconds))

  private val program = for {
    // _ <- MigMetrics.initMetrics

    _ <- ZIO.logInfo(
      s"Started the TZ migration tool on port $SERVER_PORT",
    )

    _ <- Server
      .serve(appRoutes)
      .provide(
        Server.defaultWithPort(SERVER_PORT),
        prometheus.publisherLayer,
        prometheus.prometheusLayer,
        metricsConfig,
        zMyConfigLayer
      )
  } yield ()

  val run = program
}
