# Leaves Module Test Automation Framework

Test automation framework for the ESS (Employee Self Service) Leaves module, built with Java, Selenium WebDriver, JUnit 5, and RestAssured.

## Project Structure

```
mercans-assignment/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/mercans/
│   │   │       ├── config/
│   │   │       │   └── Config.java
│   │   │       ├── pages/
│   │   │       │   ├── BasePage.java
│   │   │       │   ├── LoginPage.java
│   │   │       │   ├── LeavesPage.java
│   │   │       │   └── WelcomePage.java
│   │   │       └── utils/
│   │   │           ├── DriverFactory.java
│   │   │           └── DateUtils.java
│   │   └── resources/
│   │       └── config.properties
│   └── test/
│       ├── java/
│       │   └── com/mercans/tests/
│       │       ├── api/
│       │       │   └── APILoginTest.java
│       │       └── ui/
│       │           ├── BaseTest.java
│       │           ├── LoginTest.java
│       │           ├── LeaveRequestTest.java
│       │           ├── LeaveRegulationsTest.java
│       │           └── ManagerWorkflowTest.java
│       └── testdata/
├── .env
├── pom.xml
├── .gitignore
└── README.md
```

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Chrome browser (or Firefox/Edge if preferred)

## Setup Instructions

1. **Clone the repository** (or extract the project)

2. **Install dependencies**:
   ```bash
   mvn clean install
   ```

3. **Configure test environment**:
   - **Credentials Setup**: Copy `.env.example` to `.env` and fill in your actual credentials
     ```bash
     cp .env.example .env
     ```
   - Edit `.env` file with your test credentials:
     - Manager username and password
     - Employee username and password
   - **Important**: 
     - The `.env` file is in `.gitignore` and will NOT be committed
     - Never commit actual credentials to the repository
   - Other settings (URLs, timeouts) are in `src/main/resources/config.properties`

## Running Tests

### Run all tests:
```bash
mvn test
```

### Run specific test class:
```bash
mvn test -Dtest=LoginTest
```

### Run UI tests only:
```bash
mvn test -Dtest="LoginTest,LeaveRequestTest,LeaveRegulationsTest,ManagerWorkflowTest"
```

### Run API tests only:
```bash
mvn test -Dtest=APILoginTest
```

### Run tests in headless mode (default):
```bash
mvn test
```
Tests run in headless mode by default (no visible browser window).

### Run tests with visible browser (for debugging):
```bash
mvn test -Dheadless=false
```

### Run with specific browser:
Update `DriverFactory.BrowserType` in `BaseTest.java` or pass as system property.

## Test Reports

### Generate Allure Report:
```bash
mvn test
mvn allure:report
```

### Serve Allure Report:
```bash
mvn allure:serve
```
This will automatically generate and open the Allure report in your default browser.

### View Allure Report:
After running `mvn allure:report`, the HTML report will be generated in `target/site/allure-maven/index.html`

**Allure Report Features:**
- Beautiful HTML reports with test history
- Screenshots attached on test failures
- Test execution timeline
- Grouped by test classes and packages
- Detailed test steps and logs

## Framework Features

### Page Object Model (POM)
- Uses `@FindBy` annotations for element locators
- PageFactory initialization for lazy loading
- BasePage with common utilities

### Dynamic Waits
- Explicit waits using WebDriverWait
- Replaces hardcoded `Thread.sleep()` calls
- Supports page load, element visibility, navigation waits

### Test Organization
- Tests organized into `ui` and `api` packages
- Base test class for common setup/teardown
- Test data separation

### Utilities
- **DriverFactory**: Creates WebDriver instances for different browsers
- **DateUtils**: Utility methods for date generation and manipulation
- **Config**: Centralized configuration management

## Configuration

### Environment Variables (.env file)
The `.env` file contains sensitive credentials and is NOT committed to version control.

**Required variables:**
- `MANAGER_USERNAME` - Manager login username
- `MANAGER_PASSWORD` - Manager login password
- `EMPLOYEE_USERNAME` - Employee login username
- `EMPLOYEE_PASSWORD` - Employee login password
- `MANAGER_NAME`, `MANAGER_ID` - Manager metadata
- `EMPLOYEE_NAME`, `EMPLOYEE_ID`, `EMPLOYEE_MANAGER_ID` - Employee metadata

**Setup:**
1. Copy `.env.example` to `.env`
2. Fill in your actual credentials
3. The `.env` file is automatically ignored by git

### Application Configuration (config.properties)
Edit `src/main/resources/config.properties` to configure:
- Application base URL
- Wait timeouts (implicit and explicit)
- Screenshot settings
- Non-sensitive user metadata

## Best Practices

1. **No hardcoded waits**: Use explicit waits for all wait operations
2. **Page Object Model**: All page interactions through page classes
3. **Separation of concerns**: UI and API tests in separate packages
4. **Configuration management**: 
   - Credentials in `.env` file (not committed)
   - Other settings in `config.properties`
5. **Security**: Never commit `.env` file or credentials to version control
6. **Headless mode**: Tests run in headless mode by default for CI/CD compatibility

## Troubleshooting

### Browser driver issues
The framework uses WebDriverManager to automatically download browser drivers. Ensure you have internet connectivity on first run.

### Test failures
- Check browser version compatibility
- Verify credentials in `.env` file exist and are correct
- Ensure `.env` file is in the project root directory
- Review test logs in `target/surefire-reports/`
- Check Allure reports for detailed failure information

### Credentials not loading
- Ensure `.env` file exists in project root
- Verify `.env` file has correct format (KEY=VALUE, no spaces around =)
- Check that variable names match exactly (case-sensitive)
- If `.env` is missing, the framework will show a warning but continue with defaults

### Tests opening browser windows
- Tests run in headless mode by default
- To see browser for debugging: `mvn test -Dheadless=false`
- To ensure headless: `mvn test -Dheadless=true` or just `mvn test`
