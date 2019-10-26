# Exploding Cards

Draw from a deck of **blank** cards, but watch out, on of them **explodes**... Dun, dun, dun!

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
# Install Docker
$ brew cask install docker

# Start Docker
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
