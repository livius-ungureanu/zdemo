package zt.routes

import zio.*
import zio.http.*
import zio.metrics.Metric
import zio.metrics.connectors.prometheus.PrometheusPublisher

object PrometheusPublisherRoutes {
  def apply(): Http[PrometheusPublisher, Nothing, Request, Response] = {
    Http.collectZIO[Request] { case Method.GET -> Root / "metrics" =>
      ZIO.serviceWithZIO[PrometheusPublisher](_.get.map(Response.text))
    }
  }
}
