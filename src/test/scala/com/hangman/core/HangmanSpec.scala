package com.hangman.core

import cats.Id
import com.hangman.core.Hangman.{GuessResult, State}
import com.hangman.dictionary.DictionaryService
import com.hangman.interface.UserInterface
import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers


class HangmanSpec extends AnyFlatSpec with Matchers with MockFactory {

  private trait mocks {
    val dictionaryService = mock[DictionaryService[Id]]
    val ui = mock[UserInterface[Id]]

    val hangman = new Hangman[Id](ui, dictionaryService)
  }

  behavior of "Hangman"

  it should "get player choice" in new mocks {
    (() => ui.getChar).expects().returning('c')

    hangman.getChoice shouldBe 'c'
  }

  it should "render state with masked word" in new mocks {
    (ui.print _).expects("Mistakes: 1 of 6").returning(())
    (ui.print _).expects("The word: *****").returning(())

    hangman.renderState(State(Set('c'), "hello"))
  }

  it should "analyse new input and return GuessResult.Unchanged" in new mocks {
    hangman
      .analyzeNewInput(
        oldState = State(Set('c'), "hello"),
        newState = State(Set('c'), "hello"),
        char = 'c'
      ) shouldBe GuessResult.Unchanged
  }

  it should "analyse new input and return GuessResult.Won" in new mocks {
    hangman
      .analyzeNewInput(
        oldState = State(Set('h', 'e', 'l', 'l'), "hello"),
        newState = State(Set('h', 'e', 'l', 'l', 'o'), "hello"),
        char = 'o'
      ) shouldBe GuessResult.Won
  }

  it should "analyse new input and return GuessResult.Lost" in new mocks {
    hangman
      .analyzeNewInput(
        oldState = State(Set('a', 'q', 'v', 'z', 'g', 'p'), "hello"),
        newState = State(Set('a', 'q', 'v', 'z', 'g', 'p', 'c'), "hello"),
        char = 'c'
      ) shouldBe GuessResult.Lost
  }

  it should "analyse new input and return GuessResult.Correct" in new mocks {
    hangman
      .analyzeNewInput(
        oldState = State(Set(), "hello"),
        newState = State(Set('h'), "hello"),
        char = 'h'
      ) shouldBe GuessResult.Correct
  }

  it should "analyse new input and return GuessResult.Incorrect" in new mocks {
    hangman
      .analyzeNewInput(
        oldState = State(Set(), "hello"),
        newState = State(Set('a'), "hello"),
        char = 'a'
      ) shouldBe GuessResult.Incorrect
  }

  it should "exit from game loop if player won" in new mocks {
    (ui.print _).expects("Guess a letter:").returning(())
    (ui.print _).expects("Mistakes: 0 of 6").returning(())
    (ui.print _).expects("The word: hello").returning(())
    (() => ui.getChar).expects().returning('o')
    (ui.print _).expects("You won!").returning(())

    hangman.gameLoop(State(Set('h', 'e', 'l', 'l'), "hello")) shouldBe ()
  }

  it should "exit from game loop if player lost" in new mocks {
    (ui.print _).expects("Guess a letter:").returning(())
    (ui.print _).expects("Mistakes: 6 of 6").returning(())
    (ui.print _).expects("The word: *****").returning(())
    (() => ui.getChar).expects().returning('v')
    (ui.print _).expects("You lost!").returning(())

    hangman.gameLoop(State(Set('q', 'w', 'r', 't', 'y'), "hello")) shouldBe ()
  }

  it should "dive into recursion" in new mocks {
    (ui.print _).expects(*).returning(()).anyNumberOfTimes()
    (() => ui.getChar).expects().returning('v')
    (() => ui.getChar).expects().returning('y')
    (() => ui.getChar).expects().returning('m')

    hangman.gameLoop(State(Set(), "ab")) shouldBe ()
  }

  it should "start game with running game loop" in new mocks {
    (() => dictionaryService.getWord).expects().returning("c")
    (ui.print _).expects("\n -- New game! --").returning(())
    (ui.print _).expects("Guess a letter:").returning(())
    (ui.print _).expects("Mistakes: 0 of 1").returning(())
    (ui.print _).expects("The word: c").returning(())
    (ui.print _).expects("You won!").returning(())
    (() => ui.getChar).expects().returning('c')

    hangman.startGame() shouldBe ()
  }
}
