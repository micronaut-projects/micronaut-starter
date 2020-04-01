package io.micronaut.starter.template;

import com.fizzed.rocker.RockerModel;
import com.fizzed.rocker.runtime.OutputStreamOutput;
import io.micronaut.starter.OutputHandler;

import java.io.OutputStream;

public class RockerTemplate implements Template {

    private final String path;
    private final RockerModel delegate;

    private final boolean executable;

    public RockerTemplate(String path, RockerModel delegate) {
        this(path, delegate, false);
    }

    public RockerTemplate(String path, RockerModel delegate, boolean executable) {
        this.path = path;
        this.delegate = delegate;
        this.executable = executable;
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

    @Override
    public boolean isExecutable() {
        return executable;
    }
}
