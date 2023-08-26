package zt.metrics
import zio.metrics.Metric

object MyMetrics {
  val processCustomerOkCounter =
    Metric
      .counterInt("process_customer_ok_total")
      .fromConst(1)

  val processCustomerOkGauge =
    Metric
      .gauge("process_customer_ok_gauge")

  val processCustomerNokCounter =
    Metric
      .counterInt("process_customer_nok_total")
      .fromConst(1)

  val processCustomerNokGauge =
    Metric
      .gauge("process_customer_nok_gauge")
}
