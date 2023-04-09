#!/bin/bash

# Absolute path to this script, e.g. /home/user/bin/foo.sh
SCRIPT=$(readlink -f "$0")
# Absolute path this script is in, thus /home/user/bin
SCRIPTPATH=$(dirname "$SCRIPT")
echo $SCRIPTPATH

REPO="$SCRIPTPATH/../"
CLASSPATH="$REPO/cgen-lib/build/libs/cgen-lib-0.1.2-SNAPSHOT.jar"
CLASSPATH="$CLASSPATH:$REPO/cgen-console/build/libs/cgen-console-0.1.2-SNAPSHOT.jar"

java -cp $CLASSPATH ce.entrypoints.BuildProjectKt $1



