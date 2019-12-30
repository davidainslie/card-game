package com.backwards.card

import cats.data.StateT
import cats.effect.IO
import cats.implicits._
import org.scalacheck.Gen
import org.scalatest.MustMatchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import com.backwards.scalacheck.GenOps._
import com.backwards.scalacheck.OrGen._

class GameSpec extends AnyWordSpec with MustMatchers with ScalaCheckDrivenPropertyChecks {
  "Initial state of the game" should {
    "have no cards" in new Game {
      val noCards: Cards = Cards(Nil, Nil)
      val game: IO[(Cards, Unit)] = state run noCards

      game.unsafeRunSync mustEqual (noCards, ())
    }

    "have 1 Blank card" in new Game {
      val cards: Cards = Cards(List(Blank), Nil)
      val game: IO[(Cards, Unit)] = state run cards

      game.unsafeRunSync mustEqual (cards, ())
    }

    "have 2 Blank cards and 1 Exploding card" in new Game {
      val cards: Cards = Cards(List(Blank, Exploding, Blank), Nil)
      val game: IO[(Cards, Unit)] = state run cards

      game.unsafeRunSync mustEqual (cards, ())
    }
  }

  "End state of the game" should {
    "have a Blank card" in new Game {
      val cards: Cards = Cards(List(Blank), Nil)
      val endCard: Option[Blank.type] = Option(Blank)
      val endState: StateT[IO, Cards, Option[Card]] = end(endCard)
      val game: IO[(Cards, Option[Card])] = endState run cards

      game.unsafeRunSync mustEqual (cards, endCard)
    }

    "have an Exploding card" in new Game {
      val cards: Cards = Cards(List(Blank, Blank), Nil)
      val endCard: Option[Exploding.type] = Option(Exploding)
      val endState: StateT[IO, Cards, Option[Card]] = end(endCard)
      val game: IO[(Cards, Option[Card])] = endState run cards

      game.unsafeRunSync mustEqual (cards, endCard)
    }
  }

  "Game" should {
    "draw an Exploding card as the only available card" in new Game {
      val cards: Cards = Cards(List(Exploding), Nil)
      val game: IO[(Cards, Option[Card])] = draw run cards

      game.unsafeRunSync mustEqual (Cards(Nil, Nil), Option(Exploding))
    }

    "draw an Exploding card from deck of cards" in new Game {
      val cards: Cards = Cards(List(Exploding, Blank, Blank), Nil)
      val game: IO[(Cards, Option[Card])] = draw run cards

      game.unsafeRunSync mustEqual (Cards(cards.pack.drop(1), Nil), Option(Exploding))
    }

    "draw a Blank card as the only available card" in new Game {
      override lazy val play: StateT[IO, Cards, Option[Card]] = end(Option(Blank))

      val cards: Cards = Cards(List(Blank), Nil)
      val game: IO[(Cards, Option[Card])] = draw run cards

      game.unsafeRunSync mustEqual (Cards(Nil, Nil), Option(Blank))
    }

    "draw Blank cards until cards are exhausted" in new Game {
      override lazy val play: StateT[IO, Cards, Option[Card]] = draw

      val cards: Cards = Cards(List(Blank, Blank, Blank), Nil)
      val game: IO[(Cards, Option[Card])] = draw run cards

      game.unsafeRunSync mustEqual (Cards(Nil, Nil), None)
    }
  }

  "Game with Defuse" should {
    "draw an Exploding card and be defused by the only available Defuse" in new Game {
      override lazy val play: StateT[IO, Cards, Option[Card]] = end(None)

      val cards: Cards = Cards(List(Exploding), List(Defuse))
      val game: IO[(Cards, Option[Card])] = draw run cards

      game.unsafeRunSync mustEqual (Cards(List(Exploding), Nil), None)
    }

    "draw an Exploding card and be defused by one of the available Defuse" in new Game {
      override lazy val play: StateT[IO, Cards, Option[Card]] = end(None)

      val cards: Cards = Cards(List(Exploding), List(Defuse, Defuse))
      val game: IO[(Cards, Option[Card])] = draw run cards

      game.unsafeRunSync mustEqual (Cards(List(Exploding), List(Defuse)), None)
    }
  }

  "Playing the Game" should {
    "parse a 'draw' command" in new Game {
      val drawGen: Gen[String] = combine("d" or "D", "r" or "R", "a" or "A", "w" or "W")

      val commandGen: Gen[String] = "d" or "D" or drawGen

      forAll(commandGen) { command =>
        parseCommand(command) mustEqual Draw
      }
    }

    "parse a 'quit' command" in new Game {
      val quitGen: Gen[String] = combine("q" or "Q", "u" or "U", "i" or "I", "t" or "T")

      val commandGen: Gen[String] = "q" or "Q" or quitGen

      forAll(commandGen) { command =>
        parseCommand(command) mustEqual Quit
      }
    }

    "parse an 'unknown' command" in new Game {
      forAll(Gen.oneOf("x", " ", "blah")) { command =>
        parseCommand(command) mustEqual Unknown(command)
      }
    }
  }
}