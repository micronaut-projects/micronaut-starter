package io.micronaut.starter.template;

import com.fizzed.rocker.RockerModel;
import com.fizzed.rocker.runtime.ArrayOfByteArraysOutput;
import com.fizzed.rocker.runtime.OutputStreamOutput;
import io.micronaut.starter.OutputHandler;

import java.io.InputStream;
import java.io.OutputStream;

public class RockerTemplate implements Template {

    private final String path;
    private final RockerModel delegate;

    public RockerTemplate(String path, RockerModel delegate) {
        this.path = path;
        this.delegate = delegate;
    }

    public void write(OutputHandler outputHandler) {

    }

    @Override
    public void write(OutputStream outputStream) {
        delegate.render((contentType, charsetName) ->
                new OutputStreamOutput(contentType, outputStream, charsetName));
    }

    @Override
    public String getPath() {
        return path;
    }
}
