package com.hangman.core

import cats.Monad
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

  def getChoice: F[Char] = ???

  def renderState(state: State): F[Unit] = ???

  def analyzeNewInput(
    oldState: State,
    newState: State,
    char: Char
  ): GuessResult = ???

  def gameLoop(oldState: State): F[Unit] = ???

  def startGame(): F[Unit] = ???
}