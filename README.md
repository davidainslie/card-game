# Exploding Cards

Draw from a deck of **blank** cards, but watch out, one of them **explodes**... Dun, dun, dun!

## The Game

To play the game a prompt will ask to **draw** a card e.g. enter **d** or **D** or **draw** (or any combination of **DRAW**). Hopefully the chosen card will not explode and more cards can be drawn.

If boredom should set in, just enter **q** or **Q** or **quit** - You get the general idea.

## Installation Requirements

Either you can "play the game" via a [Docker](https://www.docker.com/) or [Scala](https://www.scala-lang.org/) installation. Apologies, the following installations are only for Mac.

Firstly you will need [homebrew](https://brew.sh/):

```bash
$ /usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
```

**Docker**

With Docker we will be able to just run the game.

```bash
Install Docker
$ brew cask install docker

Start Docker
$ open /Applications/Docker.app
```

**Scala**

Unlike Docker, along with installing the necessary infrastructure, we will also have to "get the application code".

- Install **JDK** (Java)
- Install **sbt** (Scala)

```bash
$ brew install homebrew/cask/java
$ brew cask info java
```

```bash
$ brew install sbt
$ brew info sbt
```

To "get the code" we need [git](https://git-scm.com/).

```bash
$ brew install git
```

And then **clone** this repository:

```bash
$ git clone https://github.com/davidainslie/card-game.git
```

## Docker

To play the first version of this game:

```bash
$ docker container run -it davidainslie/card-game:1.0.0
```

The application can also be run with some "debugging" to view game feedback:

```bash
$ docker container run -it -e JAVA_OPTS="-Ddebug=true" davidainslie/card-game:1.0.0
```

## Scala

**Test**

```bash
$ sbt test
```

**Run**

```bash
$ sbt run
```

Or if you would like to view the **state** of the game while running:

```bash
$ sbt '; set javaOptions += "-Ddebug=true"; run'
```

## Version 1

This application comes in two versions (flavours).

- There is one player and 17 cards.
- One of the cards is explosive and the rest are blank.
- All the cards are shuffled and arranged face down in a draw pile.
- The player draws cards one after the other.
- If the card is blank, it has no effect, and can be discarded.
- If the card is explosive, the player loses.

To view the code regarding this version, after a **git clone**:

```bash
$ git tag
1.0.0

$ git checkout 1.0.0
```

## Version 2

Game enhancement:

- Add three Defuse cards to the deck, making a total of 20.
- Game set up:
  - Give one defuse card to the player.
  - Put the remaining two defuse cards with the rest in the draw pile, shuffle and arrange face down.
- The player's turn consists of two steps:
  - Draw one card
  - There are four alternatives:
    - Blank card: the turn finishes.
    - Defuse card: add the defuse card to the player's hand and the turn finishes.
    - Explosive card, if the player has a defuse card:
      - Discard the defuse card onto the discard pile.
      - Return the explosive card to the draw pile.
      - Re-shuffle the draw pile.
    - Explosive card, if the player does not have a defuse card: The player loses.

To view the code regarding this version, after a **git clone**:

```bash
$ git tag
1.0.0
2.0.0

$ git checkout 2.0.0
```

