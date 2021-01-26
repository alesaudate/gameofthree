#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
java -jar $DIR/../build/libs/gameofthree-0.0.1-SNAPSHOT.jar --player.name=player2 --player.destination=player1 --server.port=8081
