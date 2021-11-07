package com.hangman.core

import cats.Monad
import cats.syntax.all._
import com.hangman.core.Hangman.{GuessResult, State}
import com.hangman.dictionary.DictionaryService
import com.hangman.interface.UserInterface

object Hangman {

  final case class State(
    guesses: Set[Char],
    word: String
  ) {
    val maxFailures: Int = (word.toSet.size * 1.5).toInt
    def failures: Int = (guesses -- word.toSet).size
    def playerLost: Boolean = failures >= maxFailures
    def playerWon: Boolean = (word.toSet -- guesses).isEmpty
    def addChar(char: Char): State = copy(guesses = guesses + char)
  }

  sealed trait GuessResult

  object GuessResult {
    case object Won extends GuessResult
    case object Lost extends GuessResult
    case object Correct extends GuessResult
    case object Incorrect extends GuessResult
    case object Unchanged extends GuessResult
  }
}

class Hangman[F[_]: Monad](
  ui: UserInterface[F],
  dictionaryService: DictionaryService[F])
{

  def getChoice: F[Char] = ui.getChar.map(_.toLower)

  def renderState(state: State): F[Unit] = {
    val maskedWord = state.word.map(c => if (state.guesses.contains(c)) c else '*')
    ui.print(s"Mistakes: ${state.failures} of ${state.maxFailures}")
    ui.print(s"The word: $maskedWord")
  }

  def analyzeNewInput(
    oldState: State,
    newState: State,
    char: Char
  ): GuessResult = {
    if (oldState.guesses.contains(char)) GuessResult.Unchanged
    else if (newState.playerWon) GuessResult.Won
    else if (newState.playerLost) GuessResult.Lost
    else if (oldState.word.contains(char)) GuessResult.Correct
    else GuessResult.Incorrect
  }

  def gameLoop(oldState: State): F[Unit] = for {
    _ <- ui.print(s"Guess a letter:")
    c <- getChoice
    currentState = oldState.copy().addChar(c)
    guessResult = analyzeNewInput(oldState, currentState, c)
    _ <- renderState(currentState)
    _ <- guessResult match {
      case GuessResult.Won => ui.print(s"You won!")
      case GuessResult.Lost => ui.print(s"You lost!")
      case _ => gameLoop(currentState)
    }
  } yield ()

  def startGame(): F[Unit] = for {
    _ <- ui.print("\n -- New game! --")
    word <- dictionaryService.getWord
    _ <- gameLoop(Hangman.State(Set(), word))
  } yield ()
}