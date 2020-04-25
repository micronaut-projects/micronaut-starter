#!/bin/sh
GRAAL_FILENAME="graalvm-ce-java8-${GRAAL_OS}-${GRAAL_VERSION}.tar.gz"

mkdir tmp
curl -4 -L https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-${GRAAL_VERSION}/${GRAAL_FILENAME} -o tmp/${GRAAL_FILENAME}
tar -zxvf tmp/${GRAAL_FILENAME} -C tmp && mv tmp/graalvm-ce-java8-${GRAAL_VERSION} graalvm
rm -rf tmp/*