#!/bin/bash
EXIT_STATUS=0

architecture="$1"
if [ "$architecture" != "arm" ] && [ "$architecture" != "x86" ]; then
    echo "First parameter is not equal to 'arm' or 'x86'"
    exit 1
fi

rm -rf starter-cli/temp

############
# | BUILD  | TYPE     | RUNTIME  | FEATURES
# | GRADLE | APP      | PROVIDED | jackson-databind,aws-lambda,aws-cdk,amazon-api-gateway-http,graalvm,$architecture
############

echo "create-app -b gradle -f \"jackson-databind,aws-lambda,aws-cdk,amazon-api-gateway-http,graalvm,$architecture\" temp"
./gradlew micronaut-cli:run --args="create-app -b gradle -f \"jackson-databind,aws-lambda,aws-cdk,amazon-api-gateway-http,graalvm,$architecture\" temp" || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then  
  exit $EXIT_STATUS
fi

cd starter-cli/temp

./test-lambda.sh  || EXIT_STATUS=$? 

if [ $EXIT_STATUS -ne 0 ]; then
  echo "❌ FAILED | GRADLE | APP | PROVIDED    | jackson-databind,aws-lambda,aws-cdk,amazon-api-gateway-http,graalvm,$architecture"
  exit $EXIT_STATUS
fi

cd infra
cdk destroy -f
cd ../../..
rm -rf starter-cli/temp

############
# | BUILD  | TYPE     | RUNTIME  | FEATURES
# | GRADLE | FUNCTION | PROVIDED | aws-lambda,aws-cdk,aws-lambda-function-url,graalvm,$architecture
############
echo "create-function-app -b gradle -f \"aws-lambda,aws-cdk,aws-lambda-function-url,graalvm,$architecture\" temp"
./gradlew micronaut-cli:run --args="create-function-app -b gradle -f \"aws-lambda,aws-cdk,aws-lambda-function-url,graalvm,$architecture\" temp" || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then  
  exit $EXIT_STATUS
fi

cd starter-cli/temp

./test-lambda.sh  || EXIT_STATUS=$? 

if [ $EXIT_STATUS -ne 0 ]; then
  echo "❌ FAILED | GRADLE | FUNCTION | PROVIDED    | aws-lambda,aws-cdk,aws-lambda-function-url,graalvm,$architecture"
  exit $EXIT_STATUS
fi

cd infra
cdk destroy -f
cd ../../..
rm -rf starter-cli/temp

############
# | BUILD  | TYPE | RUNTIME  | FEATURES
# | GRADLE | APP  | PROVIDED | aws-lambda,aws-cdk,graalvm,amazon-api-gateway,$architecture
############
echo "create-app -b gradle -f \"aws-lambda,aws-cdk,graalvm,amazon-api-gateway,$architecture\" temp"
./gradlew micronaut-cli:run --args="create-app -b gradle -f \"aws-lambda,aws-cdk,graalvm,amazon-api-gateway,$architecture\" temp" || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

cd starter-cli/temp

./test-lambda.sh  || EXIT_STATUS=$? 

if [ $EXIT_STATUS -ne 0 ]; then
  echo "❌ FAILED | GRADLE  | APP  | PROVIDED | aws-lambda,aws-cdk,graalvm,amazon-api-gateway,$architecture"
  exit $EXIT_STATUS
fi

cd infra
cdk destroy -f
cd ../../..
rm -rf starter-cli/temp

############
# | BUILD  | TYPE | RUNTIME  | FEATURES
# | GRADLE | APP  | PROVIDED | aws-lambda,aws-cdk,graalvm,amazon-api-gateway-http,$architecture
############
echo "create-app -b gradle -f \"aws-lambda,aws-cdk,graalvm,amazon-api-gateway-http,$architecture\" temp"
./gradlew micronaut-cli:run --args="create-app -b gradle -f \"aws-lambda,aws-cdk,graalvm,amazon-api-gateway-http,$architecture\" temp" || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

cd starter-cli/temp

./test-lambda.sh  || EXIT_STATUS=$? 

if [ $EXIT_STATUS -ne 0 ]; then
  echo "❌ FAILED | GRADLE  | APP  | PROVIDED | aws-lambda,aws-cdk,graalvm,amazon-api-gateway-http,$architecture"
  exit $EXIT_STATUS
fi

cd infra
cdk destroy -f
cd ../../..
rm -rf starter-cli/temp


############
# | BUILD  | TYPE     | RUNTIME  | FEATURES
# | GRADLE | FUNCTION | PROVIDED | aws-lambda,aws-cdk,graalvm,amazon-api-gateway,$architecture
############
echo "create-function-app -b gradle -f \"aws-lambda,aws-cdk,graalvm,amazon-api-gateway,$architecture\" temp"
./gradlew micronaut-cli:run --args="create-function-app -b gradle -f \"aws-lambda,aws-cdk,graalvm,amazon-api-gateway,$architecture\" temp" || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

cd starter-cli/temp

./test-lambda.sh  || EXIT_STATUS=$? 

if [ $EXIT_STATUS -ne 0 ]; then
  echo "❌ FAILED | GRADLE  | FUNCTION  | PROVIDED | aws-lambda,aws-cdk,graalvm,amazon-api-gateway,$architecture"
  exit $EXIT_STATUS
fi

cd infra
cdk destroy -f
cd ../../..
rm -rf starter-cli/temp

############
# | BUILD  | TYPE     | RUNTIME  | FEATURES
# | GRADLE | FUNCTION | PROVIDED | aws-lambda,aws-cdk,graalvm,amazon-api-gateway-http,$architecture
############
echo "create-function-app -b gradle -f \"aws-lambda,aws-cdk,graalvm,amazon-api-gateway-http,$architecture\" temp"
./gradlew micronaut-cli:run --args="create-function-app -b gradle -f \"aws-lambda,aws-cdk,graalvm,amazon-api-gateway-http,$architecture\" temp" || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

cd starter-cli/temp

./test-lambda.sh  || EXIT_STATUS=$? 

if [ $EXIT_STATUS -ne 0 ]; then
  echo "❌ FAILED | GRADLE  | FUNCTION  | PROVIDED | aws-lambda,aws-cdk,graalvm,amazon-api-gateway-http,$architecture"
  exit $EXIT_STATUS
fi

cd infra
cdk destroy -f
cd ../../..
rm -rf starter-cli/temp

exit $EXIT_STATUS