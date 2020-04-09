package io.micronaut.starter.template;

public interface RenderResult {

    boolean isSuccess();

    boolean isSkipped();

    String getPath();

    Exception getError();

    static RenderResult skipped(String path) {
        return new RenderResult() {
            @Override
            public boolean isSuccess() {
                return false;
            }

            @Override
            public boolean isSkipped() {
                return true;
            }

            @Override
            public String getPath() {
                return path;
            }

            @Override
            public Exception getError() {
                return null;
            }
        };
    }

    static RenderResult success(String path) {
        return new RenderResult() {
            @Override
            public boolean isSuccess() {
                return true;
            }

            @Override
            public boolean isSkipped() {
                return false;
            }

            @Override
            public String getPath() {
                return path;
            }

            @Override
            public Exception getError() {
                return null;
            }
        };
    }

    static RenderResult error(String path, Exception t) {
        return new RenderResult() {
            @Override
            public boolean isSuccess() {
                return false;
            }

            @Override
            public boolean isSkipped() {
                return false;
            }

            @Override
            public String getPath() {
                return path;
            }

            @Override
            public Exception getError() {
                return t;
            }
        };
    }
}
