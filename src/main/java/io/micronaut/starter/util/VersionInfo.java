package io.micronaut.starter.util;

import io.micronaut.starter.MicronautStarter;

import java.io.IOException;
import java.util.Properties;

public class VersionInfo {

    public static String getVersion() {
        return "2.0.0.M1";
    }

    public static int getJavaVersion() {
        String version = System.getProperty("java.version");
        if (version.startsWith("1.")) {
            version = version.substring(2);
        }
        // Allow these formats:
        // 1.8.0_72-ea
        // 9-ea
        // 9
        // 9.0.1
        int dotPos = version.indexOf('.');
        int dashPos = version.indexOf('-');
        return Integer.parseInt(version.substring(0,
                dotPos > -1 ? dotPos : dashPos > -1 ? dashPos : version.length()));
    }

    public static String getJdkVersion() {
        String version = System.getProperty("java.version");
        int dotPos = version.indexOf('.');
        int dashPos = version.indexOf('-');

        if (version.startsWith("1.")) {
            dotPos += 2;
        }

        return version.substring(0,
                dotPos > -1 ? dotPos : dashPos > -1 ? dashPos : version.length());
    }

}
