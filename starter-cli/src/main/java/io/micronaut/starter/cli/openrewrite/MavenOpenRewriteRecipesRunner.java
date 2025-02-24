package io.micronaut.starter.cli.openrewrite;

import io.micronaut.starter.sdk.OperatingSystem;
import io.micronaut.starter.openrewrite.OpenRewriteConfiguration;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.apache.maven.shared.invoker.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Named("maven")
@Singleton
public class MavenOpenRewriteRecipesRunner implements OpenRewriteRecipesRunner {

    @Override
    public void run(List<String> recipes,
                    File folder,
                    OpenRewriteConfiguration configuration,
                    Consumer<String> out,
                    Consumer<String> err) {
        InvocationRequest request = createInvocationRequest(folder, configuration, out, err);
        Invoker invoker = new DefaultInvoker();
        try {
            invoker.execute(request);
        } catch (MavenInvocationException e) {
            throw new RuntimeException(e);
        }
    }

    private InvocationRequest createInvocationRequest(File folder,
                                                      OpenRewriteConfiguration configuration,
                                                      Consumer<String> out,
                                                      Consumer<String> err) {
        String goal = "rewrite:run";
        List<String> args = new ArrayList<>();
        args.add(goal);
        args.addAll(configuration.getSystemPropertiesList());
        InvocationRequest request = new DefaultInvocationRequest();
        request.setBaseDirectory(folder);
        request.setPomFile(new File(folder, "pom.xml"));
        request.addArgs(args);
        request.setDebug(true);
        request.setOutputHandler(out::accept);
        request.setErrorHandler(err::accept);
        request.setBatchMode(true);
        request.setMavenExecutable(configuration.operatingSystem() == OperatingSystem.WINDOWS
                ? new File(folder, "mvnw.bat")
                : new File(folder, "mvnw"));
        return request;
    }
}
