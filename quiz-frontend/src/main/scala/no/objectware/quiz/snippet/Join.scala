package no.objectware.quiz.snippet

import model.{QuizGame, QuizSession}
import net.liftweb.util.Empty
import net.liftweb.util.Helpers._
import net.liftweb.http.SHtml._
import net.liftweb.http.S._
import xml.{Utility, Text, NodeSeq}

class Join {
  def joinForm(xhtml: NodeSeq) = {

    val game: QuizGame = QuizSession.is

    if (game.hasJoined) {
      redirectTo("game")
    }

    def handleName(yourName: String) {
      if (game.join(yourName)) {
        redirectTo("game")
      }
    }

    bind("f", xhtml, ("name" -> text(game.player.getOrElse(Player("")).name, handleName _)))
  }

  def leave(xhtml: NodeSeq) = {
    QuizSession.is.leave
    redirectTo("index")
  }

  def checkJoined(xhtml: NodeSeq): NodeSeq = {
    if (!QuizSession.is.hasJoined) redirectTo("index")
    Text("")
  }

}