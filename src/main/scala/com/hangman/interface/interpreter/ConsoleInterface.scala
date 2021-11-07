package com.hangman.interface.interpreter

import cats.syntax.all._
import cats.effect.Sync
import com.hangman.interface.UserInterface

import scala.io.StdIn

final class ConsoleInterface[F[_]: Sync] extends UserInterface[F] {

  override def print(string: String): F[Unit] = println(string).pure[F]

  override def getLine: F[String] = Sync[F]
    .delay(StdIn.readLine())
    .recover { case _ => Char.MinValue.toString }

  override def getChar: F[Char] = Sync[F]
    .delay(StdIn.readChar())
    .recover { case _ => Char.MinValue }
}
