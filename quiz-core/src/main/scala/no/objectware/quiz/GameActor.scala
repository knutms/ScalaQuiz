package no.objectware.quiz

import actors.Actor
import messages._

import no.objectware.quiz.{Result => ResultObject}
import no.objectware.quiz.messages.{Result => ResultMessage}
import scala.actors.Actor._
import scala.actors.remote.RemoteActor._
import scala.actors._


class GameActor extends Actor {

  start()
  private var players : List[Player] = Nil
  private var playerChoices : Map[Player with Socket, Answer] = Map()
  private var gameStarted = -1L
  private var currentCorrectChoiceId : Int = -1
  private var currentQuestion : Question = null
  def act = {
    loop {
      receive {
        case Start(allPlayers) => {
          this.players = allPlayers
          gameStarted = System.currentTimeMillis()
        }
        
        case QuizAnswer(q, choiceId) => {
          currentQuestion = q
          currentCorrectChoiceId = choiceId
        }
        
        case (player: Player with Socket, Answer(dummyPlayer, id, choice, time)) => {
          println("GameActor: got answer from " + player)
          playerChoices += player -> Answer(dummyPlayer, id, choice, time)
          if (players.size == playerChoices.size) {
            println("GameActor: sending result to all players")
            val playersWithResult = players.map(p => new Player(p.name) with ResultObject {
              def timeUsedInMilliseconds = time
              def answer = choice
              def rank = 1000
            })
            
            sendToAllPlayers(ResultMessage(currentQuestion.id, currentCorrectChoiceId, playersWithResult))
          }
        }
      }
    }
  }
  
  def sendToAllPlayers(result : ResultMessage) = {
    playerChoices.keys.foreach(_.socket ! result)
  }
  
}


case class Start(val players : List[Player])
