package io.micronaut.starter.api;

import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.OperatingSystem;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserAgentParser {

    private static final Pattern[] WINDOWS_PATTERNS = new Pattern[] { Pattern.compile("Windows"), Pattern.compile("Win ?(95|98|3.1|NT|ME|2000)") };
    private static final Pattern[] MACOS_PATTERNS = new Pattern[] { Pattern.compile("(?:Mac[ +]?|; )OS[ +]X"), Pattern.compile("Darwin"), Pattern.compile("Mac_PowerPC") };
    private static final Pattern[] LINUX_PATTERNS = new Pattern[] { Pattern.compile("Linux"), Pattern.compile("Ubuntu|Kubuntu|CentOS|Slackware|Gentoo|openSUSE|SUSE|Red Hat|Fedora|Mageia|(?:Free|Open|Net|\\b)BSD") };
    private static final Pattern[] SOLARIS_PATTERNS = new Pattern[] { Pattern.compile("SunOS") };


    public static OperatingSystem getOperatingSystem(String userAgent) {
        if (StringUtils.isNotEmpty(userAgent)) {
            if (matches(WINDOWS_PATTERNS, userAgent)) {
                return OperatingSystem.WINDOWS;
            }
            if (matches(MACOS_PATTERNS, userAgent)) {
                return OperatingSystem.MACOS;
            }
            if (matches(LINUX_PATTERNS, userAgent)) {
                return OperatingSystem.LINUX;
            }
            if (matches(SOLARIS_PATTERNS, userAgent)) {
                return OperatingSystem.SOLARIS;
            }
        }
        return null;
    }

    private static boolean matches(Pattern[] patterns, String userAgent) {
        return Arrays.stream(patterns).map(pattern -> pattern.matcher(userAgent))
                .anyMatch(Matcher::find);
    }
}
