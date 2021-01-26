#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
java -jar $DIR/../build/libs/gameofthree-0.0.1-SNAPSHOT.jar --player.name=player1 --player.destination=player2

