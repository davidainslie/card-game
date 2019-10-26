package com.backwards.card

import scala.util.Random
import cats.data._
import cats.effect.Console.io._
import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import com.backwards.card.CardShow._

object Game extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    val cards: List[Card] = Random.shuffle(Exploding +: List.fill(16)(Blank))

    val game = new Game
    import game._

    putStrLn(welcome + usage + commands) *> play(cards).flatMap { case (cards, selectedCard) =>
      putStrLn(s"Card: ${selectedCard.show}") *> putStrLn(s"Remaining cards: $cards") *> IO(ExitCode.Success)
    }
  }
}

class Game extends Synopsis {
  lazy val debug: Boolean = sys.props.get("debug").contains("true")

  lazy val prompt: StateT[IO, List[Card], Unit] = StateT.liftF(putStrLn("draw or quit:"))

  lazy val parseCommand: String => Command = {
    case c if c.equalsIgnoreCase("draw") || c.equalsIgnoreCase("d") => Draw
    case c if c.equalsIgnoreCase("quit") || c.equalsIgnoreCase("q") => Quit
    case c => Unknown(c)
  }

  lazy val state: StateT[IO, List[Card], Unit] = StateT.get[IO, List[Card]].flatMap { cards =>
    StateT.liftF[IO, List[Card], Unit](if (debug) putStrLn(s"\nDEBUG: $cards\n") else IO.unit)
  }

  lazy val end: Option[Card] => StateT[IO, List[Card], Option[Card]] =
    card => StateT[IO, List[Card], Option[Card]] { cards =>
      (cards, card).pure[IO]
    }

  lazy val draw: StateT[IO, List[Card], Option[Card]] =
    StateT[IO, List[Card], Option[Card]] { cards =>
      (cards.drop(1), cards.headOption).pure[IO]
    } flatMap {
      case Some(Blank) => play
      case Some(card @ Exploding) => StateT.liftF[IO, List[Card], Unit](putStrLn("Exploding card! You lose.")) *> end(Option(card))
      case None => StateT.liftF[IO, List[Card], Unit](putStrLn("Odd! We've run out of cards.")) *> end(None)
    }

  lazy val action: StateT[IO, List[Card], Option[Card]] = StateT.liftF(readLn).map(parseCommand).flatMap {
    case Draw => draw
    case Quit => StateT.liftF[IO, List[Card], Unit](putStrLn("Giving up, eh?")) *> end(None)
    case Unknown(c) => StateT.liftF[IO, List[Card], Unit](putStrLn(s"""Command "$c" unknown\n$commands""")) *> play
  }

  lazy val play: StateT[IO, List[Card], Option[Card]] =
    state *> prompt *> action

  def play(cards: List[Card]): IO[(List[Card], Option[Card])] = play run cards
}