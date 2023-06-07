package io.micronaut.starter.api.create.github

import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Requires
import io.micronaut.core.util.StringUtils
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.starter.client.github.oauth.AccessToken
import io.micronaut.starter.client.github.oauth.GitHubOAuthOperations
import io.micronaut.starter.client.github.v3.*
import io.micronaut.starter.util.GitHubUtil
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.transport.URIish
import spock.lang.PendingFeature
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class GitHubCreateControllerSpec extends Specification {

    void "returns redirect with error when repository exists"() {
        given:
        EmbeddedServer githubServer = ApplicationContext.run(EmbeddedServer, Collections.singletonMap("spec.name", "GitHubCreateControllerSpec"))
        Map<String, String> configuration = new HashMap<>(serverConfiguration(githubServer))
        configuration.put("micronaut.http.client.follow-redirects", StringUtils.FALSE)
        EmbeddedServer server = ApplicationContext.run(EmbeddedServer, configuration)
        HttpClient httpClient = server.applicationContext.createBean(HttpClient, server.URL)

        when:
        HttpResponse response = httpClient.toBlocking().exchange(HttpRequest.GET("/github/default/foo.bar.existing?code=123&state=123"))

        then:
        response.getStatus().code == 307
        response.header(HttpHeaders.LOCATION).contains("error")
        response.header(HttpHeaders.LOCATION).startsWith("https://micronaut.io/launch")

        cleanup:
        server.close()
        githubServer.close()
    }

    @PendingFeature
    void "returns redirect to launcher if redirectUri is configured"() {
        given:
        EmbeddedServer githubServer = ApplicationContext.run(EmbeddedServer, Collections.singletonMap("spec.name", "GitHubCreateControllerSpec"))
        Map<String, String> configuration = new HashMap<>(serverConfiguration(githubServer))
        configuration.put("micronaut.http.client.follow-redirects", StringUtils.FALSE)
        EmbeddedServer server = ApplicationContext.run(EmbeddedServer, configuration)
        HttpClient httpClient = server.applicationContext.createBean(HttpClient, server.URL)

        when:
        HttpResponse response = httpClient.toBlocking().exchange(HttpRequest.GET("/github/default/foo?code=123&state=123"))

        then:
        response.getStatus().code == 307
        response.header(HttpHeaders.LOCATION).contains("cloneUrl")
        response.header(HttpHeaders.LOCATION).contains("htmlUrl")
        response.header(HttpHeaders.LOCATION).contains("url")
        response.header(HttpHeaders.LOCATION).startsWith("https://micronaut.io/launch")

        cleanup:
        server.close()
        githubServer.close()
    }

    @PendingFeature
    void "returns github repository details when launcher missing"() {
        given:
        EmbeddedServer githubServer = ApplicationContext.run(EmbeddedServer, Collections.singletonMap("spec.name", "GitHubCreateControllerSpec"))
        Map<String, String> configuration = new HashMap<>(serverConfiguration(githubServer))
        configuration.put("micronaut.starter.redirectUri", "")
        EmbeddedServer server = ApplicationContext.run(EmbeddedServer, configuration)
        HttpClient httpClient = server.applicationContext.createBean(HttpClient, server.URL)

        when:
        GitHubCreateDTO dto = httpClient.toBlocking().retrieve(HttpRequest.GET("/github/default/foo?code=123&state=123"), GitHubCreateDTO.class)

        then:
        Path clonePath = Paths.get(new URL(dto.cloneUrl).toURI())
        Git bareRepo = Git.open(clonePath.toFile())
        List<RevCommit> commits = bareRepo.log().call().toList()
        commits.size() == 1
        commits.get(0).fullMessage == GitHubUtil.INIT_COMMIT_MESSAGE
        commits.get(0).authorIdent.name == "name"
        commits.get(0).authorIdent.emailAddress == "email"

        cleanup:
        server.close()
        githubServer.close()
        clonePath.toFile().deleteDir()
    }

    @Requires(property = 'spec.name', value = 'GitHubCreateControllerSpec')
    @Controller(value = "/")
    static class GitHubApiMockedController implements GitHubApiOperations {

        @Override
        @Post(value = "/user/repos", processes = [GitHubApiClient.GITHUB_V3_TYPE, MediaType.APPLICATION_JSON])
        GitHubRepository createRepository(
                @Header(HttpHeaders.AUTHORIZATION) String oauthToken,
                @Body GitHubRepository gitHubRepository) {
            Path temp = Files.createTempDirectory('test-github-create')
            Git repo = Git.init().setDirectory(temp.toFile()).setBare(true).call()
            URIish uri = new URIish(repo.getRepository().getDirectory().toURI().toURL())
            return new GitHubRepository(gitHubRepository.name, gitHubRepository.description, 'url', 'html_url', uri.toString())
        }

        @Override
        @Get(value = "/repos/{owner}/{repo}", processes = [GitHubApiClient.GITHUB_V3_TYPE])
        GitHubRepository getRepository(
                @Header(HttpHeaders.AUTHORIZATION) String oauthToken,
                @PathVariable String owner,
                @PathVariable String repo) {
            return repo == "existing" ? new GitHubRepository("existing", null) : null
        }

        @Override
        void deleteRepository(@Header(HttpHeaders.AUTHORIZATION) String oauthToken, @PathVariable String owner, @PathVariable String repo) {
            // no-op
        }

        @Override
        @Get(value = "/user", processes = [GitHubApiClient.GITHUB_V3_TYPE, MediaType.APPLICATION_JSON])
        GitHubUser getUser(@Header(HttpHeaders.AUTHORIZATION) String oauthToken) {
            return new GitHubUser("login", "email", "name")
        }

        @Override
        void createSecret(@Header(HttpHeaders.AUTHORIZATION) String oauthToken, @PathVariable String owner, @PathVariable String repo, @PathVariable String secretName, @Body GitHubSecret secret) {
            // no-op
        }

        @Override
        GitHubSecretsPublicKey getSecretPublicKey(@Header(HttpHeaders.AUTHORIZATION) String oauthToken, @PathVariable String owner, @PathVariable String repo) {
            return null
        }

        @Override
        GitHubWorkflowRuns listWorkflows(@Header(HttpHeaders.AUTHORIZATION) String oauthToken, @PathVariable String owner, @PathVariable String repo) {
            return null
        }

        @Override
        GitHubWorkflowRun getWorkflowRun(@Header(HttpHeaders.AUTHORIZATION) String oauthToken, @PathVariable String owner, @PathVariable String repo, @PathVariable Long runId) {
            return null
        }
    }

    @Requires(property = 'spec.name', value = 'GitHubCreateControllerSpec')
    @Controller("/login/oauth")
    static class GitHubOauthMockedController implements GitHubOAuthOperations {

        @Override
        @Post(value = "/access_token")
        AccessToken accessToken(
                @QueryValue("client_id") String clientId,
                @QueryValue("client_secret") String clientSecret,
                @QueryValue String code,
                @QueryValue String state) {
            return new AccessToken("foo", "repo,user", "123")
        }
    }

    private static Map<String, String> serverConfiguration(EmbeddedServer server) {
        return Map.of("micronaut.http.services.github-oauth.url", "http://localhost:" + server.port,
                "micronaut.http.services.github-api-v3.url", "http://localhost:" + server.port,
                "micronaut.starter.github.client-id", "clientId",
                "micronaut.starter.github.client-secret", "clientSecret")
    }
}
