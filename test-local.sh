#!/bin/bash
EXIT_STATUS=0

./gradlew check --refresh-dependencies --continue -x test-aws:test -x test-buildtool:test -x test-cli:test -x test-cloud:test -x test-core:test -x test-features:test || EXIT_STATUS=$? 

if [ $EXIT_STATUS -ne 0 ]; then  
  exit $EXIT_STATUS
fi

./gradlew test-aws:test || EXIT_STATUS=$? 

if [ $EXIT_STATUS -ne 0 ]; then  
  exit $EXIT_STATUS
fi

./gradlew test-buildtool:test || EXIT_STATUS=$? 

if [ $EXIT_STATUS -ne 0 ]; then  
  exit $EXIT_STATUS
fi

./gradlew test-cli:test || EXIT_STATUS=$? 

if [ $EXIT_STATUS -ne 0 ]; then  
  exit $EXIT_STATUS
fi

./gradlew test-cloud:test || EXIT_STATUS=$? 

if [ $EXIT_STATUS -ne 0 ]; then  
  exit $EXIT_STATUS
fi

./gradlew test-core:test || EXIT_STATUS=$? 

if [ $EXIT_STATUS -ne 0 ]; then  
  exit $EXIT_STATUS
fi

./gradlew test-features:test  || EXIT_STATUS=$? 

exit $EXIT_STATUS


