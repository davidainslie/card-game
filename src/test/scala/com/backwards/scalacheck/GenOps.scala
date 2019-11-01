package com.backwards.scalacheck

import cats.implicits._
import cats.kernel.Monoid
import org.scalacheck.Gen
import org.scalacheck.cats.implicits._

object GenOps {
  def combine[T: Monoid](ts: Gen[T]*): Gen[T] = {
    @scala.annotation.tailrec
    def go(xs: List[Gen[T]], acc: Gen[T]): Gen[T] = xs match {
      case Nil => acc
      case h :: rest => go(rest, (acc, h).mapN(_ |+| _))
    }

    go(ts.toList, Monoid[Gen[T]].empty)
  }
}