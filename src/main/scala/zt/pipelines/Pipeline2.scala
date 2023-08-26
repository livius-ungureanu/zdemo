package zt.pipelines

import zio.*
import zio.stream.ZStream.apply
import zio.stream.{ZPipeline, ZSink, ZStream}
import zt.MainApp.customerFile
import zt.bl.BlImpl.*
import zt.config.ZMyConfig
import zt.metrics.MyMetrics
import zt.model.CustomerCsv.parseCsvLine
import zt.model.{CustomerCsv, SummaryResult}
import zt.pipelines.CustomersSink.processedCustomersSink

import java.io.File
import java.nio.file.Files

object Pipeline2 {

  def processCustomersStream2: ZStream[ZMyConfig, Throwable, Boolean] = {
    ZStream
      .fromZIO(ZIO.service[ZMyConfig])
      .flatMap { zMyConfig =>
        // Process a stream of lines
        ZStream
          .fromJavaStream(Files.lines(new File(customerFile).toPath))
          .filterNot(_.isEmpty)
          .grouped(zMyConfig.parallelizationLevel)
          .map { chunkOfRows =>
            chunkOfRows.map { row =>
              parseCsvLine(row)
            }
          }
          .mapZIO(chunkOfCustomers => processCustomersPar(chunkOfCustomers))
          .flatMap(ZStream.fromChunk)
      }
  }

  def processCustomersPar(chunkOfCustomers: Chunk[CustomerCsv]) = {
    ZIO.foreachPar(chunkOfCustomers) { customer =>
      processCustomer(customer)
    }
  }


  def runPipeline2: ZIO[ZMyConfig, Throwable, SummaryResult] = {
    processCustomersStream2
      .run(processedCustomersSink)
  }
}
