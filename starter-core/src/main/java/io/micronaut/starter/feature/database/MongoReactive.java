package io.micronaut.starter.feature.database;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.Feature;

import javax.inject.Singleton;

@Singleton
public class MongoReactive implements Feature {

    @Override
    public String getName() {
        return "mongo-reactive";
    }

    @Override
    public String getDescription() {
        return "Adds support for the Mongo Reactive Streams Driver";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getConfiguration().put("mongodb.uri", "mongodb://${MONGO_HOST:localhost}:${MONGO_PORT:27017}");
    }
}
