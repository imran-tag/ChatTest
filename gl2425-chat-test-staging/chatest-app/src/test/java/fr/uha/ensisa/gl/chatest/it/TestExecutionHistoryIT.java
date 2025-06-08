package fr.uha.ensisa.gl.chatest.it;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.net.URL;
import java.time.Duration;
import java.util.List;

public class TestExecutionHistoryIT {
    
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
        
        // Check for selenium.remote.browser property
        String remoteProperty = System.getProperty("selenium.remote.browser");
        System.out.println("selenium.remote.browser property: " + remoteProperty);
        
        // Detect CI environment
        boolean isCI = detectCIEnvironment();
        System.out.println("CI Environment detected: " + isCI);
        
        if (isCI || "firefox".equals(remoteProperty)) {
            System.out.println("Using Remote WebDriver (CI mode)");
            setupRemoteWebDriver();
        } else {
            System.out.println("Using Local WebDriver (Development mode)");
            setupLocalFirefoxDriver();
        }
        
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        System.out.println("WebDriver setup completed successfully");
    }
    
    private static boolean detectCIEnvironment() {
        return System.getenv("GITLAB_CI") != null || 
               "true".equals(System.getenv("CI")) ||
               new File("/.dockerenv").exists() ||
               "root".equals(System.getProperty("user.name"));
    }
    
    private static void setupRemoteWebDriver() {
        try {
            // Configure Firefox options for remote execution
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--disable-extensions");
            options.addArguments("--disable-web-security");
            
            // Set capabilities for better compatibility
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setBrowserName("firefox");
            capabilities.setCapability("browserName", "firefox");
            capabilities.setCapability("version", "");
            capabilities.setCapability("platform", "LINUX");
            capabilities.setCapability("acceptSslCerts", true);
            capabilities.setCapability("acceptInsecureCerts", true);
            options.merge(capabilities);
            
            String seleniumUrl = "http://selenium:4444/wd/hub";
            System.out.println("Connecting to Selenium Remote at: " + seleniumUrl);
            
            // Create RemoteWebDriver with retries
            boolean connected = false;
            Exception lastException = null;
            
            for (int attempt = 1; attempt <= 3; attempt++) {
                try {
                    System.out.println("Connection attempt " + attempt + "/3");
                    
                    // Verify Selenium is ready
                    URL statusUrl = new URL(seleniumUrl + "/status");
                    java.net.HttpURLConnection conn = (java.net.HttpURLConnection) statusUrl.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    conn.connect();
                    
                    if (conn.getResponseCode() == 200) {
                        System.out.println("Selenium status check passed");
                    } else {
                        System.out.println("Selenium status check failed: " + conn.getResponseCode());
                        continue;
                    }
                    
                    // Create the WebDriver
                    driver = new RemoteWebDriver(new URL(seleniumUrl), options);
                    
                    // Test the connection by getting session info
                    String sessionId = ((RemoteWebDriver) driver).getSessionId().toString();
                    System.out.println("✓ Remote WebDriver session created: " + sessionId);
                    
                    connected = true;
                    break;
                    
                } catch (Exception e) {
                    System.err.println("Attempt " + attempt + " failed: " + e.getMessage());
                    lastException = e;
                    
                    if (driver != null) {
                        try {
                            driver.quit();
                        } catch (Exception cleanup) {
                            // Ignore cleanup errors
                        }
                        driver = null;
                    }
                    
                    if (attempt < 3) {
                        try {
                            Thread.sleep(5000); // Wait 5 seconds before retry
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                }
            }
            
            if (!connected) {
                throw new RuntimeException("Failed to connect to Selenium after 3 attempts. Last error: " + 
                    (lastException != null ? lastException.getMessage() : "Unknown"));
            }
            
        } catch (Exception e) {
            System.err.println("Failed to initialize remote WebDriver: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Remote WebDriver initialization failed", e);
        }
    }
    
    private static void setupLocalFirefoxDriver() {
        String ext = System.getProperty("os.name", "")
                .toLowerCase().startsWith("win") ? ".exe" : "";
        String geckodrivername = "geckodriver" + ext;
        Collection<String> pathes = new ArrayList<>();
        
        for (String source : new String[] {
                System.getProperty("PATH"),
                System.getenv().get("Path"),
                System.getenv().get("PATH") }) {
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
        
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--headless");
        
        driver = new FirefoxDriver(options);
        System.out.println("Successfully initialized local Firefox WebDriver");
    }

    @AfterAll
    public static void shutdownWebDriver() {
        if (driver != null) {
            try {
                driver.quit();
                System.out.println("WebDriver quit successfully");
            } catch (Exception e) {
                System.err.println("Error during WebDriver quit: " + e.getMessage());
            }
            driver = null;
        }
    }

    public static String getBaseUrl() {
        return "http://" + host + ":" + port + "/";
    }

    @BeforeEach
    public void waitForApplication() {
        // Give the application a moment to be ready
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test
    @DisplayName("Test that history page loads correctly")
    public void testHistoryPageLoads() {
        System.out.println("Testing history page load...");
        
        String url = getBaseUrl() + "test/history";
        System.out.println("Navigating to: " + url);
        
        driver.get(url);
        
        String title = driver.getTitle();
        System.out.println("Page title: " + title);
        assertTrue(title.contains("Test Execution History") || title.contains("History"), 
                  "History page should contain correct title. Actual title: " + title);
        
        WebElement heading = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//h1[contains(text(), 'Test Execution History') or contains(text(), 'History')]")));
        assertNotNull(heading, "History page should contain main heading");
        
        System.out.println("✓ History page loads correctly");
    }

    @Test
    @DisplayName("Test that test list page loads correctly")
    public void testTestListPageLoads() {
        System.out.println("Testing test list page load...");
        
        String url = getBaseUrl() + "test/list";
        System.out.println("Navigating to: " + url);
        
        driver.get(url);
        
        String title = driver.getTitle();
        System.out.println("Page title: " + title);
        assertTrue(title.contains("Test List") || title.contains("Test"), 
                  "Test list page should contain title. Actual title: " + title);
        
        WebElement heading = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//h1[contains(text(), 'Test List') or contains(text(), 'Test')]")));
        assertNotNull(heading, "Test list page should contain main heading");
        
        System.out.println("✓ Test list page loads correctly");
    }

    @Test
    @DisplayName("Test that root redirects to test list")
    public void testRootRedirectsToTestList() {
        System.out.println("Testing root redirect...");
        
        driver.get(getBaseUrl());
        
        wait.until(ExpectedConditions.urlContains("test/list"));
        
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL after redirect: " + currentUrl);
        assertTrue(currentUrl.contains("test/list"), 
                  "Root should redirect to test list. Current URL: " + currentUrl);
        
        System.out.println("✓ Root redirects correctly");
    }
}