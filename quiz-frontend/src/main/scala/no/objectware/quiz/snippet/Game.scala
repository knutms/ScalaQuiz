package no.objectware.quiz.snippet


import messages.Question
import model.{QuizGame, QuizSession}
import xml.{NodeSeq, Text}
import net.liftweb.util.Helpers._
import net.liftweb.http.SHtml._

/**
 * Created by IntelliJ IDEA.
 * User: kjellmartinrud
 * Date: Nov 28, 2009
 * Time: 5:12:20 PM
 * To change this template use File | Settings | File Templates.
 */

class Game {
  def playerName(xhtml: NodeSeq): NodeSeq = {
    Text(Game.player.name)
  }

  def question(xhtml: NodeSeq): NodeSeq = {
    Text(Game.question.question)
  }

  def options(xhtml: NodeSeq): NodeSeq = {
    xhtml
  }

}

object Game {
  def player() = {
    QuizSession.is.player.getOrElse(Player(""))
  }

  def question() = {
    QuizSession.is.question.getOrElse(Question(0, "Hvor høy er Gallopiggen?", Map(0 -> "1024m", 1 -> "2183m", 2 -> "3231m")))
  }

}