/*
 * Copyright 2017-2022 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.aws.cdk.function;

import io.micronaut.starter.application.ApplicationType;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.Function;

/**
 * Micronaut Function.
 * @author Sergio del Amo
 * @since 3.4.0
 */
public final class MicronautFunction {

    private static final String HANDLER = "io.micronaut.function.aws.proxy.MicronautLambdaHandler";

    private MicronautFunction() {

    }

    /**
     *
     * @param applicationType DEFAULT or FUNCTION
     * @param graalVMNative Whether the function is going to be distributed as a GraalVM Native Image.
     * @param scope Scope
     * @param id function id
     * @return Function Builder
     */
    public static Function.Builder create(final ApplicationType applicationType,
                                          final boolean graalVMNative,
                                          final software.constructs.Construct scope,
                                          final java.lang.String id) {
        switch (applicationType) {
            case DEFAULT:
                return Function.Builder.create(scope, id)
                        .handler(HANDLER)
                        .runtime(runtime(graalVMNative));
            case FUNCTION:
                return Function.Builder.create(scope, id)
                        .runtime(runtime(graalVMNative));
            default:
                throw new IllegalArgumentException("Please, specify application type DEFAULT or FUNCTION");
        }
    }

    private static Runtime runtime(boolean graalVMNative) {
        return graalVMNative ? Runtime.PROVIDED_AL2 : Runtime.JAVA_11;
    }

}
