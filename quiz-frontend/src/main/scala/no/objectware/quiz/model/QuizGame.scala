package no.objectware.quiz.model


import messages.Question


class QuizGame {
  private var _player: Option[Player] = None
  private var _question: Option[Question] = None

  def join(name: String): Boolean = {
    if (_player.isEmpty && !name.trim.isEmpty) {
      // TODO: Handle join on server
      _player = Some(Player(name))
      return true
    }
    false
  }

  def hasJoined: Boolean = {
    _player.isDefined
  }

  def leave = {
    _player = None
  }

  def player: Option[Player] = _player

  def question: Option[Question] = _question

}