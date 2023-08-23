#!/bin/bash
EXIT_STATUS=0

rm -rf starter-cli/temp

############
# | BUILD  | TYPE     | RUNTIME  | FEATURES
# | GRADLE | APP      | PROVIDED | jackson-databind,aws-lambda,aws-cdk,amazon-api-gateway-http,graalvm
############

./gradlew micronaut-cli:run --args="create-app -b gradle -f \"jackson-databind,aws-lambda,aws-cdk,amazon-api-gateway-http,graalvm\" temp" || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then  
  exit $EXIT_STATUS
fi

cd starter-cli/temp

./test-lambda.sh  || EXIT_STATUS=$? 

if [ $EXIT_STATUS -ne 0 ]; then
  echo "❌ FAILED | GRADLE | APP | PROVIDED    | jackson-databind,aws-lambda,aws-cdk,amazon-api-gateway-http,graalvm"
  exit $EXIT_STATUS
fi

cd infra
cdk destroy -f
cd ../../..
rm -rf starter-cli/temp

############
# | BUILD  | TYPE     | RUNTIME  | FEATURES
# | GRADLE | FUNCTION | PROVIDED | aws-lambda,aws-cdk,aws-lambda-function-url,graalvm
############

./gradlew micronaut-cli:run --args="create-function-app -b gradle -f \"aws-lambda,aws-cdk,aws-lambda-function-url,graalvm\" temp" || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then  
  exit $EXIT_STATUS
fi

cd starter-cli/temp

./test-lambda.sh  || EXIT_STATUS=$? 

if [ $EXIT_STATUS -ne 0 ]; then
  echo "❌ FAILED | GRADLE | FUNCTION | PROVIDED    | aws-lambda,aws-cdk,aws-lambda-function-url,graalvm"
  exit $EXIT_STATUS
fi

cd infra
cdk destroy -f
cd ../../..
rm -rf starter-cli/temp

############
# | BUILD  | TYPE | RUNTIME  | FEATURES
# | GRADLE | APP  | PROVIDED | aws-lambda,aws-cdk,graalvm,amazon-api-gateway
############

./gradlew micronaut-cli:run --args="create-app -b gradle -f \"aws-lambda,aws-cdk,graalvm,amazon-api-gateway\" temp" || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

cd starter-cli/temp

./test-lambda.sh  || EXIT_STATUS=$? 

if [ $EXIT_STATUS -ne 0 ]; then
  echo "❌ FAILED | GRADLE  | APP  | PROVIDED | aws-lambda,aws-cdk,graalvm,amazon-api-gateway"
  exit $EXIT_STATUS
fi

cd infra
cdk destroy -f
cd ../../..
rm -rf starter-cli/temp

############
# | BUILD  | TYPE | RUNTIME  | FEATURES
# | GRADLE | APP  | PROVIDED | aws-lambda,aws-cdk,graalvm,amazon-api-gateway-http
############

./gradlew micronaut-cli:run --args="create-app -b gradle -f \"aws-lambda,aws-cdk,graalvm,amazon-api-gateway-http\" temp" || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

cd starter-cli/temp

./test-lambda.sh  || EXIT_STATUS=$? 

if [ $EXIT_STATUS -ne 0 ]; then
  echo "❌ FAILED | GRADLE  | APP  | PROVIDED | aws-lambda,aws-cdk,graalvm,amazon-api-gateway-http"
  exit $EXIT_STATUS
fi

cd infra
cdk destroy -f
cd ../../..
rm -rf starter-cli/temp


############
# | BUILD  | TYPE     | RUNTIME  | FEATURES
# | GRADLE | FUNCTION | PROVIDED | aws-lambda,aws-cdk,graalvm,amazon-api-gateway
############

./gradlew micronaut-cli:run --args="create-function-app -b gradle -f \"aws-lambda,aws-cdk,graalvm,amazon-api-gateway\" temp" || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

cd starter-cli/temp

./test-lambda.sh  || EXIT_STATUS=$? 

if [ $EXIT_STATUS -ne 0 ]; then
  echo "❌ FAILED | GRADLE  | FUNCTION  | PROVIDED | aws-lambda,aws-cdk,graalvm,amazon-api-gateway"
  exit $EXIT_STATUS
fi

cd infra
cdk destroy -f
cd ../../..
rm -rf starter-cli/temp

############
# | BUILD  | TYPE     | RUNTIME  | FEATURES
# | GRADLE | FUNCTION | PROVIDED | aws-lambda,aws-cdk,graalvm,amazon-api-gateway-http
############

./gradlew micronaut-cli:run --args="create-function-app -b gradle -f \"aws-lambda,aws-cdk,graalvm,amazon-api-gateway-http\" temp" || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

cd starter-cli/temp

./test-lambda.sh  || EXIT_STATUS=$? 

if [ $EXIT_STATUS -ne 0 ]; then
  echo "❌ FAILED | GRADLE  | FUNCTION  | PROVIDED | aws-lambda,aws-cdk,graalvm,amazon-api-gateway-http"
  exit $EXIT_STATUS
fi

cd infra
cdk destroy -f
cd ../../..
rm -rf starter-cli/temp

exit $EXIT_STATUS