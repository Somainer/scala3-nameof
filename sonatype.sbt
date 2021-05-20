sonatypeProfileName := "com.somainer"

publishMavenStyle := true

licenses := Seq("The Unlicense" -> url("https://spdx.org/licenses/Unlicense.html"))

import xerial.sbt.Sonatype._
sonatypeProjectHosting := Some(
  GitHubHosting("Somainer", "scala3-nameof", "me@roselia.moe")
)

homepage := Some(url("https://github.com/Somainer/"))
scmInfo := Some(
  ScmInfo(
    url("https://github.com/Somainer/scala3-nameof"),
    "scm:git@github.com:Somainer/scala-nameof.git"
  )
)

sonatypeCredentialHost := "s01.oss.sonatype.org"

publishTo := sonatypePublishToBundle.value
