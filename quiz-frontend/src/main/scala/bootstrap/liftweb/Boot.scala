package bootstrap.liftweb

import _root_.net.liftweb.http._
import _root_.net.liftweb.sitemap.{Loc, SiteMap, Menu}
import _root_.net.liftweb.sitemap.Loc._
import net.liftweb.util.Full

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
    // where to search snippet
    LiftRules.addToPackages("no.objectware.quiz")

    // Build SiteMap
    val entries = Menu(Loc("Join", List("index"), "Join")) ::
            Menu(Loc("Leave", List("leave"), "Leave")) ::
            Menu(Loc("Game", List("game"), "Game")) :: Nil
    LiftRules.setSiteMap(SiteMap(entries: _*))
  }
}

