package com.backwards.card

import cats.Show

object CardShow {
  implicit val showCard: Show[Card] = {
    case Blank => "Blank"
    case Exploding => "Exploding"
  }

  implicit val showOptionCard: Show[Option[Card]] = {
    case Some(c: Card) => showCard.show(c)
    case _ => "None"
  }
}