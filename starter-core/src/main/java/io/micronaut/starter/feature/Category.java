package io.micronaut.starter.feature;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.core.naming.Named;

/**
 * Category to which a feature belongs to.
 *
 * @author Álvaro Sánchez-Mariscal
 * @since 2.0.0
 */
public enum Category implements Named {

    AWS                 ("Amazon Web Services"),
    CACHE               ("Cache"),
    CIRCUIT_BREAKER     ("Circuit Breaker"),
    CONFIGURATION       ("Configuration"),
    DATABASE            ("Database"),
    DEV_TOOLS           ("Development Tools"),
    DISTRIBUTED_CONFIG  ("Distributed Configuration"),
    DOCUMENTATION       ("Documentation"),
    FUNCTION            ("Cloud Function"),
    HTTP_CLIENT         ("HTTP Client"),
    HTTP_SERVER         ("HTTP Server"),
    LOGGING             ("Logging"),
    MANAGEMENT          ("Management"),
    MESSAGING           ("Messaging"),
    PACKAGING           ("Packaging"),
    RSS                 ("RSS Feeds"),
    SEARCH              ("Search Engine"),
    SECURITY            ("Security"),
    SERVICE_DISCOVERY   ("Service Discovery"),
    TRACING             ("Distributed Tracing"),
    VALIDATION          ("Validation"),
    VIEW                ("View Rendering"),

    OTHER               ("Other");

    private final String name;

    Category(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }

}
