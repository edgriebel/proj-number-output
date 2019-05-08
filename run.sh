#!/bin/sh

mvn package surefire-report:report site -DgenerateReports=false && java -jar target/numbers_proj-0.0.1-SNAPSHOT.jar
