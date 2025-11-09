package com.mercans.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Config {
    private static Properties properties;
    private static Properties envProperties;
    private static Properties testDataProperties;
    private static final String CONFIG_FILE = "src/main/resources/config.properties";
    private static final String ENV_FILE = ".env";
    private static final String TESTDATA_FILE = "src/testdata/testdata.properties";

    static {
        loadProperties();
        loadEnvProperties();
        loadTestDataProperties();
    }

    private static void loadProperties() {
        properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(CONFIG_FILE);
            properties.load(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties: " + e.getMessage(), e);
        }
    }

    private static void loadEnvProperties() {
        envProperties = new Properties();
        try {
            if (Files.exists(Paths.get(ENV_FILE))) {
                FileInputStream fileInputStream = new FileInputStream(ENV_FILE);
                envProperties.load(fileInputStream);
                fileInputStream.close();
            } else {
                System.out.println("Warning: .env file not found. Using default values or config.properties");
            }
        } catch (IOException e) {
            System.out.println("Warning: Failed to load .env file: " + e.getMessage());
        }
    }

    private static void loadTestDataProperties() {
        testDataProperties = new Properties();
        try {
            if (Files.exists(Paths.get(TESTDATA_FILE))) {
                FileInputStream fileInputStream = new FileInputStream(TESTDATA_FILE);
                testDataProperties.load(fileInputStream);
                fileInputStream.close();
            } else {
                System.out.println("Warning: testdata.properties file not found.");
            }
        } catch (IOException e) {
            System.out.println("Warning: Failed to load testdata.properties file: " + e.getMessage());
        }
    }

    private static String getEnvProperty(String key, String defaultValue) {
        String value = envProperties.getProperty(key);
        return value != null ? value : defaultValue;
    }

    public static String getBaseUrl() {
        return properties.getProperty("base.url");
    }

    public static String getManagerUsername() {
        return getEnvProperty("MANAGER_USERNAME", properties.getProperty("manager.username"));
    }

    public static String getManagerPassword() {
        return getEnvProperty("MANAGER_PASSWORD", properties.getProperty("manager.password"));
    }

    public static String getManagerName() {
        return properties.getProperty("manager.name");
    }

    public static String getManagerId() {
        return properties.getProperty("manager.id");
    }

    public static String getEmployeeUsername() {
        return getEnvProperty("EMPLOYEE_USERNAME", properties.getProperty("employee.username"));
    }

    public static String getEmployeePassword() {
        return getEnvProperty("EMPLOYEE_PASSWORD", properties.getProperty("employee.password"));
    }

    public static String getEmployeeName() {
        return properties.getProperty("employee.name");
    }

    public static String getEmployeeId() {
        return properties.getProperty("employee.id");
    }

    public static String getEmployeeManagerId() {
        return properties.getProperty("employee.manager.id");
    }

    public static int getImplicitWait() {
        return Integer.parseInt(properties.getProperty("implicit.wait"));
    }

    public static int getExplicitWait() {
        return Integer.parseInt(properties.getProperty("explicit.wait"));
    }

    public static boolean isScreenshotOnFailure() {
        return Boolean.parseBoolean(properties.getProperty("screenshot.on.failure"));
    }

    public static String getInvalidUsername() {
        return testDataProperties.getProperty("invalid.username");
    }

    public static String getInvalidPassword() {
        return testDataProperties.getProperty("invalid.password");
    }

    public static String getLeaveNote() {
        return testDataProperties.getProperty("leave.note");
    }

    public static String getExpectedBalanceAfterWithdrawal() {
        return testDataProperties.getProperty("expected.balance.after.withdrawal");
    }
}
