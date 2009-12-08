package no.objectware.quiz.comet


import actors.Actor
import actors.Actor._
import net.liftweb.http._
import net.liftweb.util.{Box, Full, Empty, ActorPing}
import net.liftweb._
import http._
import js._
import JsCmds._

import util._
import Helpers._


class QuestionActor extends CometActor {
  var question: Int = 0;

 
  override def highPriority = {
    case QuestionNumber(n) =>
      question = n
      reRender(true)
  }

  // start the job
  override def localSetup() {
    ThingBuilder ! this
    super.localSetup()
  }

  // display the progress or a link to the result
  def render = <span>You are currently on question {question}.</span>

}

case class QuestionNumber(n: Int)

object ThingBuilder extends Actor {
  start()

  def act = {
    loop {
      react {
        case a: QuestionActor =>
          this ! (a, 1)

        case (a: QuestionActor, x: Int) if x >= 100 =>
          a ! QuestionNumber(100)

        case (a: QuestionActor, i: Int) =>
          a ! QuestionNumber(i + 1)
          ActorPing.schedule(this, (a, i + 1), 10 seconds)

        case _ =>

      }
    }
  }

}