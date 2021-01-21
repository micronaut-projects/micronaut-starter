package io.micronaut.starter.test.github

import groovy.util.logging.Slf4j
import io.micronaut.context.annotation.Property
import io.micronaut.starter.application.Project
import io.micronaut.starter.client.github.v3.GitHubApiClient
import io.micronaut.starter.client.github.v3.GitHubRepository
import io.micronaut.starter.client.github.v3.GitHubSecret
import io.micronaut.starter.client.github.v3.GitHubSecretsPublicKey
import io.micronaut.starter.client.github.v3.GitHubUser
import io.micronaut.starter.client.github.v3.GitHubWorkflowRun
import io.micronaut.starter.client.github.v3.GitHubWorkflowRuns
import io.micronaut.starter.feature.github.workflows.Secret
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.util.GitHubUtil
import org.apache.tuweni.crypto.sodium.Box
import spock.lang.Shared
import spock.util.concurrent.PollingConditions

import java.util.stream.Collectors

/**
 * Spec class for testing of GitHub workflows. If the creation of secrets on demand is requested, then the LibSodium
 * have to be installed in the system. For more information <a href="https://docs.github.com/en/free-pro-team@latest/actions/reference/encrypted-secrets">encrypted secrets</a>
 */
@Slf4j
@Property
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

        GitHubRepository repo = apiClient.getRepository(apiToken, owner.login, project.name)
        if(repo){
            log.debug("Deleting repository ${project.name}")
            apiClient.deleteRepository(apiToken, owner.login, project.name)
        }
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

        GitHubUtil.initAndPushToGitHubRepository(repository, owner, dir.toPath(), token)
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
        new PollingConditions(initialDelay: 3*60, delay: 30).within(timeout) {
            GitHubWorkflowRun run = apiClient.getWorkflowRun(apiToken, owner.login, project.name, id)
            assert run.status == "completed" : "workflow status should be 'completed' but got: ${run.status}"
            assert run.conclusion == "success" : "workflow conclusion should be 'success' but got: ${run.conclusion}"
        }
    }

    /**
     * Deletes the repository on GitHub
     *
     * @param project the project
     */
    protected void cleanupGitHubRepository(Project project){
// TODO: remove
//        GitHubApiClient apiClient = beanContext.getBean(GitHubApiClient)
//        GitHubUser gitHubUser = apiClient.getUser(apiToken)
//        apiClient.deleteRepository(apiToken, gitHubUser.login, project.name)
    }

    protected static Secret secretFromEnvVariable(String envVariable){
        return new Secret(envVariable, System.getenv(envVariable), null)
    }

    /**
     * Encrypts secret using libsodium library like recommended by GitHub.
     * See: https://docs.github.com/en/free-pro-team@latest/rest/reference/actions#create-or-update-a-repository-secret
     *
     * @param secret     to encrypt
     * @param secretsKey repository public key used for encryption
     * @return base64 encoded encrypted secret
     */
    static String encryptSecret(String secret, String secretsKey) {
        Box.PublicKey pubKey = Box.PublicKey.fromBytes(Base64.getDecoder().decode(secretsKey.getBytes()))
        byte[] encrypted = Box.encryptSealed(secret.getBytes(), pubKey)
        return new String(Base64.getEncoder().encode(encrypted))
    }

    static List<String> splitAfterNChars(String input, int len){
        return new ArrayList<String>(Arrays.asList(input.split(String.format("(?<=\\G.{%1\$d})", len))))
    }

    static String convertPrivateKeyOnelinerToMultiline(String oneliner) {
        if(oneliner.contains("\n")){
            return oneliner
        }

        // Get the key content without
        String code = oneliner.replace('-----BEGIN PRIVATE KEY-----', '')
                .replace('-----END PRIVATE KEY-----', '')
                .replace(' ', '')

        // split content by 64 charts
        List<String> multiline = splitAfterNChars(code, 64)

        // add header and footer
        multiline.add(0, '-----BEGIN PRIVATE KEY-----')
        multiline.add('-----END PRIVATE KEY-----')
        return multiline.stream().collect(Collectors.joining("\n"))
    }
}
