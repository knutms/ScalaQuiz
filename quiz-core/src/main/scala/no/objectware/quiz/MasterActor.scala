package no.objectware.quiz

import actors.Actor
import messages._
import no.objectware.quiz.{Result => ResultObject}
import scala.actors.Actor._
import scala.actors.remote.RemoteActor._
import scala.actors._

class MasterActor(val quizActor : QuizRepositoryActor, val gameActor : GameActor) extends Actor {

  start()
  
  var players : List[(Player with Socket)] = Nil
  private var currentQuestion = 0
  private var unansweredPlayers : List[Player] = Nil
  
  
  def act = {
    alive(9000)
    register('master, self)
    loop {
      receive {
        case Join(player) => {
          println("Joining " + player)
	        players = (new Player(player.name) with Socket {socket = sender}) :: players
	        if (currentQuestion == 0) {
            gameActor ! Start(players)
	          generateQuestions()
            }
	        reply(Welcome(players))
         }
        case l: Leave => {
          
          reply(Goodbye("Good bye! See you next time."))
        }
        case q: Question => {
	        println("Distributing question")
	        players.foreach(_.socket ! q)
        }
        case Answer(player, id, choice, timeUsed) => {
          
//          val result = Result(id, id, MasterActor.createPlayerWithResult("You", id, timeUsed, 1) :: Nil)
//          players.foreach(_.socket ! result)
                                       
                                                                           
          val maybePlayerWithSocket = players.find(_ == player)
          println("sender: " + sender.getClass)
          maybePlayerWithSocket match {
            case Some(playerWithSocket) => {
              gameActor ! (playerWithSocket, Answer(playerWithSocket, id, choice, timeUsed))
              println("got answer from " + playerWithSocket)
            }
            case _ => println("Could not find player from socket")
          }
        }
      }
    }
  }
  
  def generateQuestions() = {
  	unansweredPlayers = players
  	quizActor ! 'generateQuestion
  }
}

trait Socket {
  var socket : OutputChannel[Any] = null
  override def toString = super.toString() + " with socket: " + socket
}

object MasterActor {
  def createPlayerWithResult(name: String, answer: Int, timeUsedInMilliseconds: Long, rank: Int): Player with Result = {
    new Player(name) with ResultObject {
      def answer = answer

      def timeUsedInMilliseconds = timeUsedInMilliseconds

      def rank = rank
    }
  }
}
