#!/bin/bash
EXIT_STATUS=0

rm -rf starter-cli/temp

############
# | BUILD  | TYPE | RUNTIME | FEATURES
# | MAVEN  | APP  | JAVA    | aws-lambda,aws-cdk,amazon-api-gateway-http
############
echo "create-app --jdk 17 -b maven -f \"aws-lambda,aws-cdk,amazon-api-gateway-http\" temp1"
./gradlew micronaut-cli:run --args="create-app --jdk 17 -b maven -f \"aws-lambda,aws-cdk,amazon-api-gateway-http\" temp1" || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

cd starter-cli/temp1

./test-lambda.sh  || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  echo "❌ FAILED | MAVEN  | APP  | JAVA | aws-lambda,aws-cdk,amazon-api-gateway-http"
  exit $EXIT_STATUS
fi

cd infra
cdk destroy -f
cd ../../..
rm -rf starter-cli/temp1


############
# | BUILD  | TYPE     | RUNTIME | FEATURES
# | MAVEN | FUNCTION  | JAVA    | aws-lambda,aws-cdk,aws-lambda-function-url
############
echo "create-function-app --jdk 17 -b maven -f \"aws-lambda,aws-cdk,aws-lambda-function-url\" temp2"
./gradlew micronaut-cli:run --args="create-function-app --jdk 17 -b maven -f \"aws-lambda,aws-cdk,aws-lambda-function-url\" temp2" || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

cd starter-cli/temp2

./test-lambda.sh  || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  echo "❌ FAILED | MAVEN | FUNCTION  | JAVA    | aws-lambda,aws-cdk,aws-lambda-function-url"
  exit $EXIT_STATUS
fi

cd infra
cdk destroy -f
cd ../../..
rm -rf starter-cli/temp2

############
# | BUILD  | TYPE | RUNTIME | FEATURES
# | MAVEN  | APP  | JAVA    | aws-lambda,aws-cdk,amazon-api-gateway
############
echo "create-app --jdk 17 -b maven -f \"aws-lambda,aws-cdk,amazon-api-gateway\" temp3"
./gradlew micronaut-cli:run --args="create-app --jdk 17 -b maven -f \"aws-lambda,aws-cdk,amazon-api-gateway\" temp3" || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

cd starter-cli/temp3

./test-lambda.sh  || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  echo "❌ FAILED | MAVEN  | APP  | JAVA    | aws-lambda,aws-cdk,amazon-api-gateway"
  exit $EXIT_STATUS
fi

cd infra
cdk destroy -f
cd ../../..
rm -rf starter-cli/temp3

############
# | BUILD  | TYPE     | RUNTIME | FEATURES
# | MAVEN | FUNCTION  | JAVA    | aws-lambda,aws-cdk,amazon-api-gateway
############
echo "create-function-app --jdk 17 -b maven -f \"aws-lambda,aws-cdk,amazon-api-gateway\" temp4"
./gradlew micronaut-cli:run --args="create-function-app --jdk 17 -b maven -f \"aws-lambda,aws-cdk,amazon-api-gateway\" temp4" || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

cd starter-cli/temp4

./test-lambda.sh  || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  echo "❌ FAILED | MAVEN | FUNCTION  | JAVA    | aws-lambda,aws-cdk,amazon-api-gateway"
  exit $EXIT_STATUS
fi

cd infra
cdk destroy -f
cd ../../..
rm -rf starter-cli/temp4

exit $EXIT_STATUS
