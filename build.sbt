val scala3Version = "3.0.0"

ThisBuild / organization := "com.somainer"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala3-nameof",

    scalaVersion := scala3Version,

    libraryDependencies += "org.scalatest" % "scalatest_3" % "3.2.9" % Test,

    publishTo := sonatypePublishToBundle.value
  )

import ReleaseTransformations._
releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseStepCommandAndRemaining("+publishSigned"),
  setNextVersion,
  commitNextVersion,
  releaseStepCommand("sonatypeRelease"),
  pushChanges
)
