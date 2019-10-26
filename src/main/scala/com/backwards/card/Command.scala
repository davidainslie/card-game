package com.backwards.card

sealed trait Command

case object Draw extends Command

case object Quit extends Command

final case class Unknown(requestedCommand: String) extends Command