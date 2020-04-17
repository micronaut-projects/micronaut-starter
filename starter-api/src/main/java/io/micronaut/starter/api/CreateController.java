package io.micronaut.starter.api;

import io.micronaut.core.io.Writable;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.starter.ContextFactory;
import io.micronaut.starter.Project;
import io.micronaut.starter.command.CreateAppCommand;
import io.micronaut.starter.command.CreateCliCommand;
import io.micronaut.starter.command.CreateGrpcCommand;
import io.micronaut.starter.feature.validation.FeatureValidator;
import io.micronaut.starter.io.ZipOutputHandler;
import io.micronaut.starter.util.NameUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the {@link CreateOperations} interface.
 *
 * @author graemerocher
 * @since 1.0.0
 */
@Controller("/create")
public class CreateController implements CreateOperations {
    private final CreateAppCommand.CreateAppFeatures createAppFeatures;
    private final FeatureValidator featureValidator;
    private final ContextFactory contextFactory;
    private final CreateGrpcCommand.CreateGrpcFeatures createGrpcFeatures;
    private final CreateCliCommand.CreateCliFeatures createCliFeatures;

    public CreateController(
            CreateGrpcCommand.CreateGrpcFeatures createGrpcFeatures,
            CreateAppCommand.CreateAppFeatures createAppFeatures,
            CreateCliCommand.CreateCliFeatures createCliFeatures,
            FeatureValidator featureValidator,
            ContextFactory contextFactory) {
        this.createCliFeatures = createCliFeatures;
        this.createGrpcFeatures = createGrpcFeatures;
        this.createAppFeatures = createAppFeatures;
        this.featureValidator = featureValidator;
        this.contextFactory = contextFactory;
    }

    @Override
    public HttpResponse<Writable> createApp(String name, @Nullable List<String> features) {
        Project project = NameUtils.parse(name);
        MutableHttpResponse<Writable> response = HttpResponse.created(new Writable() {
            @Override
            public void writeTo(OutputStream outputStream, @Nullable Charset charset) throws IOException {
                CreateAppCommand createAppCommand = new CreateAppCommand(
                        createAppFeatures,
                        featureValidator,
                        contextFactory

                ) {
                    @Override
                    protected List<String> getSelectedFeatures() {
                        return features != null ? features : new ArrayList<>();
                    }
                };
                try {
                    createAppCommand.generate(project, new ZipOutputHandler(outputStream));
                    outputStream.flush();
                } catch (Exception e) {
                    throw new IOException(e.getMessage(), e);
                }
            }

            @Override
            public void writeTo(Writer out) throws IOException {
                // no-op, output stream used
            }
        });
        return response.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=application.zip");
    }

    @Override
    public HttpResponse<Writable> createFunction(String name, @Nullable List<String> features) {
        // TODO: Implement function support
        return HttpResponse.status(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Override
    public HttpResponse<Writable> createGrpcApp(String name, @Nullable List<String> features) {
        Project project = NameUtils.parse(name);
        MutableHttpResponse<Writable> response = HttpResponse.created(new Writable() {
            @Override
            public void writeTo(OutputStream outputStream, @Nullable Charset charset) throws IOException {
                CreateGrpcCommand createAppCommand = new CreateGrpcCommand(
                        createGrpcFeatures,
                        featureValidator,
                        contextFactory

                ) {
                    @Override
                    protected List<String> getSelectedFeatures() {
                        return features != null ? features : new ArrayList<>();
                    }
                };
                try {
                    createAppCommand.generate(project, new ZipOutputHandler(outputStream));
                    outputStream.flush();
                } catch (Exception e) {
                    throw new IOException(e.getMessage(), e);
                }
            }

            @Override
            public void writeTo(Writer out) throws IOException {
                // no-op, output stream used
            }
        });
        return response.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=application.zip");
    }

    @Override
    public HttpResponse<Writable> createMessagingApp(String name, @Nullable List<String> features) {
        // TODO: Implement messaging app support
        return HttpResponse.status(HttpStatus.SERVICE_UNAVAILABLE);

    }

    @Override
    public HttpResponse<Writable> createCliApp(String name, @Nullable List<String> features) {
        Project project = NameUtils.parse(name);
        MutableHttpResponse<Writable> response = HttpResponse.created(new Writable() {
            @Override
            public void writeTo(OutputStream outputStream, @Nullable Charset charset) throws IOException {
                CreateCliCommand createAppCommand = new CreateCliCommand(
                        createCliFeatures,
                        featureValidator,
                        contextFactory

                ) {
                    @Override
                    protected List<String> getSelectedFeatures() {
                        return features != null ? features : new ArrayList<>();
                    }
                };
                try {
                    createAppCommand.generate(project, new ZipOutputHandler(outputStream));
                    outputStream.flush();
                } catch (Exception e) {
                    throw new IOException(e.getMessage(), e);
                }
            }

            @Override
            public void writeTo(Writer out) throws IOException {
                // no-op, output stream used
            }
        });
        return response.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=application.zip");
    }
}
