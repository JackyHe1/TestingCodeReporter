java "-javaagent:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=56371:/Applications/IntelliJ IDEA.app/Contents/bin" -Dfile.encoding=UTF-8 -classpath /Users/lyh/GoogleDrive/Courses/SoftwareTesting/TestingCodeReporter/out/production/TestingCodeReporter:/Users/lyh/GoogleDrive/Courses/SoftwareTesting/TestingCodeReporter/lib/bcel-6.4.1.jar GraphGenerator

cp src/Demo.java code_coverage/jpf-core/src/examples/.

cd code_coverage/jpf-core
./gradlew
cd src/examples/
javac Bootstrap.java Demo.java
cd ../../.

./bin/jpf +classpath=src/examples/. +listener=gov.nasa.jpf.CoverageListener Bootstrap

cd ../../.
