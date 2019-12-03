./gradlew
cd src/examples/
javac Demo.java Add.java SampleVariable.java
cd ../../.

./bin/jpf +classpath=src/examples/. +listener=gov.nasa.jpf.CoverageListener Demo
