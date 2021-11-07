package com.hangman

import cats.effect.{ExitCode, IO, IOApp}
import com.hangman.core.Hangman
import com.hangman.dictionary.DictionaryService
import com.hangman.dictionary.interpreter.SimpleDictionaryService
import com.hangman.interface.UserInterface
import com.hangman.interface.interpreter.ConsoleInterface


object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {

    val dictionaryService: DictionaryService[IO] = new SimpleDictionaryService[IO]
    val ui: UserInterface[IO] = new ConsoleInterface[IO]
    val hangman = new Hangman[IO](ui, dictionaryService)

    def repeat: IO[Unit] = for {
      _ <- hangman.startGame()
      _ <- repeat
    } yield ()

    for {
      _ <- repeat
    } yield ExitCode.Success
  }
}
