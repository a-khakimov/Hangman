
lazy val root = (project in file("."))
  .settings(Coverage.Settings)
  .settings(
    inThisBuild(List(
      organization := "com.hangman",
      scalaVersion := "2.13.4",
      semanticdbEnabled := true,
      semanticdbVersion := scalafixSemanticdb.revision,
      scalacOptions ++= Seq(
        "-Ywarn-unused",
        "-deprecation"
      )
    )),
    name := "Hangman"
  )

libraryDependencies += "org.scalamock" %% "scalamock" % "5.1.0" % Test
libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.0" % Test
libraryDependencies += "org.typelevel" %% "cats-effect" % "2.5.1"
