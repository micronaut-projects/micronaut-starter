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
package io.micronaut.starter.options;

import com.fizzed.rocker.RockerModel;
import io.micronaut.starter.application.Project;

/**
 * Provides a {@link RockerModel} each language supported by {@link TestFramework#JUNIT}.
 * @author Sergio del Amo
 */
public abstract class JunitRockerModelProvider {

    private final Project project;

    public JunitRockerModelProvider(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return this.project;
    }

    /**
     *
     * @param language Selected language
     * @return A {@link RockerModel}
     * @throws IllegalArgumentException if the test combination is not handled
     */
    public RockerModel findJunitModel(Language language) {
        switch (language) {
            case JAVA:
                return javaJunit();
            case GROOVY:
                return groovyJunit();
            case KOTLIN:
                return kotlinJunit();
            default:
                throw new IllegalArgumentException("unable to find a junit RockerModel for lang: " + language.getName());
        }
    }

    /**
     *
     * @return {@link RockerModel} for {@link TestFramework#JUNIT} and {@link Language#JAVA}
     */
    public abstract RockerModel javaJunit();

    /**
     *
     * @return {@link RockerModel} for {@link TestFramework#JUNIT} and {@link Language#GROOVY}
     */
    public abstract RockerModel groovyJunit();

    /**
     *
     * @return {@link RockerModel} for {@link TestFramework#JUNIT} and {@link Language#KOTLIN}
     */
    public abstract RockerModel kotlinJunit();
}
