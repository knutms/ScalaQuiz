package no.objectware.quiz.messages

import no.objectware.quiz.{Player, Result => PlayerResult}

sealed trait Message

case class Join(player: Player) extends Message

case class Welcome(players: List[Player]) extends Message

case class Leave() extends Message

case class Goodbye(message: String) extends Message

case class Question(id: Int, question: String, choices: Map[Int, String]) extends Message

case class Answer(id: Int, choice: Int, timeUsedInMilliseconds: Long) extends Message

case class Result(id: Int, correctAnswer: Int, result: List[Player with PlayerResult]) extends Message