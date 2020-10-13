/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.api.create.github.client.v3;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

/**
 * GitHub User.
 *
 * @author Pavol Gressa
 * @see <a href="https://docs.github.com/en/rest/reference/users">Api reference users</a>
 * @since 2.2
 */
@Introspected
public class GitHubUser {
    private final String login;
    private final String email;
    private final String name;

    @JsonCreator
    public GitHubUser(
            @JsonProperty("login") String login,
            @JsonProperty("email")  String email,
            @JsonProperty("name") String name) {
        this.login = login;
        this.email = email;
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "GitHubUser{" +
                "login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
