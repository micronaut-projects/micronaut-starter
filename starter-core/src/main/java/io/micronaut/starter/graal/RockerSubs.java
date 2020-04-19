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
package io.micronaut.starter.graal;

import com.fizzed.rocker.runtime.PlainTextUnloadedClassLoader;
import com.oracle.svm.core.annotate.Substitute;
import com.oracle.svm.core.annotate.TargetClass;
import io.micronaut.core.annotation.Internal;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

@TargetClass(PlainTextUnloadedClassLoader.class)
@Internal
final class com_fizzed_rocker_runtime_PlainTextUnloadedClassLoader {

    @Substitute
    static public PlainTextUnloadedClassLoader load(ClassLoader sourceClassLoader, String classBinaryName, String charsetName)
            throws ClassNotFoundException, IllegalArgumentException, UnsupportedEncodingException, IllegalAccessException {

        // replaces standard Rocker behaviour which uses some dynamic classloading stuff
        // which is not supported in GraalVM
        Map<String, byte[]> fields = new LinkedHashMap<>();
        Class<?> aClass = sourceClassLoader.loadClass(classBinaryName);
        for (Field field : aClass.getDeclaredFields()) {
            if (field.isSynthetic()) {
                continue;
            }

            field.setAccessible(true);

            // field should be static
            String s = (String)field.get(null);

            byte[] bytes = s.getBytes(charsetName);

            fields.put(field.getName(), bytes);
        }
        return new PlainTextUnloadedClassLoader(
                classBinaryName,
                fields
        );

    }
}
