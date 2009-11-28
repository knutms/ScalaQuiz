package no.objectware.quiz

import actors.Actor
import messages._
import no.objectware.quiz.{Result => ResultObject}
import scala.actors.Actor._
import scala.actors.remote.RemoteActor._

class MasterActor extends Actor {

  start()

  def act = {
    alive(9000)
    register('master, self)
    loop {
      receive {
        case Join(player) => reply(Welcome(player :: Nil))
        case Leave => reply(Goodbye("Good bye! See you next time."))
        case Answer(id, choice, timeUsed) => reply(Result(id, id, MasterActor.createPlayerWithResult("You", id, timeUsed, 1) :: Nil))
      }
    }
  }
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
    new MasterActor
  }
}
