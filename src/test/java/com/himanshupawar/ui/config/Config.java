package com.himanshupawar.ui.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Resolves framework configuration with a fixed precedence:
 * JVM system property (-Dkey=value) &gt; environment variable &gt; classpath
 * config.properties &gt; hardcoded default. Nothing in this framework hardcodes
 * a filesystem path or an environment-specific value — the old version had
 * both (a Windows chromedriver path, a hardcoded test-data path).
 */
public final class Config {

    private static final Properties PROPS = load();

    private Config() {
    }

    private static Properties load() {
        Properties props = new Properties();
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not read config.properties from classpath", e);
        }
        return props;
    }

    private static String resolve(String key, String envVar, String defaultValue) {
        String fromSystemProperty = System.getProperty(key);
        if (fromSystemProperty != null && !fromSystemProperty.isBlank()) {
            return fromSystemProperty;
        }
        String fromEnv = System.getenv(envVar);
        if (fromEnv != null && !fromEnv.isBlank()) {
            return fromEnv;
        }
        return PROPS.getProperty(key, defaultValue);
    }

    public static String baseUrl() {
        return resolve("baseUrl", "DDF_BASE_URL", "https://www.saucedemo.com");
    }

    public static String browser() {
        return resolve("browser", "DDF_BROWSER", "chrome");
    }

    public static boolean headless() {
        return Boolean.parseBoolean(resolve("headless", "DDF_HEADLESS", "true"));
    }

    public static int explicitWaitSeconds() {
        return Integer.parseInt(resolve("explicitWaitSeconds", "DDF_EXPLICIT_WAIT_SECONDS", "10"));
    }
}
