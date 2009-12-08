package no.objectware.quiz

import actors.Actor
import messages._
import no.objectware.quiz.{Result => ResultObject}
import scala.actors.Actor._
import scala.actors.remote.RemoteActor._
import scala.io.Source.fromInputStream
import java.net.URL
import java.util.concurrent.atomic._

class QuizRepositoryActor(val gameActor : GameActor) extends Actor{
  val Regexp = """.*<a.*>(.*)</a>.*""".r
  val ProverbRegexp = """.*<h2>(.*)</h2>.*""".r
  var questionId = new AtomicInteger(0)
  
  start
  
  def act = {
    loop {
      receive {
        case 'generateQuestion => {
          val q = generateQuestion()
          sender ! q
          gameActor ! QuizAnswer(q, 1)
        }
      }
    }
  }
  
  
  def generateQuestion() = {
    val facts : List[String] = 
      "Bad news travels fast" ::
      "Barking dogs seldom bite " ::
      "Meaning: People who are busy complaining rarely take more concrete hostile action" ::
      "Barking up the wrong tree" ::
      "Be careful before every step" ::
      "Before criticizing a man, walk a mile in his shoes " ::
      "One should not criticize a person without understanding their situation" ::
      "Beginning is half done " ::
      "Beggars can't be choosers " ::
      "Those who are in need of help can't afford to be too demanding" ::
      "Better to have it and not need it than to need it and not have it" ::
      "Better to remain silent and be thought a fool, than to open your mouth and remove all doubt " ::
      "Better late than never " ::
      "It's better to make an effort to keep an appointment than to give up altogether when you discover you will be late" ::
      "Better safe than sorry " ::
      "It is better to take precautions when it's possible that something can go amiss than to regret doing nothing later if something should indeed go wrong" ::
      "Better the devil you know (than the one you don't)" ::
      "Beware of the Bear when he tucks in his shirt" ::
      "Beware of the false prophets, who come to you in sheep's clothing, and inwardly are ravening wolves" ::
      "Beware of Greeks bearing gifts" :: 
      "Smi mens jarnet er varmt" :: Nil
      
      val randomNumber : Int = (Math.random * facts.length.asInstanceOf[Double]).asInstanceOf[Int]
      generateQuestionFromString(facts(randomNumber))
  }
  
  def findIndexToChange(words : Array[String]) : Int = {
    for (integer <- 1 to words.length) {
      def word = words(words.length - integer)
      if (word.length > 4) {
        return words.length - integer
      }
    }
    return 1
  }
  
  def generateQuestionFromString(fact: String) = {
    val words = fact.split(" ")
    
    val index = findIndexToChange(words)
    val wordToChange = words(index)
    
    val synonyms = getSynonyms(wordToChange)
    
    val question = words.map(word => if (word == wordToChange) "*" else word).foldLeft("")(_+" "+_)
    
    Question(questionId.getAndIncrement, question, getSynonyms(wordToChange))
  }
  
  def getSynonyms(word : String) = {
    val url= new URL("http://labs.google.com/sets?hl=en&q1=" + word)
    val res = fromInputStream(url.openStream).getLines.filter(_.contains("td")).map(_.trim)
    
    val words = for {line <- res }
      yield line match {
        case Regexp(param) => Some(param)
        case x => None
      }
     var map : Map[Int, String]= Map()
     var increment = new AtomicInteger(0)
     words.filter(!_.isEmpty).map(_.get).filter(p => !p.contains("/") && !p.contains(" ")).foreach(arg => map += (increment.getAndIncrement -> arg))
     map.filter(_._1 < 5)
  }

//  def getRandomProverb() {
//   val url= new URL("http://www.idefex.net/b3taproverbs")
//   val res = fromInputStream(url.openStream).getLines//.filter(_.contains("h2")).map(_.trim)
////   for {line <- res } 
////      yield line match {
////        case ProverbRegexp(param) => println("Found: " + line) ; return param.trim
////        case x => println("l: " + line) ; None
////      } 
//   val words = for {line <- res }
//      yield line match {
//        case ProverbRegexp(param) => Some(param)
//        case x => None
//      }
//   words.filter(!_.isEmpty)
////    throw new IllegalStateException("Cosuld not find proberb")
//  }
}



case class QuizAnswer(val question : Question, val answer : Int)
