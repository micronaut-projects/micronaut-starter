#!/bin/bash
EXIT_STATUS=0

JAVA_VER=$(java -version 2>&1 | sed -n ';s/.* version "\(.*\)\.\(.*\)\..*".*/\1\2/p;')
if [[ "$JAVA_VER" != "210" ]]; then
  echo test-lambda works only with Java 21
  exit 1;
fi

rm -rf starter-cli/temp

############
# | BUILD  | TYPE | RUNTIME | FEATURES
# | GRADLE  | APP  | JAVA    | aws-lambda,aws-cdk,amazon-api-gateway-http
############

./gradlew micronaut-cli:run --args="create-app --jdk 21 -b gradle -f \"aws-lambda,aws-cdk,amazon-api-gateway-http\" temp1" || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

cd starter-cli/temp1

./test-lambda.sh  || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  echo "❌ FAILED | GRADLE  | APP  | JAVA | aws-lambda,aws-cdk,amazon-api-gateway-http"
  exit $EXIT_STATUS
fi

cd infra
cdk destroy -f
cd ../../..
rm -rf starter-cli/temp1

############
# | BUILD  | TYPE     | RUNTIME | FEATURES
# | GRADLE | FUNCTION | JAVA    | aws-lambda,aws-cdk,aws-lambda-function-url
############

./gradlew micronaut-cli:run --args="create-function-app --jdk 21 -b gradle -f \"aws-lambda,aws-cdk,aws-lambda-function-url\" temp2" || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then  
  exit $EXIT_STATUS
fi

cd starter-cli/temp2

./test-lambda.sh  || EXIT_STATUS=$? 

if [ $EXIT_STATUS -ne 0 ]; then
  echo "❌ FAILED | GRADLE | FUNCTION | JAVA    | aws-lambda,aws-cdk,aws-lambda-function-url"
  exit $EXIT_STATUS
fi

cd infra
cdk destroy -f
cd ../../..
rm -rf starter-cli/temp2

############
# | BUILD  | TYPE | RUNTIME | FEATURES
# | GRADLE | APP  | JAVA    | aws-lambda,aws-cdk,amazon-api-gateway
############

./gradlew micronaut-cli:run --args="create-app --jdk 21 -b gradle -f \"aws-lambda,aws-cdk,amazon-api-gateway\" temp3" || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

cd starter-cli/temp3

./test-lambda.sh  || EXIT_STATUS=$? 

if [ $EXIT_STATUS -ne 0 ]; then
  echo "❌ FAILED | GRADLE | APP  | JAVA    | aws-lambda,aws-cdk,amazon-api-gateway"
  exit $EXIT_STATUS
fi

cd infra
cdk destroy -f
cd ../../..
rm -rf starter-cli/temp3

############
# | BUILD  | TYPE     | RUNTIME | FEATURES
# | GRADLE | FUNCTION | JAVA    | aws-lambda,aws-cdk,amazon-api-gateway
############

./gradlew micronaut-cli:run --args="create-function-app --jdk 21 -b gradle -f \"aws-lambda,aws-cdk,amazon-api-gateway\" temp4" || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then  
  exit $EXIT_STATUS
fi

cd starter-cli/temp4

./test-lambda.sh  || EXIT_STATUS=$? 

if [ $EXIT_STATUS -ne 0 ]; then
  echo "❌ FAILED | GRADLE | FUNCTION | JAVA    | aws-lambda,aws-cdk,amazon-api-gateway"
  exit $EXIT_STATUS
fi

cd infra
cdk destroy -f
cd ../../..
rm -rf starter-cli/temp4

exit $EXIT_STATUS
