#!/bin/bash
javac -cp jackson-annotations-2.7.3.jar:jackson-core-2.7.3.jar:jackson-databind-2.7.3.jar *.java
nohup java -cp jackson-annotations-2.7.3.jar:jackson-core-2.7.3.jar:jackson-databind-2.7.3.jar:. RootServer < /dev/null > serverlogg.txt 2> servererr.txt &
