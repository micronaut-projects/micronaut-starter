/*
 * Copyright 2017-2023 original authors
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
package io.micronaut.starter.feature.validation;

import io.micronaut.starter.application.Project;
import jakarta.inject.Singleton;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class ProjectNameValidator {

    private final Pattern packageName = Pattern.compile("(?!^abstract$|^abstract\\..*|.*\\.abstract\\..*|.*\\.abstract$|^assert$|^assert\\..*|.*\\.assert\\..*|.*\\.assert$|^boolean$|^boolean\\..*|.*\\.boolean\\..*|.*\\.boolean$|^break$|^break\\..*|.*\\.break\\..*|.*\\.break$|^byte$|^byte\\..*|.*\\.byte\\..*|.*\\.byte$|^case$|^case\\..*|.*\\.case\\..*|.*\\.case$|^catch$|^catch\\..*|.*\\.catch\\..*|.*\\.catch$|^char$|^char\\..*|.*\\.char\\..*|.*\\.char$|^class$|^class\\..*|.*\\.class\\..*|.*\\.class$|^const$|^const\\..*|.*\\.const\\..*|.*\\.const$|^continue$|^continue\\..*|.*\\.continue\\..*|.*\\.continue$|^default$|^default\\..*|.*\\.default\\..*|.*\\.default$|^do$|^do\\..*|.*\\.do\\..*|.*\\.do$|^double$|^double\\..*|.*\\.double\\..*|.*\\.double$|^else$|^else\\..*|.*\\.else\\..*|.*\\.else$|^enum$|^enum\\..*|.*\\.enum\\..*|.*\\.enum$|^extends$|^extends\\..*|.*\\.extends\\..*|.*\\.extends$|^final$|^final\\..*|.*\\.final\\..*|.*\\.final$|^finally$|^finally\\..*|.*\\.finally\\..*|.*\\.finally$|^float$|^float\\..*|.*\\.float\\..*|.*\\.float$|^for$|^for\\..*|.*\\.for\\..*|.*\\.for$|^goto$|^goto\\..*|.*\\.goto\\..*|.*\\.goto$|^if$|^if\\..*|.*\\.if\\..*|.*\\.if$|^implements$|^implements\\..*|.*\\.implements\\..*|.*\\.implements$|^import$|^import\\..*|.*\\.import\\..*|.*\\.import$|^instanceof$|^instanceof\\..*|.*\\.instanceof\\..*|.*\\.instanceof$|^int$|^int\\..*|.*\\.int\\..*|.*\\.int$|^interface$|^interface\\..*|.*\\.interface\\..*|.*\\.interface$|^long$|^long\\..*|.*\\.long\\..*|.*\\.long$|^native$|^native\\..*|.*\\.native\\..*|.*\\.native$|^new$|^new\\..*|.*\\.new\\..*|.*\\.new$|^package$|^package\\..*|.*\\.package\\..*|.*\\.package$|^private$|^private\\..*|.*\\.private\\..*|.*\\.private$|^protected$|^protected\\..*|.*\\.protected\\..*|.*\\.protected$|^public$|^public\\..*|.*\\.public\\..*|.*\\.public$|^return$|^return\\..*|.*\\.return\\..*|.*\\.return$|^short$|^short\\..*|.*\\.short\\..*|.*\\.short$|^static$|^static\\..*|.*\\.static\\..*|.*\\.static$|^strictfp$|^strictfp\\..*|.*\\.strictfp\\..*|.*\\.strictfp$|^super$|^super\\..*|.*\\.super\\..*|.*\\.super$|^switch$|^switch\\..*|.*\\.switch\\..*|.*\\.switch$|^synchronized$|^synchronized\\..*|.*\\.synchronized\\..*|.*\\.synchronized$|^this$|^this\\..*|.*\\.this\\..*|.*\\.this$|^throw$|^throw\\..*|.*\\.throw\\..*|.*\\.throw$|^throws$|^throws\\..*|.*\\.throws\\..*|.*\\.throws$|^transient$|^transient\\..*|.*\\.transient\\..*|.*\\.transient$|^try$|^try\\..*|.*\\.try\\..*|.*\\.try$|^void$|^void\\..*|.*\\.void\\..*|.*\\.void$|^volatile$|^volatile\\..*|.*\\.volatile\\..*|.*\\.volatile$|^while$|^while\\..*|.*\\.while\\..*|.*\\.while$)(^(?:[a-z_]+(?:\\d*[a-zA-Z_]*)*)(?:\\.[a-z_]+(?:\\d*[a-zA-Z_]*)*)*$)");

    public void validate(Project project) {
        Matcher matcher = packageName.matcher(project.getPackageName());
        if (!matcher.find()) {
            throw new IllegalArgumentException("Invalid package name: %s".formatted(project.getPackageName()));
        }
    }

}
