package no.objectware.quiz

import org.junit.runners.JUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert._

import no.objectware.quiz.messages._
import no.objectware.quiz.messages.{Result => ResultMessage}
import scala.actors._
import scala.actors.Actor._

import java.util.concurrent._

@RunWith(classOf[JUnit4])
class QuizGameTest {

  @Test
  def addPlayerToGame() {
    val game = new QuizGame
    
    assertEquals(0, 0);
  }

  @Test
  def startGameAndJoinAndGetQuestionAndAnswerAndGetResult() {
    val gameActor = new GameActor
    val quizActor = new QuizRepositoryActor(gameActor)
    val main = new MasterActor(quizActor, gameActor)
    
    val twoThreadBarrier = new CyclicBarrier(2)
    var result :ResultMessage = null
	  def playerMock = actor {
      val player = Player("Per") 
        loop {
          receive {
            case 'start => main ! Join(player)
            case 'answer => main ! Answer(player, 1, 2, 0)
            case x: Welcome => println("I'm welcomed!")
            case q: Question => twoThreadBarrier.await(1, TimeUnit.SECONDS) ; println("I got a question: " + q + " hmm.. not sure")
            case r: ResultMessage => {
              println("Got result: " + r)
              result = r;
              twoThreadBarrier.await(1, TimeUnit.SECONDS)
            }
            case x => println("Got unknwn: " + x)
          }
        }
	  }
  
    playerMock ! 'start
    
    twoThreadBarrier.await(1, TimeUnit.SECONDS)
    
    assertEquals(1, main.players.size)
    
    playerMock ! 'answer
    
    twoThreadBarrier.await(1, TimeUnit.SECONDS)
    assertNotNull(result)
  }
  
}
