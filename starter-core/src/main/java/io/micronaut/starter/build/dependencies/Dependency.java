package io.micronaut.starter.build.dependencies;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class Dependency {

    private Scope scope;
    private String groupId;
    private String artifactId;
    private String version;
    private boolean requiresLookup;
    private int order;
    private boolean annotationProcessorPriority;

    private Dependency(Scope scope, String groupId, String artifactId, String version, boolean requiresLookup, boolean annotationProcessorPriority, int order) {
        this.scope = scope;
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.requiresLookup = requiresLookup;
        this.annotationProcessorPriority = annotationProcessorPriority;
        this.order = order;
    }

    public Scope getScope() {
        return scope;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    @Nullable
    public String getVersion() {
        return version;
    }

    public int getOrder() {
        return order;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean requiresLookup() {
        return requiresLookup;
    }

    public Dependency resolved(Coordinate coordinate) {
        return new Dependency(scope, coordinate.getGroupId(), artifactId, coordinate.getVersion(), false, annotationProcessorPriority, order);
    }

    public boolean isAnnotationProcessorPriority() {
        return annotationProcessorPriority;
    }

    public static class Builder {

        private Scope scope;
        private String groupId;
        private String artifactId;
        private String version;
        private boolean requiresLookup;
        private int order = 0;
        private boolean template = false;
        private boolean annotationProcessorPriority = false;

        public Builder scope(@NonNull Scope scope) {
            if (template) {
                return copy().scope(scope);
            } else {
                this.scope = scope;
                return this;
            }
        }

        public Builder compile() {
            return scope(new Scope(Source.MAIN, Arrays.asList(Phase.COMPILATION, Phase.RUNTIME)));
        }

        public Builder compileOnly() {
            return scope(new Scope(Source.MAIN, Collections.singletonList(Phase.COMPILATION)));
        }

        public Builder runtime() {
            return scope(new Scope(Source.MAIN, Collections.singletonList(Phase.RUNTIME)));
        }

        public Builder test() {
            return scope(new Scope(Source.TEST, Arrays.asList(Phase.COMPILATION, Phase.RUNTIME)));
        }

        public Builder testCompileOnly() {
            return scope(new Scope(Source.TEST, Collections.singletonList(Phase.COMPILATION)));
        }

        public Builder testRuntime() {
            return scope(new Scope(Source.TEST, Collections.singletonList(Phase.RUNTIME)));
        }

        public Builder annotationProcessor() {
            return scope(new Scope(Source.MAIN, Collections.singletonList(Phase.ANNOTATION_PROCESSING)));
        }

        public Builder annotationProcessor(boolean requiresPriority) {
            this.annotationProcessorPriority = requiresPriority;
            return annotationProcessor();
        }

        public Builder testAnnotationProcessor() {
            return scope(new Scope(Source.TEST, Collections.singletonList(Phase.ANNOTATION_PROCESSING)));
        }

        public Builder testAnnotationProcessor(boolean requiresPriority) {
            this.annotationProcessorPriority = requiresPriority;
            return testAnnotationProcessor();
        }

        public Builder groupId(@Nullable String groupId) {
            if (template) {
                return copy().groupId(groupId);
            } else {
                this.groupId = groupId;
                return this;
            }
        }

        public Builder artifactId(@NonNull String artifactId) {
            if (template) {
                return copy().artifactId(artifactId);
            } else {
                this.artifactId = artifactId;
                return this;
            }
        }

        public Builder lookupArtifactId(@NonNull String artifactId) {
            if (template) {
                return copy().lookupArtifactId(artifactId);
            } else {
                this.artifactId = artifactId;
                this.requiresLookup = true;
                return this;
            }
        }

        public Builder version(@Nullable String version) {
            if (template) {
                return copy().version(version);
            } else {
                this.version = version;
                return this;
            }
        }

        public Builder order(int order) {
            if (template) {
                return copy().order(order);
            } else {
                this.order = order;
                return this;
            }
        }

        public Builder template() {
            this.template = true;
            return this;
        }

        public Dependency build() {
            Objects.requireNonNull(scope, "The dependency scope must be set");
            Objects.requireNonNull(artifactId, "The artifact id must be set");

            return new Dependency(scope, groupId, artifactId, version, requiresLookup, annotationProcessorPriority, order);
        }

        public DependencyCoordinate buildCoordinate() {
            Objects.requireNonNull(artifactId, "The artifact id must be set");

            return new DependencyCoordinate(groupId, artifactId, version, order);
        }

        private Builder copy() {
            Builder builder = new Builder().scope(scope);
            if (requiresLookup) {
                builder.lookupArtifactId(artifactId);
            } else {
                builder.groupId(groupId).artifactId(artifactId).version(version);
            }
            return builder.order(order);
        }
    }
}
