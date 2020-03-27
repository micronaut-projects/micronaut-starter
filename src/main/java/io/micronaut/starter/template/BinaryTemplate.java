package io.micronaut.starter.template;

import io.micronaut.starter.OutputHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class BinaryTemplate implements Template {

    private final String path;
    private final URL url;
    private final boolean executable;

    public BinaryTemplate(String path, URL url) {
        this(path, url, false);
    }

    public BinaryTemplate(String path, URL url, boolean executable) {
        this.path = path;
        this.url = url;
        this.executable = executable;
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
        try (InputStream inputStream = url.openStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
        }
    }

    public boolean isExecutable() {
        return executable;
    }

    @Override
    public String getPath() {
        return path;
    }
}
