#!/bin/bash
workingDir=$(pwd)
cd $1
git log --pretty=format: --name-only --diff-filter=A | sort -u > $workingDir/Data/allFiles.txt
cd $workingDir/Data
javac *.java
java Parse $workingDir/Data
echo "All files listed."