package com.backwards.card

import cats.data._
import cats.effect.Console.io._
import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import monocle.macros.syntax.lens._
import com.backwards.card.CardShow._
import com.backwards.card.Cards._
import com.backwards.card.regex._

object Game extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    val cards: List[Card] = Exploding +: Defuse +: Defuse +: List.fill(16)(Blank)
    val playerCards: List[Defuse.type] = List(Defuse)

    val game = new Game
    import game._

    putStrLn(welcome + usage + commands) *> play(shuffle(Cards(cards, playerCards))).flatMap { case (cards, selectedCard) =>
      putStrLn(s"Card: ${selectedCard.show}") *> putStrLn(s"Remaining cards: $cards") *> IO(ExitCode.Success)
    }
  }
}

class Game extends Synopsis {
  lazy val debug: Boolean = sys.props.get("debug").contains("true")

  lazy val prompt: StateT[IO, Cards, Unit] = StateT.liftF(putStrLn("draw or quit:"))

  lazy val parseCommand: String => Command = {
    case r"(?i)draw" | r"(?i)d" => Draw
    case r"(?i)quit" | r"(?i)q" => Quit
    case c => Unknown(c)
  }

  lazy val state: StateT[IO, Cards, Unit] = StateT.get[IO, Cards].flatMap { cards =>
    StateT.liftF[IO, Cards, Unit](if (debug) putStrLn(s"\nDEBUG: $cards\n") else IO.unit)
  }

  lazy val end: Option[Card] => StateT[IO, Cards, Option[Card]] =
    card => StateT[IO, Cards, Option[Card]] { cards =>
      (cards, card).pure[IO]
    }

  lazy val draw: StateT[IO, Cards, Option[Card]] =
    StateT[IO, Cards, Option[Card]] { cards =>
      (cards.lens(_.pack).modify(_.drop(1)), cards.pack.headOption).pure[IO]
    } flatMap {
      case Some(Blank) =>
        play

      case Some(card @ Exploding) =>
        StateT.get[IO, Cards].flatMap { cards =>
          if (cards.player.isEmpty) StateT.liftF[IO, Cards, Unit](putStrLn("Exploding card! You lose - Though the game is rigged.")) *> end(Option(card))
          else StateT.modify[IO, Cards](takePlayerDefuse andThen addExploding andThen shuffle) *> play
        }

      case Some(Defuse) =>
        StateT.modify[IO, Cards](givePlayerDefuse) *> play

      case None =>
        StateT.liftF[IO, Cards, Unit](putStrLn("Odd! We've run out of cards.")) *> end(None)
    }

  lazy val action: StateT[IO, Cards, Option[Card]] = StateT.liftF(readLn).map(parseCommand).flatMap {
    case Draw => draw
    case Quit => StateT.liftF[IO, Cards, Unit](putStrLn("Giving up, eh?")) *> end(None)
    case Unknown(c) => StateT.liftF[IO, Cards, Unit](putStrLn(s"""Command "$c" unknown\n$commands""")) *> play
  }

  lazy val play: StateT[IO, Cards, Option[Card]] =
    state *> prompt *> action

  def play(cards: Cards): IO[(Cards, Option[Card])] = play run cards
}