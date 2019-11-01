package com.backwards.scalacheck

import org.scalacheck.Gen

object OrGen {
  implicit class OrGen[T](x: T) {
    def or(y: T): Gen[T] = Gen.oneOf(x, y)

    def or(zGen: Gen[T]): Gen[T] = zGen.flatMap(z => Gen.oneOf(x, z))
  }

  implicit class GenOps[T](gen: Gen[T]) {
    def or(z: T): Gen[T] = gen.flatMap(t => Gen.oneOf(t, z))

    def or(zGen: Gen[T]): Gen[T] = gen.flatMap(t => zGen.flatMap(z => Gen.oneOf(t, z)))
  }
}