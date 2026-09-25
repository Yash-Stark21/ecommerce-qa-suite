package com.ecommerce.qa.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;

/**
 * BaseTest initializes and tears down Selenium WebDriver before/after each TestNG test method.
 * Fully compatible with both IntelliJ IDEA direct test runner (visible Chrome by default)
 * and Maven Surefire CLI (headless Chrome by default).
 */
public abstract class BaseTest {

    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() throws Exception {
        ChromeOptions options = new ChromeOptions();
        // Defaults to visible Chrome when clicked in IntelliJ IDEA; headless when run via 'mvn test'
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1440,900");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        driver.get(resolveStorefrontUri());
    }

    /**
     * Resolves webapp/index.html whether launched from Maven classpath,
     * IntelliJ module directory (ecommerce-qa-suite), or parent workspace directory (ISTQB).
     */
    private String resolveStorefrontUri() throws Exception {
        URL resourceUrl = getClass().getClassLoader().getResource("webapp/index.html");
        if (resourceUrl != null) {
            return Paths.get(resourceUrl.toURI()).toUri().toString();
        }

        String[] fallbackPaths = {
            "src/test/resources/webapp/index.html",
            "ecommerce-qa-suite/src/test/resources/webapp/index.html"
        };
        for (String relativePath : fallbackPaths) {
            File candidate = new File(relativePath);
            if (candidate.exists()) {
                return candidate.getAbsoluteFile().toURI().toString();
            }
        }
        throw new IllegalStateException("Could not locate src/test/resources/webapp/index.html");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
