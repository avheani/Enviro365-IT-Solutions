import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.List;

public class ImprovedSeleniumTest {
    
    public static void main(String[] args) {
        // Setup Chrome driver automatically
        WebDriverManager.chromedriver().setup();
        
        // Configure Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        
        WebDriver driver = new ChromeDriver(options);
        
        try {
            // Navigate to the website
            driver.get("https://askomdch.com/");
            driver.manage().window().maximize();
            
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            
            // 1. Wait for page to load completely
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
            
            // 2. Scroll to Featured Products section
            scrollToFeaturedProducts(driver, wait);
            
            // 3. Add products to cart
            addProductToCart(driver, wait, "Blue Shoes");
            addProductToCart(driver, wait, "Blue T-shirt");
            
            System.out.println("Test completed successfully!");
            
        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close the browser
            driver.quit();
        }
    }
    
    /**
     * Scroll to the Featured Products section
     */
    private static void scrollToFeaturedProducts(WebDriver driver, WebDriverWait wait) {
        try {
            WebElement featuredSection = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//h2[contains(text(),'Featured Products')] | //h2[contains(text(),'Featured')] | //*[contains(@class,'featured')]//h2")
            ));
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", featuredSection);
            Thread.sleep(2000); // Wait for smooth scroll to complete
            
            System.out.println("Scrolled to Featured Products section");
            
        } catch (Exception e) {
            System.err.println("Could not find Featured Products section: " + e.getMessage());
            // Try scrolling down manually
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight/2);");
        }
    }
    
    /**
     * Add a specific product to cart
     */
    private static void addProductToCart(WebDriver driver, WebDriverWait wait, String productName) {
        try {
            System.out.println("Attempting to add " + productName + " to cart...");
            
            // Strategy 1: Find by product title and locate add to cart button
            List<WebElement> productElements = driver.findElements(
                By.xpath("//h2[contains(text(),'" + productName + "')] | //h3[contains(text(),'" + productName + "')] | //*[contains(@class,'product-title') and contains(text(),'" + productName + "')]")
            );
            
            if (!productElements.isEmpty()) {
                WebElement productElement = productElements.get(0);
                
                // Scroll to product
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", productElement);
                Thread.sleep(1000);
                
                // Find add to cart button near this product
                WebElement addToCartButton = findAddToCartButton(driver, wait, productElement);
                
                if (addToCartButton != null) {
                    addToCartButton.click();
                    System.out.println(productName + " added to cart successfully!");
                    Thread.sleep(2000); // Wait for cart update
                    return;
                }
            }
            
            // Strategy 2: Find by data attributes or other selectors
            List<WebElement> addToCartButtons = driver.findElements(
                By.xpath("//a[contains(@class,'add_to_cart_button')] | //button[contains(@class,'add-to-cart')] | //*[contains(text(),'Add to cart')]")
            );
            
            if (!addToCartButtons.isEmpty()) {
                // Try clicking the first available add to cart button
                WebElement button = addToCartButtons.get(0);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", button);
                Thread.sleep(1000);
                button.click();
                System.out.println("Added a product to cart (fallback method)");
                Thread.sleep(2000);
            } else {
                System.err.println("No add to cart buttons found for " + productName);
            }
            
        } catch (Exception e) {
            System.err.println("Failed to add " + productName + " to cart: " + e.getMessage());
        }
    }
    
    /**
     * Find the add to cart button associated with a product element
     */
    private static WebElement findAddToCartButton(WebDriver driver, WebDriverWait wait, WebElement productElement) {
        try {
            // Look for add to cart button in various locations relative to the product
            String[] xpathPatterns = {
                ".//following::a[contains(@class,'add_to_cart_button')][1]",
                ".//ancestor::*[contains(@class,'product')]//a[contains(@class,'add_to_cart_button')]",
                ".//parent::*/following-sibling::*//a[contains(@class,'add_to_cart_button')]",
                ".//following::button[contains(text(),'Add to cart')][1]",
                ".//ancestor::*[contains(@class,'product')]//button[contains(text(),'Add to cart')]"
            };
            
            for (String xpath : xpathPatterns) {
                try {
                    WebElement button = productElement.findElement(By.xpath(xpath));
                    if (button.isDisplayed() && button.isEnabled()) {
                        return button;
                    }
                } catch (Exception ignored) {
                    // Try next pattern
                }
            }
            
            return null;
            
        } catch (Exception e) {
            return null;
        }
    }
}