# Game of Three

## Decisions taken

### Infrastructure

This game is entirely based on events, and it uses Apache Kafka as a message broker. It allows the players to be offline during the game and, as soon as they get back, it makes it possible for them to keep on playing as if they were never offline.
To start it, one must use the `docker-compose.yml` file that is placed in the `docker` folder or, if convenient, use the script `startInfra.bash` under the `scripts` folder.

#### Scripts

These scripts are aimed at easing the burden of trying to figure out what are the possible options to start the system.

They are the following:
- `buildJar.bash`: build the application locally, using `gradlew` as wrapper (which, in turn, will download Gradle if needed)
- `startInfra.bash`: will invoke Docker compose to bootstrap the infrastructure needed to run the system. As a Docker tool, will download Docker images if needed.
- `startPlayer1.bash` / `startPlayer2.bash`: will spawn instances of players named `player1` and `player2`, respectively. These are definitions that will reflect both on the infrastructure side (because they will attach to Kafka in topics that are in accordance with this name) as in the domain side (because it will output to the terminal reflecting it).

### General coding

#### Onion architecture

The architecture used here is an onion, similar to the one seen on [Jeffrey Palermo's blog](https://jeffreypalermo.com/2008/07/the-onion-architecture-part-1/). 
It is not using the same definitions, though: the package structure used here is:
- `interfaces.incoming`:
  Represents the data that comes to the system and, therefore, is the outer layer of the onion. As such, it may only access the layer that is immediately below it or itself, but not layers that are below the one that's immediately below.
- `domain`:
  Represents the domain of the game. Like the `interfaces.incoming` package, it also cannot access layers other than the one that is immediately below, and also cannot access any layer that is above it.
- `interfaces.outcoming`:
  Represents the data that is leaving the system. As it is the last layer, it cannot access any other layers than itself.

#### DDD

Usage of DDD is constrained to the `domain` package. Classes there try to communicate to each other like real-world objects would try to communicate. So in general, players talk to each other by telling them what the game data is, and it is possible for each of them to recreate the other one's data by applying the given modifications. 
There's a fundamental difference between a human playing and a computer playing. Often, the human will just try allowed modifiers to check if there's a fit. If not, he/she will just keep trying until figure out what is a valid modifier.
The computer player will not apply this behavior; it will rather try some math trick in order to figure out what is a feasible modifier.

#### Best practices

- Avoid using dependency injection through fields, but rather via constructor. This way, objects can ensure their own initial state without the need to rely on any framework (in this case, Spring)

### Code Quality

This project has been developed using [SonarLint](https://www.sonarlint.org/). Therefore, several changes to the code have been applied due to the plugin's recommendation. The most evident is turning test classes no longer public.

Also, this project uses Spring's formatter plugin. As such, before doing any checks it verifies if the code is following the plugin's standards. To apply these standards, just run `./gradlew format`.

### Tests

#### Unit tests
- Test data should be random where possible (to avoid using test data that leads to skewed tests)
- Also, the tests use as much as possible the [fixture test pattern](https://medium.com/flawless-app-stories/swift-tests-tips-tricks-fixture-object-pattern-5decefe6f10c)
- The architecture itself is unit-tested through the use of the [ArchUnit framework](https://www.archunit.org/)

#### Coverage tests

Jacoco is configured to prevent builds if coverage levels are below 100%. This can be configured in the `build.gradle` file.

#### Contract tests

Contract tests are defined by using [REST-assured](https://rest-assured.io/) and Spring for Apache Kafka.


