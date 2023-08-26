package zt.pipelines
import zio.stream.{ZPipeline, ZSink, ZStream}
import zt.model.SummaryResult
import zio._
import zt.metrics.MyMetrics
object CustomersSink {
  val processedCustomersSink = ZSink.foldLeftZIO(SummaryResult(0, 0)) {
    (summary: SummaryResult, processingResult: Boolean) =>
      if (processingResult) {
        ZIO.succeed {
          val newSummary = summary.copy(ok = summary.ok + 1)
          newSummary
        } @@
          MyMetrics.processCustomerOkGauge.contramap(_.ok) @@
          MyMetrics.processCustomerOkCounter
      } else {
        ZIO.succeed(summary.copy(nok = summary.nok + 1)) @@
          MyMetrics.processCustomerNokGauge.contramap(_.nok) @@
          MyMetrics.processCustomerNokCounter
      }
  }
}
