package no.objectware.quiz.model


import net.liftweb.http.SessionVar


object QuizSession extends SessionVar[QuizGame](new QuizGame)