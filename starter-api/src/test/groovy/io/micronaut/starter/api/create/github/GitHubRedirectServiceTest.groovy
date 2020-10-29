package io.micronaut.starter.api.create.github

import io.micronaut.context.annotation.Property
import io.micronaut.http.HttpParameters
import io.micronaut.starter.api.RequestInfo
import io.micronaut.starter.api.create.github.client.v3.GitHubRepository
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification

import javax.inject.Inject

@Property(name = "micronaut.http.services.github-oauth.url", value =  "https://api.github.com")
@Property(name = "micronaut.starter.github.clientId", value =  "clientId")
@MicronautTest
class GitHubRedirectServiceTest extends Specification{

    @Inject GitHubRedirectService gitHubRedirectService

    void 'generates redirect to oauth github'(){
        given:
        def parameters = Spy(HttpParameters)
        parameters.names() >> ["lang", "build"]
        parameters.getAll("lang") >> Collections.singletonList("java")
        parameters.getAll("build") >> Collections.singletonList("maven")
        RequestInfo requestInfo = new RequestInfo("https://localhost:8080", "/github/default/foo", parameters, Locale.ENGLISH, "");

        when:
        URI redirectUri = gitHubRedirectService.constructOAuthRedirectUrl(requestInfo)

        then:
        redirectUri.query.contains('scope=user,repo')
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
