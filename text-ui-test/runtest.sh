#!/usr/bin/env bash

set -e

# resolve script directory (CI-safe)
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
SRC_DIR="$SCRIPT_DIR/../src/main/java"
BIN_DIR="$SCRIPT_DIR/../bin"

# create bin directory if it doesn't exist
mkdir -p "$BIN_DIR"

# delete output from previous run
rm -f "$SCRIPT_DIR/ACTUAL.TXT"

# compile the code into the bin folder
if ! javac -Xlint:none -d "$BIN_DIR" $(find "$SRC_DIR" -name "*.java"); then
    echo "********** BUILD FAILURE **********"
    exit 1
fi

# run the program
java -cp "$BIN_DIR" her.m35.HERM35 < "$SCRIPT_DIR/input.txt" > "$SCRIPT_DIR/ACTUAL.TXT"

# convert to UNIX format
cp "$SCRIPT_DIR/EXPECTED.TXT" "$SCRIPT_DIR/EXPECTED-UNIX.TXT"
dos2unix "$SCRIPT_DIR/ACTUAL.TXT" "$SCRIPT_DIR/EXPECTED-UNIX.TXT"

# compare the output
if diff "$SCRIPT_DIR/ACTUAL.TXT" "$SCRIPT_DIR/EXPECTED-UNIX.TXT"; then
    echo "Test result: PASSED"
    exit 0
else
    echo "Test result: FAILED"
    exit 1
fi