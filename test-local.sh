#!/bin/bash
EXIT_STATUS=0

./gradlew --refresh-dependencies || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

./gradlew checkstyleMain --parallel --rerun-tasks || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

./gradlew :starter-core:build --parallel --rerun-tasks || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

./gradlew check --continue -x test-aws:test -x test-buildtool:test -x test-cli:test -x test-cloud:test -x test-core:test -x test-features:test || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then  
  exit $EXIT_STATUS
fi

./gradlew test-aws:test --parallel --rerun-tasks || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then  
  exit $EXIT_STATUS
fi

./gradlew test-buildtool:test --parallel --rerun-tasks || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then  
  exit $EXIT_STATUS
fi

./gradlew test-cli:test --parallel --rerun-tasks || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then  
  exit $EXIT_STATUS
fi

./gradlew test-cloud:test --parallel --rerun-tasks || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then  
  exit $EXIT_STATUS
fi

./gradlew test-core:test --parallel --rerun-tasks || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then  
  exit $EXIT_STATUS
fi

./gradlew test-features:test --parallel --rerun-tasks || EXIT_STATUS=$?

exit $EXIT_STATUS
