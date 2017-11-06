sbtPlugin := true

organization := "hackerdashery"

name := "sbt-babel"

description := "An SBT plugin to perform Babel transpilation."

scalaVersion := "2.12.3"

resolvers += Resolver.typesafeRepo("releases")

libraryDependencies ++= Seq(
  "com.typesafe" % "jstranspiler" % "1.0.1"
)

addSbtPlugin("com.typesafe.sbt" %% "sbt-js-engine" % "1.2.2")

version in ThisBuild := "2.0.0"

licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT"))
