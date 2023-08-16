package example;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.starter.client.github.v3.GitHubApiClient;
import io.micronaut.starter.client.github.v3.GitHubRepository;
import io.micronaut.starter.client.github.v3.GitHubSecret;
import io.micronaut.starter.client.github.v3.GitHubSecretsPublicKey;
import io.micronaut.starter.client.github.v3.GitHubUser;
import io.micronaut.starter.client.github.v3.GitHubWorkflowRun;
import io.micronaut.starter.client.github.v3.GitHubWorkflowRuns;
import jakarta.inject.Singleton;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.URIish;

import java.nio.file.Files;
import java.nio.file.Path;

@Replaces(GitHubApiClient.class)
@Singleton
public class GitHubApiClientMock implements GitHubApiClient {

    @Override
    public GitHubRepository createRepository(String oauthToken, GitHubRepository gitHubRepository) {
        try {
            Path temp = Files.createTempDirectory("test-github-create");
            Git repo = Git.init().setDirectory(temp.toFile()).setBare(true).call();
            URIish uri = new URIish(repo.getRepository().getDirectory().toURI().toURL());
            return new GitHubRepository(gitHubRepository.getName(), gitHubRepository.getDescription(), "url", "html_url", uri.toString());
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
