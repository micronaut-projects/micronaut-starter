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

PREDICTIVE_TEST_SELECTION=false ./gradlew :starter-core:build --parallel --rerun-tasks || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

PREDICTIVE_TEST_SELECTION=false ./gradlew check --no-build-cache --continue -x test-aws:test -x test-buildtool:test -x test-cli:test -x test-cloud:test -x test-core:test -x test-features:test || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then  
  exit $EXIT_STATUS
fi

PREDICTIVE_TEST_SELECTION=false ./gradlew test-aws:test --no-build-cache --parallel --rerun-tasks || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then  
  exit $EXIT_STATUS
fi

PREDICTIVE_TEST_SELECTION=false ./gradlew test-buildtool:test --no-build-cache --parallel --rerun-tasks || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then  
  exit $EXIT_STATUS
fi

PREDICTIVE_TEST_SELECTION=false ./gradlew test-cli:test --no-build-cache --parallel --rerun-tasks || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then  
  exit $EXIT_STATUS
fi

PREDICTIVE_TEST_SELECTION=false ./gradlew test-cloud:test --no-build-cache --parallel --rerun-tasks || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then  
  exit $EXIT_STATUS
fi

PREDICTIVE_TEST_SELECTION=false ./gradlew test-core:test  --no-build-cache --parallel --rerun-tasks || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then  
  exit $EXIT_STATUS
fi

PREDICTIVE_TEST_SELECTION=false ./gradlew test-features:test  --no-build-cache --parallel --rerun-tasks || EXIT_STATUS=$?

exit $EXIT_STATUS
