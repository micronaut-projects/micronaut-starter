/*
 * Copyright 2017-2020 original authors
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
package io.micronaut.starter.feature.aws;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.MultiProjectFeature;
import io.micronaut.starter.feature.aws.template.cdkappstack;
import io.micronaut.starter.feature.aws.template.cdkgradle;
import io.micronaut.starter.feature.aws.template.cdkjson;
import io.micronaut.starter.feature.aws.template.cdkmain;
import io.micronaut.starter.feature.aws.template.cdkparentpom;
import io.micronaut.starter.feature.aws.template.cdkpom;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.Template;
import jakarta.inject.Singleton;

@Singleton
public class Cdk implements MultiProjectFeature {

    public static final String INFRA_MODULE = "infra";

    @Override
    @NonNull
    public String getName() {
        return "aws-cdk";
    }

    @Override
    public String getTitle() {
        return "AWS CDK (Cloud Development Kit)";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds Amazon Web Services CDK (Cloud Development Kit) support";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT || applicationType == ApplicationType.FUNCTION;
    }

    @Override
    public String getCategory() {
        return Category.CLOUD;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addTemplate("cdk-json", new RockerTemplate(INFRA_MODULE, "cdk.json", cdkjson.template()));
        generatorContext.addTemplate("cdk-main", new RockerTemplate(INFRA_MODULE, "src/main/java/{packagePath}/Main.java", cdkmain.template(generatorContext.getProject())));
        generatorContext.addTemplate("cdk-appstack", new RockerTemplate(INFRA_MODULE, "src/main/java/{packagePath}/AppStack.java", cdkappstack.template(generatorContext.getProject())));
        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            generatorContext.addTemplate("cdk-build", new RockerTemplate(INFRA_MODULE, generatorContext.getBuildTool().getBuildFileName(), cdkpom.template(generatorContext.getProject())));
            generatorContext.addTemplate("cdk-module-build", new RockerTemplate(Template.ROOT, generatorContext.getBuildTool().getBuildFileName(), cdkparentpom.template(generatorContext.getProject())));
        } else {
            generatorContext.addTemplate("cdk-build", new RockerTemplate(INFRA_MODULE, generatorContext.getBuildTool().getBuildFileName(), cdkgradle.template(generatorContext.getProject())));
        }
    }
}
