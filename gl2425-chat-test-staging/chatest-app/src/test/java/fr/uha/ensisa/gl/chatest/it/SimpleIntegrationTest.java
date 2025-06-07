import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled("Disabled on CI because no Chrome available")  
public class SimpleIntegrationTest {
    /*
    private static WebDriver driver;

    @BeforeAll
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @AfterAll
    public static void teardownClass() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void homepageLoads() {
        driver.get("http://localhost:8080/");
        assertTrue(driver.getTitle().toLowerCase().contains("test") ||
                   driver.getPageSource().toLowerCase().contains("test"),
                   "Homepage should contain the word 'test'");
    }*/
}
