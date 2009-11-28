package no.objectware.quiz

import no.objectware.quiz.messages._
import scala.actors._
import scala.actors.Actor._

object QuizApp extends Application {

  
  println("Starting Quiz server")
  
  val quizActor = new QuizRepositoryActor
  val main = new MainActor(quizActor)
  
  
  def playerMock = actor {
    loop {
      receive {
        case 'start => main ! Join(Player("Per"))
        case x: Welcome => println("I'm welcomed!")
        case q: Question => println("I got a question: " + q + " hmm.. not sure")
        case x => println("Got unknwn: " + x)
      }
    }
  }
  
  playerMock ! 'start
  
}

trait Socket {
  var socket : OutputChannel[Any] = null
  
  override def toString = super.toString() + " with socket: " + socket
}


class MainActor(val quizActor : QuizRepositoryActor) extends Actor{
  
  private var players : List[(Player with Socket)] = Nil
  private var currentQuestion = 0
  private var unansweredPlayers : List[Player] = Nil
  
  start
  
  def act = {
	  loop {
	    receive {
	      case Join(player) => {
	        println("joining " + player)
	        players = (new Player(player.name) with Socket {socket = sender}) :: players
	        reply(Welcome(players))
	        if (currentQuestion == 0) generateQuestions
	      }
	      case q: Question => {
	        println("Distributing question")
	        players.foreach(_.socket ! q)
          }
	      case Leave => reply(Goodbye("Good bye! See you next time."))
	      case Answer(id, choise, timeUsed) => {
	        reply(Result(id, id, Nil))
          }
	      case _ => println("not found..")
	    }
	  }
  }
  def generateQuestions = {
	unansweredPlayers = players
	quizActor ! 'generateQuestion
  }
}
class QuizRepositoryActor extends Actor{
  start
  
  def act = {
    loop {
      receive {
        case 'generateQuestion => sender ! Question(1, "i'm a question", null)
      }
    }
  }
  
}
