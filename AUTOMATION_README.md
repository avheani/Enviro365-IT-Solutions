# E-commerce Automation Test for askomdch.com

This project contains comprehensive Selenium WebDriver automation tests for the askomdch.com e-commerce website, covering the complete user journey from product selection to order completion.

## Files Overview

- `AskOmDchAutomation.java` - Fixed version of your original automation script
- `AskOmDchAutomationFixed.java` - Enhanced version with additional robustness features
- `src/test/resources/users.csv` - CSV file containing user data for registration
- `pom.xml` - Maven configuration with all required dependencies

## Key Issues Fixed

### 1. **XPath Selector Issues**
- **Original Problem**: The XPath `//h2[contains(text(), 'productName')]/following-sibling::a[contains(@class,'add_to_cart_button')]` was too restrictive
- **Fix**: Added multiple fallback XPath patterns and strategies to locate add-to-cart buttons

### 2. **User Registration Conflicts**
- **Original Problem**: Using static usernames would cause "user already exists" errors
- **Fix**: Added timestamp-based unique usernames and emails

### 3. **Insufficient Wait Strategies**
- **Original Problem**: Fixed waits and potential timing issues
- **Fix**: Improved explicit waits and added appropriate sleep statements

### 4. **Missing Error Handling**
- **Original Problem**: Script would fail completely on any single step failure
- **Fix**: Added comprehensive try-catch blocks and fallback strategies

### 5. **Element Location Robustness**
- **Original Problem**: Single XPath patterns that might not work across different page layouts
- **Fix**: Multiple XPath alternatives and fallback element location strategies

## Test Workflow

The automation performs these steps:

1. **🌐 Navigate** to askomdch.com
2. **📜 Scroll** to Featured Products section
3. **👟 Add Blue Shoes** to cart
4. **👕 Add Blue T-shirt** to cart
5. **🏪 Navigate** to Store page
6. **🔍 Search** for "Jean" products
7. **✔️ Verify** search results contain "Jean"
8. **👖 Add Faint Blue Jeans** to cart
9. **🛒 View** shopping cart
10. **🔢 Update** Blue T-shirt quantity to 2
11. **💳 Proceed** to checkout
12. **👤 Register** new user account (from CSV data)
13. **📝 Fill** billing information
14. **🛍️ Place** order
15. **🎉 Verify** order success

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Chrome browser installed
- Internet connection

## Setup Instructions

1. **Install Java and Maven**:
   ```bash
   # Ubuntu/Debian
   sudo apt update
   sudo apt install openjdk-11-jdk maven
   
   # CentOS/RHEL
   sudo yum install java-11-openjdk-devel maven
   
   # macOS (with Homebrew)
   brew install openjdk@11 maven
   ```

2. **Verify installations**:
   ```bash
   java -version
   mvn -version
   ```

3. **Install project dependencies**:
   ```bash
   mvn clean install
   ```

## Running the Tests

### Option 1: Run the fixed original version
```bash
mvn compile exec:java -Dexec.mainClass="AskOmDchAutomation"
```

### Option 2: Run the enhanced version
```bash
mvn compile exec:java -Dexec.mainClass="AskOmDchAutomationFixed"
```

### Option 3: Compile and run manually
```bash
# Compile the project
mvn compile

# Run the original fixed version
java -cp target/classes:$(mvn dependency:build-classpath -Dmdep.outputFile=/dev/stdout -q) AskOmDchAutomation

# Run the enhanced version
java -cp target/classes:$(mvn dependency:build-classpath -Dmdep.outputFile=/dev/stdout -q) AskOmDchAutomationFixed
```

## CSV Data Format

The `src/test/resources/users.csv` file contains user registration data:

```csv
username,email,password
testuser123,testuser123@example.com,TestPassword123!
johndoe,john.doe@test.com,SecurePass456!
janedoe,jane.doe@test.com,MyPassword789!
```

**Note**: The automation automatically appends timestamps to usernames and emails to ensure uniqueness.

## Key Improvements Made

### 1. **Robust Element Selection**
```java
// Multiple XPath strategies for finding add-to-cart buttons
String[] buttonXPaths = {
    "//h2[contains(text(),'" + productName + "')]/following::a[contains(@class,'add_to_cart_button')][1]",
    "//h2[contains(text(),'" + productName + "')]/parent::*/following-sibling::*//a[contains(@class,'add_to_cart_button')]",
    "//h2[contains(text(),'" + productName + "')]/ancestor::*[contains(@class,'product')]//a[contains(@class,'add_to_cart_button')]"
};
```

### 2. **Enhanced Chrome Options**
```java
ChromeOptions options = new ChromeOptions();
options.addArguments("--disable-blink-features=AutomationControlled");
options.addArguments("--user-agent=Mozilla/5.0...");
```

### 3. **Unique User Generation**
```java
String uniqueUsername = user.get("username") + System.currentTimeMillis();
String uniqueEmail = "test" + System.currentTimeMillis() + "@example.com";
```

### 4. **Safe Field Filling**
```java
private static void fillFieldSafely(WebDriver driver, String fieldId, String value) {
    try {
        WebElement field = driver.findElement(By.id(fieldId));
        field.clear();
        field.sendKeys(value);
    } catch (Exception e) {
        System.out.println("⚠️ Could not fill field " + fieldId + ": " + e.getMessage());
    }
}
```

## Expected Output

When running successfully, you'll see output like:
```
🌐 Navigating to askomdch.com...
📜 Scrolling to Featured Products section...
🛒 Adding Blue Shoes to cart...
✅ Blue Shoes added to cart successfully!
🛒 Adding Blue T-shirt to cart...
✅ Blue T-shirt added to cart successfully!
🏪 Navigating to Store...
🔍 Searching for 'Jean'...
✔️ Search results verified
🛒 Adding Faint Blue Jeans to cart...
✅ Faint Blue Jeans added to cart successfully!
🛒 Viewing cart...
🔢 Updating Blue T-shirt quantity...
✅ Cart updated successfully
💳 Proceeding to checkout...
👤 Registering new user...
✅ User registered successfully: testuser1231703123456789
📝 Filling billing information...
✅ Billing information filled successfully
🛍️ Completing checkout...
🎉 Order placed successfully!
✅ Automation test completed successfully!
```

## Troubleshooting

### Common Issues and Solutions

1. **ChromeDriver Issues**:
   - The script uses WebDriverManager for automatic driver management
   - Ensure Chrome browser is installed and up to date

2. **Element Not Found**:
   - Website structure may have changed
   - Check if product names exist on the current website
   - The script includes multiple fallback strategies

3. **User Registration Fails**:
   - Username might already exist (script handles this with timestamps)
   - Check CSV file format and content

4. **Timeout Exceptions**:
   - Increase wait times in WebDriverWait
   - Check internet connection speed
   - Verify website accessibility

5. **Cart/Checkout Issues**:
   - Products might be out of stock
   - Payment processing might require real payment details
   - Some e-commerce sites block automated purchases

### Debug Mode

To run in visible mode (not headless), modify the Chrome options:
```java
// Remove or comment out headless mode
// options.addArguments("--headless");
```

### Logging

The script includes comprehensive logging with emojis for easy tracking:
- 🌐 Navigation steps
- 🛒 Shopping cart operations
- ✅ Successful operations
- ⚠️ Warnings
- ❌ Errors

## Dependencies Used

- **Selenium WebDriver 4.15.0**: Browser automation
- **WebDriverManager 5.6.2**: Automatic driver management
- **Apache Commons CSV 1.10.0**: CSV file processing
- **Chrome WebDriver**: Browser control

## Notes

- Tests are designed to be robust with multiple fallback strategies
- The script handles dynamic content and timing issues
- User data is automatically made unique to avoid conflicts
- Comprehensive error handling ensures partial test completion even if some steps fail
- The enhanced version (`AskOmDchAutomationFixed.java`) includes additional robustness features

## Contributing

When modifying the tests:
1. Always include fallback strategies for element location
2. Add appropriate wait conditions
3. Include error handling and logging
4. Test with different network conditions
5. Verify compatibility with website changes