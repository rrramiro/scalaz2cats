lazy val compilerOptions = Seq(
  "-deprecation", "-encoding", "UTF-8", "-feature", "-language:existentials",
  "-language:higherKinds", "-unchecked", "-Ywarn-dead-code", "-Ywarn-numeric-widen", "-Yrangepos",
  "-P:semanticdb:synthetics:on"
)

lazy val V = _root_.scalafix.sbt.BuildInfo

lazy val baseSettings = Seq(
  scalaVersion := V.scala212,
  addCompilerPlugin(scalafixSemanticdb),
  scalacOptions ++= compilerOptions,
  scalacOptions in (Compile, console) ~= {
    _.filterNot(Set("-Ywarn-unused-import", "-Ywarn-unused:imports"))
  },
  scalacOptions in (Test, console) ~= {
    _.filterNot(Set("-Ywarn-unused-import", "-Ywarn-unused:imports"))
  }
  //,coverageHighlighting := true
)

lazy val root = project
  .in(file("."))
  .settings(baseSettings)
  .settings(
    skip in publish := true
  )
  .aggregate(scalaz2cats, input, output, tests)

lazy val scalaz2cats = (project in file("rules"))
  .settings(baseSettings)
  .settings(
    libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % V.scalafixVersion,
    libraryDependencies += ("ch.epfl.scala" % "scalafix-reflect" % V.scalafixVersion)
      .cross(CrossVersion.full),
    scalacOptions += "-Ywarn-unused-import"
  )

lazy val input = project
  .settings(baseSettings)
  .settings(
    skip in publish := true,
    libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.29"
  )

lazy val output = project
  .disablePlugins(ScalafmtPlugin)
  .settings(
    skip in publish := true,
    libraryDependencies += "org.typelevel" %% "cats-effect" % "2.0.0",
    libraryDependencies += "org.typelevel" %% "cats-core" % "1.5.0",
    libraryDependencies += "org.typelevel" %% "mouse" % "0.25"
  )

lazy val tests = project
  .settings(baseSettings)
  .settings(
    crossScalaVersions := Seq(scalaVersion.value),
    skip in publish := true,
    libraryDependencies += ("ch.epfl.scala" % "scalafix-testkit" % V.scalafixVersion % Test)
      .cross(CrossVersion.full),
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0" % Test,
    compile.in(Compile) := compile.in(Compile).dependsOn(compile.in(input, Compile)).value,
    scalafixTestkitOutputSourceDirectories := sourceDirectories.in(output, Compile).value,
    scalafixTestkitInputSourceDirectories := sourceDirectories.in(input, Compile).value,
    scalafixTestkitInputClasspath := fullClasspath.in(input, Compile).value,
    scalafixTestkitInputScalacOptions := scalacOptions.in(input, Compile).value,
    scalafixTestkitInputScalaVersion := scalaVersion.in(input, Compile).value
  )
  .dependsOn(scalaz2cats)
  .enablePlugins(ScalafixTestkitPlugin)

Global / onChangedBuildSource := ReloadOnSourceChanges
