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

echo "create-app -b gradle -f \"jackson-databind,aws-lambda,aws-cdk,amazon-api-gateway-http,graalvm,$architecture\" temp1"
./gradlew micronaut-cli:run --args="create-app -b gradle -f \"jackson-databind,aws-lambda,aws-cdk,amazon-api-gateway-http,graalvm,$architecture\" temp1" || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then  
  exit $EXIT_STATUS
fi

cd starter-cli/temp1

./test-lambda.sh  || EXIT_STATUS=$? 

if [ $EXIT_STATUS -ne 0 ]; then
  echo "❌ FAILED | GRADLE | APP | PROVIDED    | jackson-databind,aws-lambda,aws-cdk,amazon-api-gateway-http,graalvm,$architecture"
  exit $EXIT_STATUS
fi

cd infra
cdk destroy -f
cd ../../..
rm -rf starter-cli/temp1

############
# | BUILD  | TYPE     | RUNTIME  | FEATURES
# | GRADLE | FUNCTION | PROVIDED | aws-lambda,aws-cdk,aws-lambda-function-url,graalvm,$architecture
############
echo "create-function-app -b gradle -f \"aws-lambda,aws-cdk,aws-lambda-function-url,graalvm,$architecture\" temp2"
./gradlew micronaut-cli:run --args="create-function-app -b gradle -f \"aws-lambda,aws-cdk,aws-lambda-function-url,graalvm,$architecture\" temp2" || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then  
  exit $EXIT_STATUS
fi

cd starter-cli/temp2

./test-lambda.sh  || EXIT_STATUS=$? 

if [ $EXIT_STATUS -ne 0 ]; then
  echo "❌ FAILED | GRADLE | FUNCTION | PROVIDED    | aws-lambda,aws-cdk,aws-lambda-function-url,graalvm,$architecture"
  exit $EXIT_STATUS
fi

cd infra
cdk destroy -f
cd ../../..
rm -rf starter-cli/temp2

############
# | BUILD  | TYPE | RUNTIME  | FEATURES
# | GRADLE | APP  | PROVIDED | aws-lambda,aws-cdk,graalvm,amazon-api-gateway,$architecture
############
echo "create-app -b gradle -f \"aws-lambda,aws-cdk,graalvm,amazon-api-gateway,$architecture\" temp3"
./gradlew micronaut-cli:run --args="create-app -b gradle -f \"aws-lambda,aws-cdk,graalvm,amazon-api-gateway,$architecture\" temp3" || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

cd starter-cli/temp3

./test-lambda.sh  || EXIT_STATUS=$? 

if [ $EXIT_STATUS -ne 0 ]; then
  echo "❌ FAILED | GRADLE  | APP  | PROVIDED | aws-lambda,aws-cdk,graalvm,amazon-api-gateway,$architecture"
  exit $EXIT_STATUS
fi

cd infra
cdk destroy -f
cd ../../..
rm -rf starter-cli/temp3

############
# | BUILD  | TYPE | RUNTIME  | FEATURES
# | GRADLE | APP  | PROVIDED | aws-lambda,aws-cdk,graalvm,amazon-api-gateway-http,$architecture
############
echo "create-app -b gradle -f \"aws-lambda,aws-cdk,graalvm,amazon-api-gateway-http,$architecture\" temp4"
./gradlew micronaut-cli:run --args="create-app -b gradle -f \"aws-lambda,aws-cdk,graalvm,amazon-api-gateway-http,$architecture\" temp4" || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

cd starter-cli/temp4

./test-lambda.sh  || EXIT_STATUS=$? 

if [ $EXIT_STATUS -ne 0 ]; then
  echo "❌ FAILED | GRADLE  | APP  | PROVIDED | aws-lambda,aws-cdk,graalvm,amazon-api-gateway-http,$architecture"
  exit $EXIT_STATUS
fi

cd infra
cdk destroy -f
cd ../../..
rm -rf starter-cli/temp4

############
# | BUILD  | TYPE     | RUNTIME  | FEATURES
# | GRADLE | FUNCTION | PROVIDED | aws-lambda,aws-cdk,graalvm,amazon-api-gateway,$architecture
############
echo "create-function-app -b gradle -f \"aws-lambda,aws-cdk,graalvm,amazon-api-gateway,$architecture\" temp5"
./gradlew micronaut-cli:run --args="create-function-app -b gradle -f \"aws-lambda,aws-cdk,graalvm,amazon-api-gateway,$architecture\" temp5" || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

cd starter-cli/temp5

./test-lambda.sh  || EXIT_STATUS=$? 

if [ $EXIT_STATUS -ne 0 ]; then
  echo "❌ FAILED | GRADLE  | FUNCTION  | PROVIDED | aws-lambda,aws-cdk,graalvm,amazon-api-gateway,$architecture"
  exit $EXIT_STATUS
fi

cd infra
cdk destroy -f
cd ../../..
rm -rf starter-cli/temp5

############
# | BUILD  | TYPE     | RUNTIME  | FEATURES
# | GRADLE | FUNCTION | PROVIDED | aws-lambda,aws-cdk,graalvm,amazon-api-gateway-http,$architecture
############
echo "create-function-app -b gradle -f \"aws-lambda,aws-cdk,graalvm,amazon-api-gateway-http,$architecture\" temp6"
./gradlew micronaut-cli:run --args="create-function-app -b gradle -f \"aws-lambda,aws-cdk,graalvm,amazon-api-gateway-http,$architecture\" temp6" || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

cd starter-cli/temp6

./test-lambda.sh  || EXIT_STATUS=$? 

if [ $EXIT_STATUS -ne 0 ]; then
  echo "❌ FAILED | GRADLE  | FUNCTION  | PROVIDED | aws-lambda,aws-cdk,graalvm,amazon-api-gateway-http,$architecture"
  exit $EXIT_STATUS
fi

cd infra
cdk destroy -f
cd ../../..
rm -rf starter-cli/temp6

exit $EXIT_STATUS