package example;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.starter.api.StarterConfiguration;
import io.micronaut.starter.api.create.github.GitHubCreateDTO;
import io.micronaut.starter.api.preview.PreviewDTO;
import io.micronaut.starter.netty.Application;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(application = Application.class)
public class StarterWebNettyTest {

    private static final String BASE_URI = "/default/com.example.demo?lang=JAVA&build=GRADLE&test=JUNIT&javaVersion=JDK_17";

    private static final String ZIP_CREATE_URI = "/create" + BASE_URI;

    private static final String GITHUB_CREATE_URI = "/github" + BASE_URI + "&code=testCode&state=testState";

    private static final String PREVIEW_URI = "/preview" + BASE_URI;

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    StarterConfiguration starterConfiguration;

    @Test
    void testZipCreate() {
        HttpRequest<?> request = HttpRequest.GET(ZIP_CREATE_URI).accept("application/zip");
        HttpResponse<byte[]> response = client.toBlocking().exchange(request, byte[].class);
        assertNotNull(response);
        assertTrue(response.getBody().isPresent());
    }

    @Test
    void testGitHubCreate() {
        starterConfiguration.setRedirectUri(null);
        GitHubCreateDTO response = client.toBlocking().retrieve(HttpRequest.GET(GITHUB_CREATE_URI), GitHubCreateDTO.class);
        assertNotNull(response);
        assertNotNull(response.getCloneUrl());
    }

    @Test
    void testPreview() {
        PreviewDTO response = client.toBlocking().retrieve(HttpRequest.GET(PREVIEW_URI), PreviewDTO.class);
        assertNotNull(response);
        assertNotNull(response.getContents());
        assertFalse(response.getContents().isEmpty());
        assertNotNull(response.getContents().get("settings.gradle"));
        assertTrue(response.getContents().get("settings.gradle").contains("rootProject.name=\"demo\""));
        assertNull(response.getContents().get("gradle/wrapper/gradle-wrapper.jar"));
    }
}
