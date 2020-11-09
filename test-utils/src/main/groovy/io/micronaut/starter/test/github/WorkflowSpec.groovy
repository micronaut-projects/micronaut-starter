package io.micronaut.starter.test.github

import groovy.util.logging.Slf4j
import io.micronaut.core.util.StringUtils
import io.micronaut.starter.application.Project
import io.micronaut.starter.feature.github.workflows.Secret
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.github.client.GitHubApiClient
import io.micronaut.starter.test.github.client.GitHubRepository
import io.micronaut.starter.test.github.client.GitHubSecret
import io.micronaut.starter.test.github.client.GitHubSecretsPublicKey
import io.micronaut.starter.test.github.client.GitHubUser
import io.micronaut.starter.test.github.client.GitHubWorkflowRun
import io.micronaut.starter.test.github.client.GitHubWorkflowRuns
import org.apache.tuweni.crypto.sodium.Box
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.errors.GitAPIException
import org.eclipse.jgit.transport.PushResult
import org.eclipse.jgit.transport.RemoteRefUpdate
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import spock.lang.Shared
import spock.util.concurrent.PollingConditions

import java.nio.file.Path
import java.util.stream.Collectors
import java.util.stream.StreamSupport

/**
 * Spec class for testing of GitHub workflows. If the creation of secrets on demand is requested, then the LibSodium
 * have to be installed in the system. For more information <a href="https://docs.github.com/en/free-pro-team@latest/actions/reference/encrypted-secrets">encrypted secrets</a>
 */
@Slf4j
abstract class WorkflowSpec extends CommandSpec{

    public static final String GH_TOKEN = "GH_TOKEN"

    @Shared
    private String apiToken

    @Shared
    private String token

    void setupSpec() {
        token = System.getenv(GH_TOKEN)
        apiToken = "token $token"
    }

    @Override
    Map<String, Object> getConfiguration() {
        return ["micronaut.http.services.github-api-v3.url": GitHubApiClient.SERVICE_URL]
    }

    protected void pushToGitHub(Project project, List<Secret> secrets){
        String repoName = project.getName()

        GitHubApiClient apiClient = beanContext.getBean(GitHubApiClient)
        GitHubUser owner = apiClient.getUser(apiToken)

        GitHubRepository repository = apiClient.createRepository(apiToken, new GitHubRepository(repoName, "Test"))
        log.debug("Repository created ${repository}")

        GitHubSecretsPublicKey secretsPublicKey = apiClient.getSecretPublicKey(apiToken, owner.login, repoName)
        for(Secret secret : secrets){
            if(secret.name && secret.value) {
                log.debug("Creating secret ${secret.name}")
                String encryptedSecret = encryptSecret(secret.value, secretsPublicKey.key)
                GitHubSecret gitHubSecret = new GitHubSecret(encryptedSecret, secretsPublicKey.keyId)
                apiClient.createSecret(apiToken, owner.login, repository.name, secret.name, gitHubSecret)
            }
        }

        pushToRepository(repository, owner, dir.toPath(), token)
    }

    /**
     * Polls on the first workflow for the status=completed and conclusion=passed.
     * @param project project
     * @param timeout timeout for polling
     */
    protected void workflowPassed(Project project, Long timeout){
        GitHubApiClient apiClient = beanContext.getBean(GitHubApiClient)
        GitHubUser owner = apiClient.getUser(apiToken)

        log.debug("Fetching workflows")
        new PollingConditions().within(30){
            GitHubWorkflowRuns workflowRuns = apiClient.listWorkflows(apiToken, owner.login, project.name)
            assert !workflowRuns.runs.isEmpty() : "workflow didn't starter"
        }

        Long id = apiClient.listWorkflows(apiToken, owner.login, project.name).runs.first().id
        log.debug("Polling on workflow status=completed and conclusion=success")
        new PollingConditions(initialDelay: 5*60, delay: 60).within(timeout) {
            GitHubWorkflowRun run = apiClient.getWorkflowRun(apiToken, owner.login, project.name, id)
            assert run.status == "completed" : "workflow status should be 'completed' but got: ${run.status}"
            assert run.conclusion == "success" : "workflow conclusion should be 'success' but got: ${run.conclusion}"
        }
    }

    /**
     * Push files in {@code localPath} to the GitHub repository {@code repository}.
     *
     * @param repository github repository
     * @param user       github user
     * @param localPath  local directry with generated content
     * @param token      github user access token
     * @throws IOException
     */
    protected static void pushToRepository(
            GitHubRepository repository,
            GitHubUser user,
            Path localPath,
            String token) throws IOException {
        try {
            final String name = StringUtils.isEmpty(user.getName()) ? user.getLogin() : user.getName();
            final String email = StringUtils.isEmpty(user.getEmail()) ? user.getLogin() : user.getEmail();

            Git gitRepo = Git.init().setDirectory(localPath.toFile())
                    .call();
            gitRepo.add()
                    .addFilepattern(".")
                    .call();

            gitRepo.commit()
                    .setMessage("Initial commit")
                    .setAuthor(name, email)
                    .setCommitter(name, email)
                    .setSign(false).call();

            Iterable<PushResult> pushResults = gitRepo.push()
                    .setRemote(repository.getCloneUrl())
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(user.getLogin(), token))
                    .call();

            List<RemoteRefUpdate> failedRefUpdates = StreamSupport.stream(pushResults.spliterator(), false)
                    .flatMap{pushResult -> pushResult.getRemoteUpdates().stream()}
                    .filter{remoteRefUpdate -> remoteRefUpdate.getStatus() != RemoteRefUpdate.Status.OK}
                    .collect(Collectors.toList());

            if (!failedRefUpdates.isEmpty()) {
                String msg = String.format("Failed to push to %s repository.", repository.getName());
                log.error(msg);
                for(RemoteRefUpdate ref : failedRefUpdates){
                    log.error("${ref}")
                }
                throw new IOException(msg);
            }
        } catch (GitAPIException e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * Deletes the repository on GitHub
     *
     * @param project the project
     */
    protected void cleanupGitHubRepository(Project project){
        GitHubApiClient apiClient = beanContext.getBean(GitHubApiClient)
        GitHubUser gitHubUser = apiClient.getUser(apiToken)
        apiClient.deleteRepository(apiToken, gitHubUser.login, project.name)
    }

    /**
     * Encrypts secret using libsodium library like recommended by GitHub.
     * See: https://docs.github.com/en/free-pro-team@latest/rest/reference/actions#create-or-update-a-repository-secret
     *
     * @param secret to encrypt
     * @param secretsKey repository public key used for encryption
     * @return base64 encoded encrypted secret
     */
    private static String encryptSecret(String secret, String secretsKey){
        Box.PublicKey pubKey = Box.PublicKey.fromBytes(Base64.decoder.decode(secretsKey.bytes))
        byte[] encrypted = Box.encryptSealed(secret.bytes, pubKey)
        return new String(Base64.encoder.encode(encrypted))
    }

    protected static Secret secretFromEnvVariable(String envVariable){
        return new Secret(envVariable, System.getenv(envVariable), null)
    }
}
