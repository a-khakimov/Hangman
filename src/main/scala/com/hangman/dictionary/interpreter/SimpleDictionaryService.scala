package com.hangman.dictionary.interpreter

import cats.Monad
import com.hangman.dictionary.DictionaryService

class SimpleDictionaryService[F[_]: Monad] extends DictionaryService[F] {

  override def getWord: F[String] = ???
}

