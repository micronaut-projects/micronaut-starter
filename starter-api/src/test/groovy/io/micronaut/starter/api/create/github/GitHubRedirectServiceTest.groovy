package io.micronaut.starter.api.create.github

import io.micronaut.context.annotation.Property
import io.micronaut.http.HttpParameters
import io.micronaut.starter.api.RequestInfo
import io.micronaut.starter.client.github.v3.GitHubRepository
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification

import jakarta.inject.Inject

@Property(name = "micronaut.http.services.github-oauth.url", value =  "https://api.github.com")
@Property(name = "micronaut.starter.github.clientId", value =  "clientId")
@Property(name = "micronaut.starter.github.tokenPermissions", value =  "user,repo,workflow")
@MicronautTest
@spock.lang.IgnoreIf({ jvm.java16 }) // fails on java 16 due to spock bug
class GitHubRedirectServiceTest extends Specification{

    @Inject GitHubRedirectService gitHubRedirectService

    void 'generates redirect to oauth github'(){
        given:
        def parameters = Spy(HttpParameters)
        parameters.names() >> ["lang", "build"]
        parameters.getAll("lang") >> Collections.singletonList("java")
        parameters.getAll("build") >> Collections.singletonList("maven")
        RequestInfo requestInfo = new RequestInfo("https://localhost:8080",
                "/github/default/foo", parameters, Locale.ENGLISH, "")

        when:
        URI redirectUri = gitHubRedirectService.constructOAuthRedirectUrl(requestInfo)

        then:
        redirectUri.query.contains('scope=user,repo,workflow')
        redirectUri.query.contains('client_id=clientId')
        redirectUri.query.contains('state=')
        redirectUri.query.contains('github/default/foo')
        redirectUri.query.contains('lang=java')
        redirectUri.query.contains('build=maven')
    }

    void 'generates redirect to launcher'(){
        given:
        GitHubRepository repo = new GitHubRepository("name", "desc",
                "https://api.github/micronaut-projects/micronaut",
                "https://github.com/micronaut-projects/micronaut-starter",
                "https://github.com/micronaut-projects/micronaut-starter.git")

        when:
        URI redirectUri = gitHubRedirectService.constructLauncherRedirectUrl(repo)

        then:
        redirectUri.host == 'micronaut.io'
        redirectUri.query.contains("cloneUrl")
        redirectUri.query.contains("url")
        redirectUri.query.contains("htmlUrl")
    }
}
