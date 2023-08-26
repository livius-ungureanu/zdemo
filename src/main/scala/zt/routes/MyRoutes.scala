package zt.routes

import zio.ZIO
import zio.http.*
import zio.http.{Http, Request, Response}
import zt.config.ZMyConfig
import zio.http.Status.InternalServerError
import zt.pipelines.Pipeline1.*
import zt.model.SummaryResult
import zt.pipelines.Pipeline2.runPipeline2

object MyRoutes {

  private type RequirementsT =
      ZMyConfig

  private type PipelineResultZio = ZIO[RequirementsT, Throwable, Response]

  private def httpResponseFromPipelineResult(pipelineResult: PipelineResultZio) = {
    pipelineResult
      .tapError(e => ZIO.logError(s"Error encountered before replying: $e"))
      .catchAll(e =>
        ZIO.succeed(
          Response
            .text(s"App Internal error: ${e.toString}")
            .withStatus(InternalServerError),
        ),
      )
  }

  private def toResponse(resultZio: ZIO[ZMyConfig, Throwable, SummaryResult]) = {
    resultZio.map { summaryResult =>
      Response.text(s"Success: ${summaryResult.ok}  Failed: ${summaryResult.nok}")
    }
  }

  def apply(): Http[
    RequirementsT,
    Nothing,
    Request,
    Response,
  ] = {
    Http.collectZIO[Request] {
      case Method.GET -> Root / "proccess1"  =>
        httpResponseFromPipelineResult(toResponse(runPipeline1))
      case Method.GET -> Root / "proccess2" =>
        httpResponseFromPipelineResult(toResponse(runPipeline2))
    }
  }
}