FROM ghcr.io/graalvm/graalvm-ce:java8-21.0.0 as graalvm
# For JDK 11
#FROM ghcr.io/graalvm/graalvm-ce:java11-21.0.0 as graalvm
RUN gu install native-image

COPY . /home/app/micronaut-starter
WORKDIR /home/app/micronaut-starter

RUN native-image -cp build/libs/starter-web-netty-*-all.jar

FROM adoptopenjdk/openjdk11:alpine-slim
EXPOSE 8080
COPY --from=graalvm /home/app/micronaut-starter/micronaut-starter /app/micronaut-starter
ENTRYPOINT ["/app/micronaut-starter"]
