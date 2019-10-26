package com.backwards.card

import cats.data.StateT
import cats.effect.IO
import cats.implicits._
import org.scalacheck.Gen
import org.scalatest.MustMatchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class GameSpec extends AnyWordSpec with MustMatchers with ScalaCheckDrivenPropertyChecks {
  "Initial state of the game" should {
    "have no cards" in new Game {
      val noCards: List[Card] = Nil
      val game: IO[(List[Card], Unit)] = state run noCards

      game.unsafeRunSync mustEqual (noCards, ())
    }

    "have 1 Blank card" in new Game {
      val cards: List[Card] = List(Blank)
      val game: IO[(List[Card], Unit)] = state run cards

      game.unsafeRunSync mustEqual (cards, ())
    }

    "have 2 Blank cards and 1 Exploding card" in new Game {
      val cards: List[Card] = List(Blank, Exploding, Blank)
      val game: IO[(List[Card], Unit)] = state run cards

      game.unsafeRunSync mustEqual (cards, ())
    }
  }

  "End state of the game" should {
    "have a Blank card" in new Game {
      val cards: List[Card] = List(Blank)
      val endCard = Option(Blank)
      val endState: StateT[IO, List[Card], Option[Card]] = end(endCard)
      val game: IO[(List[Card], Option[Card])] = endState run cards

      game.unsafeRunSync mustEqual (cards, endCard)
    }

    "have an Exploding card" in new Game {
      val cards: List[Card] = List(Blank, Blank)
      val endCard = Option(Exploding)
      val endState: StateT[IO, List[Card], Option[Card]] = end(endCard)
      val game: IO[(List[Card], Option[Card])] = endState run cards

      game.unsafeRunSync mustEqual (cards, endCard)
    }
  }

  "Game" should {
    "draw an Exploding card as the only available card" in new Game {
      val cards: List[Card] = List(Exploding)
      val game: IO[(List[Card], Option[Card])] = draw run cards

      game.unsafeRunSync mustEqual (Nil, Option(Exploding))
    }

    "draw an Exploding card from deck of cards" in new Game {
      val cards: List[Card] = List(Exploding, Blank, Blank)
      val game: IO[(List[Card], Option[Card])] = draw run cards

      game.unsafeRunSync mustEqual (cards.drop(1), Option(Exploding))
    }

    "draw a Blank card as the only available card" in new Game {
      override lazy val play: StateT[IO, List[Card], Option[Card]] = end(Option(Blank))

      val cards: List[Card] = List(Blank)
      val game: IO[(List[Card], Option[Card])] = draw run cards

      game.unsafeRunSync mustEqual (Nil, Option(Blank))
    }

    "draw Blank cards until cards are exhausted" in new Game {
      override lazy val play: StateT[IO, List[Card], Option[Card]] = draw

      val cards: List[Card] = List(Blank, Blank, Blank)
      val game: IO[(List[Card], Option[Card])] = draw run cards

      game.unsafeRunSync mustEqual (Nil, None)
    }
  }

  "Playing the Game" should {
    "parse a 'draw' command" in new Game {
      val drawGen: Gen[String] = for {
        d <- Gen.oneOf("d", "D")
        r <- Gen.oneOf("r", "R")
        a <- Gen.oneOf("a", "A")
        w <- Gen.oneOf("w", "W")
      } yield d + r + a + w

      val commandGen: Gen[String] =
        Gen.oneOf(Gen.oneOf("d", "D"), drawGen)

      forAll(commandGen) { command =>
        parseCommand(command) mustEqual Draw
      }
    }

    "parse a 'quit' command" in new Game {
      val quitGen: Gen[String] = for {
        q <- Gen.oneOf("q", "Q")
        u <- Gen.oneOf("u", "U")
        i <- Gen.oneOf("i", "I")
        t <- Gen.oneOf("t", "T")
      } yield q + u + i + t

      val commandGen: Gen[String] =
        Gen.oneOf(Gen.oneOf("q", "Q"), quitGen)

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