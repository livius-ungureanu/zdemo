scalaVersion := "3.2.2"
name := "ztrouble"
version := "0.4.6"
val zioVersion = "2.0.11"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % zioVersion,
  "dev.zio" %% "zio-test" % zioVersion,
  "dev.zio" %% "zio-test-sbt" % zioVersion,
  "dev.zio" %% "zio-http" % "3.0.0-RC2",
  "dev.zio" %% "zio-metrics-connectors" % "2.0.7",
  "dev.zio" %% "zio-json" % "0.5.0",
  "dev.zio" %% "zio-streams" % zioVersion,
  "dev.zio" %% "zio-config" % "4.0.0-RC16",
  "dev.zio" %% "zio-config-typesafe" % "4.0.0-RC16",
  "dev.zio" %% "zio-config-magnolia" % "4.0.0-RC16",
  "dev.zio" %% "zio-logging" % "2.1.13",
  "dev.zio" %% "zio-logging-slf4j" % "2.1.13",
  "org.slf4j" % "slf4j-simple" % "1.7.36"
)

enablePlugins(
  JavaAppPackaging,
  DockerPlugin
)
//dockerExposedPorts := Seq(8070)
//

// This is the official user from the TZ LAB vm
Docker / daemonUserUid := Some("1001")
Docker / daemonUser := "fworks"

dockerUsername := Some("tz_lab")
dockerRepository := Some("artifactory.openet.com:5000")
