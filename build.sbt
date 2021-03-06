name := "zio-scope-issue"

version := "0.1"

scalaVersion := "2.13.2"

val zioVersion            = "1.0.0-RC21-2"
val zioInteropCatsVersion = "2.1.4.0-RC17"
val http4sVersion         = "0.21.6"

fork in run := true

libraryDependencies ++= Seq(
  "dev.zio"      %% "zio"                       % zioVersion,
  "dev.zio"      %% "zio-interop-cats"          % zioInteropCatsVersion,
  "org.http4s"   %% "http4s-core"               % http4sVersion,
  "org.http4s"   %% "http4s-dsl"                % http4sVersion,
  "org.http4s"   %% "http4s-blaze-server"       % http4sVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)

enablePlugins(JavaAppPackaging)

dockerExposedPorts ++= Seq(8080)

version in Docker:="latest"