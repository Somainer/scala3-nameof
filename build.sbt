val scala3Version = "3.0.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala3-nameof",
    version := "0.1.0",

    scalaVersion := scala3Version,

    libraryDependencies += "org.scalatest" % "scalatest_3" % "3.2.9"
  )
