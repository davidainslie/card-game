package com.backwards.card

import scala.util.Random
import monocle.macros.syntax.lens._

case class Cards(pack: List[Card], player: List[Defuse.type])

object Cards {
  lazy val shuffle: Cards => Cards =
    cards => cards.lens(_.pack).modify(Random.shuffle(_))

  lazy val givePlayerDefuse: Cards => Cards =
    _.lens(_.player).modify(_ :+ Defuse)

  lazy val takePlayerDefuse: Cards => Cards =
    _.lens(_.player).modify(_.drop(1))

  lazy val addExploding: Cards => Cards =
    _.lens(_.pack).modify(_ :+ Exploding)
}