import scoverage.ScoverageKeys.{coverageEnabled, coverageExcludedFiles, coverageFailOnMinimum, coverageMinimumStmtTotal}

object Coverage {
  val Settings = Seq(
    coverageEnabled := true,
    coverageFailOnMinimum := true,
    coverageMinimumStmtTotal := 100,
    coverageExcludedFiles := Seq(
      ".*Main.*",
      ".*ConsoleInterface.*"
    ).mkString(";")
  )
}