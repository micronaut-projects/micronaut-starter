#!/bin/bash
EXIT_STATUS=0

JAVA_VER=$(java -version 2>&1 | sed -n ';s/.* version "\(.*\)\.\(.*\)\..*".*/\1\2/p;')
if [[ "$JAVA_VER" != "170" ]]; then
  echo test-lambda works only with Java 17
  exit 1;
fi

rm -rf starter-cli/temp

############
# | BUILD  | TYPE | RUNTIME | FEATURES
# | GRADLE  | APP  | JAVA    | aws-lambda,aws-cdk,amazon-api-gateway-http
############

./gradlew micronaut-cli:run --args="create-app --jdk 17 -b gradle -f \"aws-lambda,aws-cdk,amazon-api-gateway-http\" temp" || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

cd starter-cli/temp

./test-lambda.sh  || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  echo "❌ FAILED | GRADLE  | APP  | JAVA | aws-lambda,aws-cdk,amazon-api-gateway-http"
  exit $EXIT_STATUS
fi

cd infra
cdk destroy -f
cd ../../..
rm -rf starter-cli/temp

############
# | BUILD  | TYPE     | RUNTIME | FEATURES
# | GRADLE | FUNCTION | JAVA    | aws-lambda,aws-cdk,aws-lambda-function-url
############

./gradlew micronaut-cli:run --args="create-function-app --jdk 17 -b gradle -f \"aws-lambda,aws-cdk,aws-lambda-function-url\" temp" || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then  
  exit $EXIT_STATUS
fi

cd starter-cli/temp

./test-lambda.sh  || EXIT_STATUS=$? 

if [ $EXIT_STATUS -ne 0 ]; then
  echo "❌ FAILED | GRADLE | FUNCTION | JAVA    | aws-lambda,aws-cdk,aws-lambda-function-url"
  exit $EXIT_STATUS
fi

cd infra
cdk destroy -f
cd ../../..
rm -rf starter-cli/temp

############
# | BUILD  | TYPE | RUNTIME | FEATURES
# | GRADLE | APP  | JAVA    | aws-lambda,aws-cdk,amazon-api-gateway
############

./gradlew micronaut-cli:run --args="create-app --jdk 17 -b gradle -f \"aws-lambda,aws-cdk,amazon-api-gateway\" temp" || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

cd starter-cli/temp

./test-lambda.sh  || EXIT_STATUS=$? 

if [ $EXIT_STATUS -ne 0 ]; then
  echo "❌ FAILED | GRADLE | APP  | JAVA    | aws-lambda,aws-cdk,amazon-api-gateway"
  exit $EXIT_STATUS
fi

cd infra
cdk destroy -f
cd ../../..
rm -rf starter-cli/temp

############
# | BUILD  | TYPE     | RUNTIME | FEATURES
# | GRADLE | FUNCTION | JAVA    | aws-lambda,aws-cdk,amazon-api-gateway
############

./gradlew micronaut-cli:run --args="create-function-app --jdk 17 -b gradle -f \"aws-lambda,aws-cdk,amazon-api-gateway\" temp" || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then  
  exit $EXIT_STATUS
fi

cd starter-cli/temp

./test-lambda.sh  || EXIT_STATUS=$? 

if [ $EXIT_STATUS -ne 0 ]; then
  echo "❌ FAILED | GRADLE | FUNCTION | JAVA    | aws-lambda,aws-cdk,amazon-api-gateway"
  exit $EXIT_STATUS
fi

cd infra
cdk destroy -f
cd ../../..
rm -rf starter-cli/temp

exit $EXIT_STATUS
