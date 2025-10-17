#!/bin/bash

echo "Starting build test..."
echo "Current directory: $(pwd)"
echo "Java version: $(java -version 2>&1 | head -n 1)"
echo "Gradle wrapper exists: $(test -f gradlew && echo 'YES' || echo 'NO')"

echo "Running gradle clean..."
./gradlew clean --no-daemon --stacktrace

echo "Build test completed with exit code: $?"
