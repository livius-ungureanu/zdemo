package zt.bl
import zio.*
import zt.model.CustomerCsv

object BlImpl {
  def processCustomer(customerCsv: CustomerCsv): ZIO[Any, Throwable, Boolean] = {
    ZIO.succeed(true)
  }
}
