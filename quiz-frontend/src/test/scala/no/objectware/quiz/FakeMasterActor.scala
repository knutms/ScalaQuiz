package no.objectware.quiz


import actors.Actor
import messages._
import no.objectware.quiz.{Result => ResultObject}
import scala.actors.Actor._
import scala.actors.remote.RemoteActor._

class FakeMasterActor extends Actor {
  start()

  def act = {
    alive(9000)
    register('master, self)
    loop {
      receive {
        case Join(player) => reply(Welcome(player :: Nil))
        case Leave => reply(Goodbye("Good bye! See you next time."))
        case Answer(_, id, choise, timeUsed) => reply(Result(id, id, FakeMasterActor.createPlayerWithResult("You", id, timeUsed, 1) :: Nil))
      }
    }
  }
}

object FakeMasterActor {
  def createPlayerWithResult(name: String, answer: Int, timeUsedInMilliseconds: Long, rank: Int): Player with Result = {
    new Player(name) with ResultObject {
      def answer = answer

      def timeUsedInMilliseconds = timeUsedInMilliseconds

      def rank = rank
    }
  }
}

object FakeMasterBootStrapper {
  def main(args: Array[String]) {
    new FakeMasterActor
  }
}
