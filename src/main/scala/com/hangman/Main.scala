package com.hangman

import cats.effect.{ExitCode, IO, IOApp}


object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {

    for {
      _ <- IO(())
    } yield ExitCode.Success
  }
}
