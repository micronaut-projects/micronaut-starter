package example;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.starter.client.github.v3.GitHubApiClient;
import io.micronaut.starter.client.github.v3.GitHubRepository;
import io.micronaut.starter.client.github.v3.GitHubSecret;
import io.micronaut.starter.client.github.v3.GitHubSecretsPublicKey;
import io.micronaut.starter.client.github.v3.GitHubUser;
import io.micronaut.starter.client.github.v3.GitHubWorkflowRun;
import io.micronaut.starter.client.github.v3.GitHubWorkflowRuns;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Singleton;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.URIish;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

@Replaces(GitHubApiClient.class)
@Singleton
public class GitHubApiClientMock implements GitHubApiClient {

    private static final Logger LOG = LoggerFactory.getLogger(GitHubApiClientMock.class);

    private Repository gitRepository;

    private String gitRepositoryUrl;

    @PostConstruct
    public void initialize() throws Exception {
        Path temp = Files.createTempDirectory("test-github-create");
        Git git = Git.init().setDirectory(temp.toFile()).setBare(true).call();
        gitRepository = git.getRepository();
        URIish uri = new URIish(gitRepository.getDirectory().toURI().toURL());
        gitRepositoryUrl = uri.toString();
    }

    @PreDestroy
    public void close() {
        if (gitRepository != null) {
            try {
                gitRepository.close();
            } catch (Exception e) {
                LOG.warn("Failed to close git repository: {}", gitRepositoryUrl, e);
            }
            try {
                Files.walk(Path.of(new URI(gitRepositoryUrl)))
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            } catch (Exception e) {
                LOG.warn("Failed to delete git repository: {}", gitRepositoryUrl, e);
            }
        }
    }

    @Override
    public GitHubRepository createRepository(String oauthToken, GitHubRepository gitHubRepository) {
        try {
            return new GitHubRepository(gitHubRepository.getName(), gitHubRepository.getDescription(), "url", "html_url", gitRepositoryUrl);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create repository: " + gitHubRepository.getName(), e);
        }
    }

    @Override
    public GitHubRepository getRepository(String oauthToken, String owner, String repo) {
        return null;
    }

    @Override
    public void deleteRepository(String oauthToken, String owner, String repo) {

    }

    @Override
    public GitHubUser getUser(String oauthToken) {
        return new GitHubUser("testLogin", "testEmail", "testName");
    }

    @Override
    public void createSecret(String oauthToken, String owner, String repo, String secretName, GitHubSecret secret) {

    }

    @Override
    public GitHubSecretsPublicKey getSecretPublicKey(String oauthToken, String owner, String repo) {
        return null;
    }

    @Override
    public GitHubWorkflowRuns listWorkflows(String oauthToken, String owner, String repo) {
        return null;
    }

    @Override
    public GitHubWorkflowRun getWorkflowRun(String oauthToken, String owner, String repo, Long runId) {
        return null;
    }
}
