package com.hangman.dictionary

import cats.Id
import com.hangman.dictionary.interpreter.SimpleDictionaryService
import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class SimpleDictionaryServiceSpec extends AnyFlatSpec with Matchers with MockFactory {

  behavior of "SimpleDictionaryService"

  it should "return some non empty word" in {
    val dictionaryService = new SimpleDictionaryService[Id]

    dictionaryService.getWord.isEmpty shouldBe false
  }
}
