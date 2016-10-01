#!/bin/bash

echo "Compiling java files..."
javac -target 1.8 `find . -name "*.java"`

echo "Creating jar files..."
jar cfm release/puzzle.jar manifests/MANIFEST.mf\
        `find . -name "*.class"`\
        `find . -name "*.gif"`\
        `find . -name "*.jpg"`

echo "Creating jar source files..."
jar cfm release/puzzle-source.jar manifests/MANIFEST.mf\
         build.sh\
        `find . -name "*.java"`\
        `find . -name "*.gif"`\
        `find . -name "*.jpg"`

echo "Deleting class files..."
rm `find . -name "*.class"`

echo "Export to target path..."
sudo cp release/puzzle.jar /opt/
sudo cp puzzle.sh /opt/
sudo chmod +x /opt/puzzle.sh