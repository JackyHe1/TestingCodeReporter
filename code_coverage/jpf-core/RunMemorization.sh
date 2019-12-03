./gradlew
cd src/examples/
javac MemorizeTest1.java MemorizeTest2.java MemorizeTest3.java MemorizeTest4.java Add.java SampleVariable.java
cd ../../.

./bin/jpf +classpath=src/examples/. +listener=gov.nasa.jpf.MemoizationListener MemorizeTest1
./bin/jpf +classpath=src/examples/. +listener=gov.nasa.jpf.MemoizationListener MemorizeTest2
./bin/jpf +classpath=src/examples/. +listener=gov.nasa.jpf.MemoizationListener MemorizeTest3
./bin/jpf +classpath=src/examples/. +listener=gov.nasa.jpf.MemoizationListener MemorizeTest4
