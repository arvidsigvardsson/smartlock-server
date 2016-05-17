#!/bin/bash
javac -cp jackson-annotations-2.7.3.jar:jackson-core-2.7.3.jar:jackson-databind-2.7.3.jar *.java
java -cp jackson-annotations-2.7.3.jar:jackson-core-2.7.3.jar:jackson-databind-2.7.3.jar:. RootServer