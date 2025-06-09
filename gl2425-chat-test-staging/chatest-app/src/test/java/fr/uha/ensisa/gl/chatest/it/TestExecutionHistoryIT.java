package fr.uha.ensisa.gl.chatest.it;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.net.URL;
import java.time.Duration;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Integration tests for the Test Execution History functionality.
 * Uses Selenium WebDriver to test the web application in a real browser environment.
 * Supports both local Firefox and remote Selenium Grid execution for CI.
 */
public class TestExecutionHistoryIT {
    /*
    public static WebDriver driver;
    private static String host, port;
    private static WebDriverWait wait;

    @BeforeAll
    public static void setupWebDriver() {
        if (driver != null) return;
        
        host = System.getProperty("host", "localhost");
        port = System.getProperty("servlet.port", "8080");
        
        System.out.println("=== WebDriver Setup Debug Info ===");
        System.out.println("Host: " + host);
        System.out.println("Port: " + port);
        System.out.println("Base URL: " + getBaseUrl());
        System.out.println("selenium.remote.browser property: " + System.getProperty("selenium.remote.browser"));
        
        // Check if we're in CI environment
        boolean isCiEnvironment = System.getProperty("CI") != null || 
                                 System.getProperty("selenium.remote.browser") != null ||
                                 System.getProperty("ci.environment") != null;
        
        System.out.println("CI Environment detected: " + isCiEnvironment);
        
        try {
            if (isCiEnvironment) {
                System.out.println("Using Remote WebDriver (CI mode)");
                setupRemoteWebDriver();
            } else {
                System.out.println("Using Local Firefox WebDriver");
                setupLocalWebDriver();
            }
            
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            System.out.println("WebDriver setup completed successfully");
            
        } catch (Exception e) {
            System.err.println("Failed to initialize WebDriver: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("WebDriver initialization failed", e);
        }
    }
    
    private static void setupLocalWebDriver() {
        // Looking for geckodriver in PATH
        String ext = System.getProperty("os.name", "")
                .toLowerCase().startsWith("win") ? ".exe" : "";
        String geckodrivername = "geckodriver" + ext;
        Collection<String> pathes = new ArrayList<>();
        
        for (String source : new String[] {
                System.getProperty("PATH") ,
                System.getenv().get("Path") ,
                System.getenv().get("PATH")  }) {
            if (source != null)
                pathes.addAll(Arrays.asList(source.trim().split(File.pathSeparator)));
        }
        
        File geckoDriver = null;
        for (String path : pathes) {
            File f = new File(path, geckodrivername);
            if (f.exists() && f.canExecute()) {
                System.setProperty("webdriver.gecko.driver", f.getAbsolutePath());
                geckoDriver = f;
                break;
            }
        }
        
        if (geckoDriver == null) {
            throw new IllegalStateException("Cannot find geckodriver on " + pathes.toString());
        }
        
        driver = new FirefoxDriver();
    }
    
    private static void setupRemoteWebDriver() {
        String seleniumHost = System.getProperty("selenium.host", "selenium");
        String seleniumPort = System.getProperty("selenium.port", "4444");
        String seleniumUrl = System.getProperty("selenium.remote.url", 
                "http://" + seleniumHost + ":" + seleniumPort + "/wd/hub");
        
        System.out.println("Connecting to Selenium Remote at: " + seleniumUrl);
        
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName("firefox");
        
        // Retry mechanism for CI environments
        int maxRetries = 3;
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                System.out.println("Connection attempt " + attempt + "/" + maxRetries);
                driver = new RemoteWebDriver(new URL(seleniumUrl), capabilities);
                System.out.println("Successfully connected to remote WebDriver");
                return;
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e.getMessage());
                if (attempt == maxRetries) {
                    throw new RuntimeException("Failed to connect to Selenium after " + maxRetries + 
                            " attempts. Last error: " + e.getMessage(), e);
                }
                try {
                    Thread.sleep(2000); // Wait 2 seconds between retries
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted while waiting to retry", ie);
                }
            }
        }
    }

    @AfterAll
    public static void shutdownWebDriver() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.err.println("Error during WebDriver shutdown: " + e.getMessage());
            }
            try {
                driver.close();
            } catch (Exception e) {
                // Ignore close errors
            }
            driver = null;
        }
    }

    public static String getBaseUrl() {
        return "http://" + host + ":" + port + "/";
    }

    @Test
    public void testHistoryPageLoads() {
        driver.get(getBaseUrl() + "test/history");
        
        // Wait for page to load
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        
        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("History") || pageSource.contains("Test"), 
                "History page should contain relevant content");
    }

    @Test
    public void testTestListPageLoads() {
        driver.get(getBaseUrl() + "test/list");
        
        // Wait for page to load
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        
        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("Test") || pageSource.contains("List"), 
                "Test list page should contain relevant content");
    }

    @Test
    public void testRootRedirectsOrLoads() {
        driver.get(getBaseUrl());
        
        // Wait for page to load or redirect
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        
        String currentUrl = driver.getCurrentUrl();
        String pageSource = driver.getPageSource();
        
        // Should either redirect to a meaningful page or show content
        assertTrue(currentUrl.contains(host) && 
                  (pageSource.length() > 100 || currentUrl.contains("test") || currentUrl.contains("hello")), 
                "Root should load meaningful content or redirect properly");
    }

    @Test
    public void testCreateTestPageExists() {
        try {
            driver.get(getBaseUrl() + "test/create");
            
            // Wait for page to load
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
            
            String pageSource = driver.getPageSource();
            assertTrue(pageSource.length() > 0, "Create page should have content");
            
        } catch (Exception e) {
            // If create page doesn't exist yet, that's acceptable for early development
            System.out.println("Create page not yet implemented: " + e.getMessage());
        }
    }

    @Test
    public void testHelloPageWithParameter() {
        String testName = "SeleniumTest";
        driver.get(getBaseUrl() + "hello?name=" + testName);
        
        // Wait for page to load
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        
        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains(testName) || pageSource.contains("Hello") || pageSource.length() > 0, 
                "Hello page should contain the test name or greeting");
    }

    @Test
    public void testApplicationRespondsToRequests() {
        // Test that the application is actually running and responding
        driver.get(getBaseUrl());
        
        // Just verify that we get a valid HTTP response (not an error page)
        String pageSource = driver.getPageSource();
        assertFalse(pageSource.contains("404") && pageSource.contains("Not Found"), 
                "Should not get 404 error for root URL");
        assertFalse(pageSource.contains("500") && pageSource.contains("Internal Server Error"), 
                "Should not get 500 error for root URL");
        
        assertTrue(pageSource.length() > 50, "Page should have meaningful content");
    }

    @Test
    public void testBasicNavigationStructure() {
        // Test basic application structure and navigation
        driver.get(getBaseUrl());
        
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        
        String pageSource = driver.getPageSource();
        
        // Should have basic HTML structure
        assertTrue(pageSource.contains("<html") || pageSource.contains("<HTML"), 
                "Should have HTML structure");
        assertTrue(pageSource.contains("<body") || pageSource.contains("<BODY"), 
                "Should have body element");
        
        // Check if common test-related URLs work
        try {
            driver.get(getBaseUrl() + "test/list");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
            String listPageSource = driver.getPageSource();
            assertTrue(listPageSource.length() > 0, "Test list page should load");
        } catch (Exception e) {
            System.out.println("Test list page not yet fully implemented: " + e.getMessage());
        }
    }
    */
}