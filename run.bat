@echo off
cd /d %~dp0
if not exist out mkdir out
javac -encoding UTF-8 -d out src\main\java\com\bankmanagement\*.java
java -cp out com.bankmanagement.Main
pause
