package no.objectware.quiz

trait Result {

  def answer: Int

  def timeUsedInMilliseconds: Long

  def rank: Int
  
  override def toString() = super.toString + " with answer: " + answer

}