package io.micronaut.starter.template;

import java.net.URL;

public class BinaryTemplate extends URLTemplate {

    public BinaryTemplate(String path, URL url) {
        super(path, url);
    }

    @Override
    public boolean isBinary() {
        return true;
    }
}
