/*
 * Copyright 2017-2024 original authors
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
package io.micronaut.starter.feature;

/**
 * Some third party features apply annotation processors that are not compatible with Kotlin Symbol Processing (KSP)
 * for Kotlin language projects. They require using the kapt compiler plugin (Kapt) instead. Note that Maven
 * projects always use Kapt since Maven isn't compatible with KSP.
 *
 * @see io.micronaut.starter.feature.build.Kapt
 * @see KotlinSymbolProcessing
 * @see RequireKaptFeatureValidator
 */
public interface RequireKaptFeature extends Feature {
}
