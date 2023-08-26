package zt.config

import zio.Config
import zio.config.magnolia.deriveConfig
import zio.Duration

case class ZMyConfig(
  parallelizationLevel: Int,
)

object ZMyConfig {
  val configDescriptor: Config[ZMyConfig] =
    deriveConfig[ZMyConfig].nested("ZMyConfig")
}
