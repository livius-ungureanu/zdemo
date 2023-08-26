package zt.pipelines
import zt.config.ZMyConfig
import zio.stream.{ZPipeline, ZSink, ZStream}
import zio.stream.ZStream.apply
import zt.model.CustomerCsv
import zt.model.CustomerCsv.parseCsvLine
import zio.*
import zt.MainApp.customerFile

import java.io.File
import java.nio.file.Files
import zt.bl.BlImpl.*
import zt.model.SummaryResult
import zt.metrics.MyMetrics
import zt.pipelines.CustomersSink.processedCustomersSink

object Pipeline1 {

  def processCustomersStream1:  ZStream[ZMyConfig, Throwable, Boolean] = {
    ZStream
      .fromZIO(ZIO.service[ZMyConfig])
      .flatMap { zMyConfig =>
        // Process a stream of lines
        ZStream
          .fromJavaStream(Files.lines(new File(customerFile).toPath))
          .filterNot(_.isEmpty)
          .map { row =>
            parseCsvLine(row)
          }
          .mapZIOPar(zMyConfig.parallelizationLevel) { customerCsv =>
            processCustomer(customerCsv)
          }
      }
  }


  def runPipeline1: ZIO[ZMyConfig, Throwable, SummaryResult] = {
    processCustomersStream1
      .run(processedCustomersSink)
  }

}
