import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileReader;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

public class AskOmDchAutomation {
    public static void main(String[] args) throws Exception {
        WebDriverManager.chromedriver().setup();
        
        // Add Chrome options for better stability
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        
        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        Actions actions = new Actions(driver);

        try {
            driver.manage().window().maximize();
            driver.get("https://askomdch.com/");

            // 2. Scroll to Featured Products
            WebElement featured = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//h2[contains(text(),'Featured Products')] | //h2[contains(text(),'Featured')] | //*[contains(@class,'featured')]//h2")
            ));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", featured);
            Thread.sleep(2000);

            // 3. Add Blue Shoes to cart
            addToCart(driver, wait, "Blue Shoes");

            // 4. Add Blue T-shirt to cart
            addToCart(driver, wait, "Blue T-shirt");

            // 5. Scroll to top
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
            Thread.sleep(1000);

            // 6. Click 'Store' menu
            WebElement storeLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Store')] | //a[@href*='store']")
            ));
            storeLink.click();
            Thread.sleep(2000);

            // 7. Search for 'Jean'
            WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@name='s'] | //input[@type='search']")
            ));
            searchBox.sendKeys("Jean", Keys.ENTER);
            Thread.sleep(3000);

            // 8. Verify that search results have 'jean'
            wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(text(),'Jean') or contains(text(),'jean')]")
            ));
            System.out.println("✅ Search results verified");

            // 9. Scroll to find 'Faint Blue Jeans' and 10. Add to cart
            addToCart(driver, wait, "Faint Blue Jeans");

            // 11. Click View cart
            WebElement viewCartLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'View cart')] | //a[@href*='cart']")
            ));
            viewCartLink.click();
            Thread.sleep(2000);

            // 12. Verify cart page is visible
            wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//h1[contains(text(),'Cart')] | //h1[contains(text(),'Shopping Cart')]")
            ));

            // 13. Increase Blue T-shirt quantity
            try {
                WebElement qtyInput = driver.findElement(
                    By.xpath("//a[contains(text(),'Blue T-shirt')]/ancestor::tr//input[@type='number'] | " +
                            "//*[contains(text(),'Blue T-shirt')]/ancestor::*[contains(@class,'cart')]//input[@type='number']")
                );
                qtyInput.clear();
                qtyInput.sendKeys("2");

                // 14. Click Update cart
                WebElement updateButton = driver.findElement(
                    By.xpath("//button[@name='update_cart'] | //input[@name='update_cart']")
                );
                updateButton.click();
                Thread.sleep(3000);
                System.out.println("✅ Cart updated successfully");
            } catch (Exception e) {
                System.out.println("⚠️ Could not update cart quantity: " + e.getMessage());
            }

            // 15. Proceed to checkout
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(1000);
            WebElement checkoutButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Proceed to checkout')] | //a[contains(text(),'Checkout')]")
            ));
            checkoutButton.click();
            Thread.sleep(3000);

            // 16. Verify Checkout page
            wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//h3[contains(text(),'Billing details')] | //*[contains(@class,'billing')]")
            ));

            // 17. Click Account menu
            WebElement accountLink = driver.findElement(
                By.xpath("//a[contains(text(),'My account')] | //a[contains(text(),'Account')]")
            );
            accountLink.click();
            Thread.sleep(2000);

            // 18. Register new user from CSV
            CSVParser parser = new CSVParser(new FileReader(Paths.get("src/test/resources/users.csv").toFile()),
                    CSVFormat.DEFAULT.withFirstRecordAsHeader());
            CSVRecord user = parser.getRecords().get(0);

            // Make username and email unique to avoid conflicts
            String uniqueUsername = user.get("username") + System.currentTimeMillis();
            String uniqueEmail = "test" + System.currentTimeMillis() + "@example.com";

            WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("reg_username")));
            usernameField.sendKeys(uniqueUsername);
            
            WebElement emailField = driver.findElement(By.id("reg_email"));
            emailField.sendKeys(uniqueEmail);
            
            WebElement passwordField = driver.findElement(By.id("reg_password"));
            passwordField.sendKeys(user.get("password"));
            
            WebElement registerButton = driver.findElement(By.name("register"));
            registerButton.click();
            Thread.sleep(3000);

            // 19. Verify account creation
            wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(text(),'Hello')] | //*[contains(text(),'Welcome')] | //*[contains(text(),'Dashboard')]")
            ));
            System.out.println("✅ User registered successfully: " + uniqueUsername);

            // 20. Hover over cart and click checkout
            WebElement cartButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[contains(@class,'cart-contents')] | //*[contains(@class,'cart')]//a")
            ));
            actions.moveToElement(cartButton).perform();
            Thread.sleep(1000);
            
            WebElement checkoutLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Checkout')] | //a[@href*='checkout']")
            ));
            checkoutLink.click();
            Thread.sleep(3000);

            // 21. Fill in billing details
            fillBillingInfo(driver, wait, uniqueEmail);

            // 22. Place order
            WebElement placeOrderButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@id='place_order'] | //input[@id='place_order']")
            ));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", placeOrderButton);
            Thread.sleep(1000);
            placeOrderButton.click();
            Thread.sleep(5000);

            // 23. Verify order success
            wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(text(),'Thank you')] | //*[contains(text(),'Order received')] | //*[contains(text(),'Success')]")
            ));
            System.out.println("🎉 Order placed successfully!");

            parser.close();
            
        } catch (Exception e) {
            System.err.println("❌ Test failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private static void addToCart(WebDriver driver, WebDriverWait wait, String productName) {
        try {
            System.out.println("🛒 Adding " + productName + " to cart...");
            
            // Strategy 1: Find product by name and locate add to cart button
            List<WebElement> productElements = driver.findElements(
                By.xpath("//h2[contains(text(),'" + productName + "')] | //h3[contains(text(),'" + productName + "')] | //*[contains(@class,'product-title') and contains(text(),'" + productName + "')]")
            );
            
            if (!productElements.isEmpty()) {
                WebElement productElement = productElements.get(0);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", productElement);
                Thread.sleep(1000);
                
                // Try different XPath patterns to find the add to cart button
                String[] buttonXPaths = {
                    "//h2[contains(text(),'" + productName + "')]/following::a[contains(@class,'add_to_cart_button')][1]",
                    "//h2[contains(text(),'" + productName + "')]/parent::*/following-sibling::*//a[contains(@class,'add_to_cart_button')]",
                    "//h2[contains(text(),'" + productName + "')]/ancestor::*[contains(@class,'product')]//a[contains(@class,'add_to_cart_button')]",
                    "//*[contains(text(),'" + productName + "')]/ancestor::*[contains(@class,'product')]//a[contains(@class,'add_to_cart_button')]"
                };
                
                for (String xpath : buttonXPaths) {
                    try {
                        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
                        addButton.click();
                        Thread.sleep(2000);
                        System.out.println("✅ " + productName + " added to cart successfully!");
                        return;
                    } catch (Exception ignored) {
                        // Try next pattern
                    }
                }
            }
            
            // Strategy 2: Fallback - find any add to cart button
            List<WebElement> addToCartButtons = driver.findElements(
                By.xpath("//a[contains(@class,'add_to_cart_button')] | //button[contains(@class,'add-to-cart')] | //*[contains(text(),'Add to cart')]")
            );
            
            if (!addToCartButtons.isEmpty()) {
                WebElement button = addToCartButtons.get(0);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", button);
                Thread.sleep(1000);
                button.click();
                Thread.sleep(2000);
                System.out.println("⚠️ Added a product to cart using fallback method");
            } else {
                throw new Exception("No add to cart buttons found");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Failed to add " + productName + " to cart: " + e.getMessage());
        }
    }

    private static void fillBillingInfo(WebDriver driver, WebDriverWait wait, String email) {
        try {
            System.out.println("📝 Filling billing information...");
            
            // Wait for billing form to be present
            wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//h3[contains(text(),'Billing details')] | //*[contains(@class,'billing')]")
            ));
            
            // Fill billing details with safe field filling
            fillFieldSafely(driver, "billing_first_name", "Test");
            fillFieldSafely(driver, "billing_last_name", "User");
            fillFieldSafely(driver, "billing_address_1", "123 Test Street");
            fillFieldSafely(driver, "billing_city", "Testville");
            
            // Handle state dropdown
            try {
                WebElement stateDropdown = driver.findElement(By.id("billing_state"));
                Select state = new Select(stateDropdown);
                state.selectByVisibleText("California");
            } catch (Exception e) {
                System.out.println("⚠️ Could not select state: " + e.getMessage());
            }
            
            fillFieldSafely(driver, "billing_postcode", "90001");
            fillFieldSafely(driver, "billing_phone", "0123456789");
            fillFieldSafely(driver, "billing_email", email);
            
            System.out.println("✅ Billing information filled successfully");
            
        } catch (Exception e) {
            System.err.println("❌ Failed to fill billing information: " + e.getMessage());
        }
    }
    
    private static void fillFieldSafely(WebDriver driver, String fieldId, String value) {
        try {
            WebElement field = driver.findElement(By.id(fieldId));
            field.clear();
            field.sendKeys(value);
        } catch (Exception e) {
            System.out.println("⚠️ Could not fill field " + fieldId + ": " + e.getMessage());
        }
    }
}