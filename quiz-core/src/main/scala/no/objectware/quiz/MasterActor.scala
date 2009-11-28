package no.objectware.quiz

import actors.Actor
import messages._
import no.objectware.quiz.{Result => ResultObject}
import scala.actors.Actor._
import scala.actors.remote.RemoteActor._
import scala.actors._

class MasterActor(val quizActor : QuizRepositoryActor) extends Actor {

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
	          generateQuestions()
            }
	        reply(Welcome(players))
         }
        case Leave => reply(Goodbye("Good bye! See you next time."))
        case q: Question => {
	        println("Distributing question")
	        players.foreach(_.socket ! q)
          }
        case Answer(id, choice, timeUsed) => reply(Result(id, id, MasterActor.createPlayerWithResult("You", id, timeUsed, 1) :: Nil))
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

object MasterBootStrapper {
  def main(args: Array[String]) {
    val quizRepositoryActor = new QuizRepositoryActor();
    new MasterActor(quizRepositoryActor)
  }
}
