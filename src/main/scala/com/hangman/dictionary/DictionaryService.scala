package com.hangman.dictionary

trait DictionaryService[F[_]] {
  def getWord: F[String]
}
