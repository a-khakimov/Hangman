package com.hangman.dictionary.interpreter

import cats.Monad
import cats.syntax.all._
import com.hangman.dictionary.DictionaryService
import com.hangman.dictionary.interpreter.SimpleDictionaryService.dictionary

import scala.util.Random

class SimpleDictionaryService[F[_]: Monad] extends DictionaryService[F] {

  override def getWord: F[String] = for {
    randomIndex <- Random.nextInt(dictionary.length).pure[F]
    word <- dictionary(randomIndex).pure[F]
  } yield word
}

private[dictionary] object SimpleDictionaryService {

  val dictionary = List(
    "hello",
    "my",
    "little",
    "pony"
  )
}

