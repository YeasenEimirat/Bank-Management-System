#!/usr/bin/env bash
cd "$(dirname "$0")"
mkdir -p out
javac -encoding UTF-8 -d out src/main/java/com/bankmanagement/*.java
java -cp out com.bankmanagement.Main
