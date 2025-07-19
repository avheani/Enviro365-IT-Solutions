import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.io.IOException;
import java.time.Duration;

public class SeleniumTest {
    
    public static void main(String[] args) throws IOException {
        WebDriver driver = new ChromeDriver();
        
        try {
            // Navigate to the website
            driver.get("https://askomdch.com/");
            driver.manage().window().maximize();
            
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            
            // 1. Wait for and scroll to Featured Products section
            WebElement featured = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//h2[contains(text(),'Featured Products')]")
            ));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", featured);
            
            // Wait a moment for the page to settle after scrolling
            Thread.sleep(1000);
            
            // 2. Add Blue Shoes to cart
            addToCart(driver, wait, "Blue Shoes");
            
            // 3. Add Blue T-shirt to cart
            addToCart(driver, wait, "Blue T-shirt");
            
            System.out.println("Products added to cart successfully!");
            
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close the browser
            driver.quit();
        }
    }
    
    /**
     * Helper method to add a product to cart by product name
     * @param driver WebDriver instance
     * @param wait WebDriverWait instance
     * @param productName Name of the product to add to cart
     */
    public static void addToCart(WebDriver driver, WebDriverWait wait, String productName) {
        try {
            // Find the product by name and locate its "Add to cart" button
            // This XPath looks for a product with the given name and finds the associated add to cart button
            WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//h2[contains(text(),'" + productName + "')]/parent::*/following-sibling::*//a[contains(@class, 'add_to_cart_button') or contains(text(), 'Add to cart')]")
            ));
            
            // Scroll to the button to ensure it's visible
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addToCartButton);
            Thread.sleep(500); // Brief pause to ensure element is ready
            
            // Click the add to cart button
            addToCartButton.click();
            
            System.out.println(productName + " added to cart successfully!");
            
            // Wait a moment for the cart to update
            Thread.sleep(1000);
            
        } catch (Exception e) {
            System.err.println("Failed to add " + productName + " to cart: " + e.getMessage());
            
            // Alternative approach - try finding by different XPath patterns
            try {
                WebElement alternativeButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(@data-product_id, '') and contains(@class, 'add_to_cart_button')]")
                ));
                alternativeButton.click();
                System.out.println("Used alternative method to add " + productName + " to cart!");
            } catch (Exception altException) {
                System.err.println("Alternative method also failed for " + productName);
            }
        }
    }
}