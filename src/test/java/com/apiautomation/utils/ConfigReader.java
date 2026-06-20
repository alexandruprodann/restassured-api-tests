package com.apiautomation.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input != null) {
                properties.load(input);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String get(String key) {
        String value = System.getProperty(key);
        if (hasText(value)) {
            return value;
        }

        value = System.getenv(toEnvironmentVariableName(key));
        if (hasText(value)) {
            return value;
        }

        return properties.getProperty(key);
    }

    public static String getOrDefault(String key, String defaultValue) {
        String value = get(key);
        return hasText(value) ? value : defaultValue;
    }

    public static String getRequired(String key) {
        String value = get(key);
        if (!hasText(value) || isPlaceholder(value)) {
            throw new IllegalStateException("Missing required configuration value: " + key
                    + ". Provide it in src/test/resources/config.properties, as a -D system property, or as "
                    + toEnvironmentVariableName(key) + " environment variable.");
        }
        return value;
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private static boolean isPlaceholder(String value) {
        return value.strip().startsWith("YOUR_");
    }

    private static String toEnvironmentVariableName(String key) {
        return key.toUpperCase().replace('.', '_');
    }
}
