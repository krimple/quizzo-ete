#!/bin/sh
pushd .
cd quizzo-data-access
mvn compile exec:java -Dexec.mainClass=org.springframework.data.examples.quizzo.util.QuizLoader -Dexec.args="../JavascriptQuiz.txt"
popd 
