package com.backwards.card

trait Synopsis {
  val welcome: String =
    """
      |8888888888                   888               888 d8b                         .d8888b.                       888
      |888                          888               888 Y8P                        d88P  Y88b                      888
      |888                          888               888                            888    888                      888
      |8888888    888  888 88888b.  888  .d88b.   .d88888 888 88888b.   .d88b.       888         8888b.  888d888 .d88888 .d8888b
      |888        `Y8bd8P' 888 "88b 888 d88""88b d88" 888 888 888 "88b d88P"88b      888            "88b 888P"  d88" 888 88K
      |888          X88K   888  888 888 888  888 888  888 888 888  888 888  888      888    888 .d888888 888    888  888 "Y8888b.
      |888        .d8""8b. 888 d88P 888 Y88..88P Y88b 888 888 888  888 Y88b 888      Y88b  d88P 888  888 888    Y88b 888      X88
      |8888888888 888  888 88888P"  888  "Y88P"   "Y88888 888 888  888  "Y88888       "Y8888P"  "Y888888 888     "Y88888  88888P'
      |                    888                                              888
      |                    888                                         Y8b d88P
      |                    888                                          "Y88P"
      |
      |Draw cards and hope they don't explode!
      |""".stripMargin

  val usage: String =
    """
      |Usage:
      |  Run from command line: sbt run
      |  Run from command line with debugging: sbt '; set javaOptions += "-Ddebug=true"; run'
      |""".stripMargin

  val commands: String =
    """
      |Game commands:
      |  D, d, draw = draw a card
      |  Q, q, quit = quit the game
      |""".stripMargin
}