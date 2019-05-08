# Quickstart
* Ensure that Java 8 JDK and Maven 3 are installed and on the classpath, and that JAVA\_HOME is defined
* On a Linux system, run the script `run.sh`. If the compilation and tests run successfully the application will start automatically. 
* This script can be run multiple times if multiple runs are needed. Even though this will run unit tests and generate a test results report again, repeated runs will take umder 10 seconds.
* Test results are in `target/site/surefire-report.html`
* Code coverage reports are in `target/site/jacoco/index.html`. 
  * Note that "Missed Branches" is high because JaCoCo has a bug in how it counts assertions even when `-ea` is set.

# Prerequisites
* Java 8 JDK is installed, download [here](https://www.oracle.com/technetwork/java/javase/downloads/index-jsp-138363.html])
* Maven 3 is installed, download [here](https://maven.apache.org/download.cgi)

# Building Application
* Simple build: `mvn compile` will create java class files under `target/classes` directory
* Building full package: `mvn package` will create the java classes, run unit tests, and generate a jar file located in `target` 

# Testing Application
Unit tests can be run with `mvn test`, and are run automatically when `run.sh` is run. Test results will be displayed and can also be found in `target/surefire-reports/`

An HTML-formatted test results report can be generated with `mvn surefire-report:report site -DgenerateReports=false`. The html file will be located in `target/site/surefire-report.html`

Code coverage reports are also generated by JaCoCo. These can be found in `target/site/jacoco/index.html`. Note that "Missed Branches" is high because JaCoCo has a bug in how it counts assertions even when `-ea` is set ([https://github.com/jacoco/jacoco/issues/324]).

# Running Application
* The recommended method is to build a full package then run `java -jar target/numbers_proj-0.0.1-SNAPSHOT.jar`
* A secondary method is to cd to `target/classes` and then run `java com.edgriebel.numbers.NumbersTester`

# Cleaning up generated files
Run `mvn clean`, which will remove `target` directory and all contents.

_Created by Ed Griebel, 2018-11-15 for Sonatype_
