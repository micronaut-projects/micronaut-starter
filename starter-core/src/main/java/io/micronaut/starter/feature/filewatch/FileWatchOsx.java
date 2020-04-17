package io.micronaut.starter.feature.filewatch;

import io.micronaut.starter.feature.Feature;

import javax.inject.Singleton;

@Singleton
public class FileWatchOsx implements Feature {

    @Override
    public String getName() {
        return "file-watch-osx";
    }

    @Override
    public boolean isVisible() {
        return false;
    }
}
