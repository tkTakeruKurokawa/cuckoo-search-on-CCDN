#!/bin/bash

javac ChangeParameter.java
javac ExtractStatistics.java
javac -classpath src:jep-2.3.0.jar:djep-1.0.0.jar `find src/peersim src/main -name "*.java"`

rm ./src/peersim/*/*.class
