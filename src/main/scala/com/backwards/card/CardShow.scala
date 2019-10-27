package com.backwards.card

import cats.Show

object CardShow {
  implicit val cardShow: Show[Card] = {
    case Blank => "Blank"
    case Exploding => "Exploding"
  }

  implicit val optionCardShow: Show[Option[Card]] = {
    case Some(c: Card) => cardShow show c
    case _ => "None"
  }
}