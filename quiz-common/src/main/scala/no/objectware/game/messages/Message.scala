package no.objectware.game.messages

sealed trait Message

case class JoinMessage(player: Player) extends Message

case class WelcomeMessage(players: List[Player]) extends Message

case class LeaveMessage() extends Message

case class GoodbyeMessage(message: String) extends Message

case class QuestionMessage(id: Int, question: String, choices: Map[Int, String]) extends Message

case class AnswerMessage(id: Int, choice: Int, timeUsedInMilliseconds: Long) extends Message

case class ResultMessage(id: Int, correctAnswer: Int, result: List[Player with Result]) extends Message