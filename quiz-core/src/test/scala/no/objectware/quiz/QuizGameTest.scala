package no.objectware.quiz

import org.junit.runners.JUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert._

import no.objectware.quiz.messages._
import scala.actors._
import scala.actors.Actor._

import java.util.concurrent.CyclicBarrier

@RunWith(classOf[JUnit4])
class QuizGameTest {

  @Test
  def addPlayerToGame() {
    val game = new QuizGame
    
    assertEquals(0, 0);
  }

  @Test
  def startGameAndJoin() {
      val quizActor = new QuizRepositoryActor
      val main = new MasterActor(quizActor)
      
      val twoThreadBarrier = new CyclicBarrier(2)
      
	  def playerMock = actor {
	    loop {
	      receive {
	        case 'start => main ! Join(Player("Per"))
	        case x: Welcome => println("I'm welcomed!")
	        case q: Question => twoThreadBarrier.await() ; println("I got a question: " + q + " hmm.. not sure")
	        case x => println("Got unknwn: " + x)
	      }
	    }
	  }
  
      playerMock ! 'start
    
      twoThreadBarrier.await()
      
      assertEquals(1, main.players.size)
  }
  
}
