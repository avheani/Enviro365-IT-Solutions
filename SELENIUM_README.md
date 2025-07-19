# Selenium WebDriver Test for askomdch.com

This project contains Selenium WebDriver tests for adding products to cart on the askomdch.com e-commerce website.

## Files

- `SeleniumTest.java` - Basic version of the test
- `ImprovedSeleniumTest.java` - Enhanced version with better error handling and WebDriverManager
- `pom.xml` - Maven configuration with required dependencies

## Issues Fixed in Your Original Code

1. **Duplicate variable declarations** - You had multiple `addToCartButton` declarations
2. **Incomplete method calls** - Missing proper method implementation
3. **Syntax errors** - Fixed various syntax issues
4. **XPath improvements** - Better XPath selectors for finding products and buttons
5. **Error handling** - Added proper try-catch blocks and fallback strategies

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Chrome browser installed
- Internet connection

## Setup and Installation

1. **Install Java and Maven** (if not already installed):
   ```bash
   # On Ubuntu/Debian
   sudo apt update
   sudo apt install openjdk-11-jdk maven
   
   # On CentOS/RHEL
   sudo yum install java-11-openjdk-devel maven
   ```

2. **Verify installations**:
   ```bash
   java -version
   mvn -version
   ```

3. **Install dependencies**:
   ```bash
   mvn clean install
   ```

## Running the Tests

### Option 1: Run the basic test
```bash
mvn compile exec:java -Dexec.mainClass="SeleniumTest"
```

### Option 2: Run the improved test
```bash
mvn compile exec:java -Dexec.mainClass="ImprovedSeleniumTest"
```

### Option 3: Compile and run manually
```bash
# Compile
mvn compile

# Run basic test
java -cp target/classes:$(mvn dependency:build-classpath -Dmdep.outputFile=/dev/stdout -q) SeleniumTest

# Run improved test
java -cp target/classes:$(mvn dependency:build-classpath -Dmdep.outputFile=/dev/stdout -q) ImprovedSeleniumTest
```

## Key Improvements in the Fixed Code

### 1. Proper Class Structure
- Complete class with all necessary imports
- Proper exception handling with try-catch-finally blocks
- Clean resource management (driver.quit() in finally block)

### 2. Enhanced Product Selection
- Multiple XPath strategies for finding products
- Fallback mechanisms if primary selectors fail
- Better error messages and logging

### 3. Robust Element Interaction
- Explicit waits instead of implicit waits
- Scroll into view before clicking elements
- Proper timing with Thread.sleep() for dynamic content

### 4. WebDriverManager Integration
- Automatic Chrome driver management
- Chrome options for better compatibility
- User agent configuration to avoid detection

## Troubleshooting

### Common Issues:

1. **ChromeDriver not found**:
   - The improved version uses WebDriverManager to handle this automatically
   - If using the basic version, download ChromeDriver manually

2. **Element not found**:
   - The website structure might have changed
   - Check if the product names "Blue Shoes" and "Blue T-shirt" exist on the site
   - Modify the XPath selectors if needed

3. **Timeout exceptions**:
   - Increase the wait time in WebDriverWait
   - Check your internet connection
   - Verify the website is accessible

### Debug Mode:
To see what's happening, you can add these Chrome options:
```java
options.addArguments("--disable-headless"); // Run in visible mode
options.addArguments("--start-maximized");
```

## Expected Output

When the test runs successfully, you should see output like:
```
Scrolled to Featured Products section
Attempting to add Blue Shoes to cart...
Blue Shoes added to cart successfully!
Attempting to add Blue T-shirt to cart...
Blue T-shirt added to cart successfully!
Test completed successfully!
```

## Notes

- The test assumes Chrome browser is installed
- The website structure may change over time, requiring XPath updates
- Tests include multiple fallback strategies for robustness
- The improved version is recommended for production use