import sbt._

object Dependencies {
  lazy val dependencies: Seq[ModuleID] =
    Seq(
      scalatest, scalacheck, cats, simulacrum, refined, monocle, shapeless
    ).flatten
  
  lazy val scalatest: Seq[ModuleID] = Seq(
    "org.scalatest" %% "scalatest" % "3.0.8" % "test"
  )

  lazy val scalacheck: Seq[ModuleID] = Seq(
    "org.scalacheck" %% "scalacheck" % "1.14.2" % "test"
  )

  lazy val cats: Seq[ModuleID] = {
    lazy val version = "2.0.0"

    Seq(
      "org.typelevel" %% "cats-core",
      "org.typelevel" %% "cats-effect"
    ).map(_ % version) ++ Seq(
      "org.typelevel" %% "cats-laws",
      "org.typelevel" %% "cats-testkit"
    ).map(_ % version % "test") ++ Seq(
      "dev.profunktor" %% "console4cats" % "0.8.0"
    )
  }
  
  lazy val simulacrum: Seq[ModuleID] = Seq(
    "com.github.mpilquist" %% "simulacrum" % "0.19.0"
  )
  
  lazy val refined: Seq[ModuleID] = {
    lazy val version = "0.9.10"

    Seq(
      "eu.timepit" %% "refined",
      "eu.timepit" %% "refined-pureconfig",
      "eu.timepit" %% "refined-cats"
    ).map(_ % version)
  }

  lazy val monocle: Seq[ModuleID] = {
    lazy val version = "2.0.0"

    Seq(
      "com.github.julien-truffaut" %% "monocle-law"
    ).map(_ % version % "test") ++ Seq(
      "com.github.julien-truffaut" %% "monocle-core",
      "com.github.julien-truffaut" %% "monocle-macro",
      "com.github.julien-truffaut" %% "monocle-generic"
    ).map(_ % version)
  }

  lazy val shapeless: Seq[ModuleID] = Seq(
    "com.chuusai" %% "shapeless" % "2.3.3"
  )
}