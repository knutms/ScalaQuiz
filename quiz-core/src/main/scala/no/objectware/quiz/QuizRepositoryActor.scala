package no.objectware.quiz

import actors.Actor
import messages._
import no.objectware.quiz.{Result => ResultObject}
import scala.actors.Actor._
import scala.actors.remote.RemoteActor._

class QuizRepositoryActor(val gameActor : GameActor) extends Actor{
  start
  
  def act = {
    loop {
      receive {
        case 'generateQuestion => {
          val q = Question(1, "i'm a question", Map((1 -> "(riktig) svar A"),(2, "svar B"),Tuple2(3, "svar C")))
          sender ! q
          gameActor ! QuizAnswer(q, 1)
        }
      }
    }
  }
}

case class QuizAnswer(val question : Question, val answer : Int)
