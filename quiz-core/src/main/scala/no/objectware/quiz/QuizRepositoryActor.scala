package no.objectware.quiz

import actors.Actor
import messages._
import no.objectware.quiz.{Result => ResultObject}
import scala.actors.Actor._
import scala.actors.remote.RemoteActor._

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