package no.objectware.game

trait Result {
  
  def answer: Int

  def timeUsedInMilliseconds: Long

  def rank: Int

}