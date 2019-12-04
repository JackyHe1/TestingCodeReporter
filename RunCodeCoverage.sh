cd code_coverage/jpf-core
./gradlew
cd src/examples/
javac Bootstrap.java Demo.java
cd ../../.

./bin/jpf +classpath=src/examples/. +listener=gov.nasa.jpf.CoverageListener Bootstrap

cd ../../.
