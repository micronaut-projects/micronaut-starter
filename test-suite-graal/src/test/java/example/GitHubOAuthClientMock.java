package example;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.starter.client.github.oauth.AccessToken;
import io.micronaut.starter.client.github.oauth.GitHubOAuthClient;
import jakarta.inject.Singleton;

@Replaces(GitHubOAuthClient.class)
@Singleton
public class GitHubOAuthClientMock implements GitHubOAuthClient {

    @Override
    public AccessToken accessToken(String clientId, String clientSecret, String code, String state) {
        return new AccessToken("tokenTypeTest", "scopeTest", "accessTokenTest");
    }
}
