package com.backwards.card

sealed trait Card

case object Blank extends Card

case object Exploding extends Card

case object Defuse extends Card