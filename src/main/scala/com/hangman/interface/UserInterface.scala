package com.hangman.interface

trait UserInterface[F[_]] {
  def print(string: String): F[Unit]
  def getLine: F[String]
  def getChar: F[Char]
}
