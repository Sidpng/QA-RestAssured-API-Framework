# QA RestAssured API Testing Framework

![GitHub Stars](https://img.shields.io/github/stars/Sidpng/QA-RestAssured-API-Framework?style=social)
![Java Version](https://img.shields.io/badge/Java-17-blue)
![TestNG](https://img.shields.io/badge/TestNG-7.8.1-green)
![RestAssured](https://img.shields.io/badge/RestAssured-5.4.0-orange)

A **comprehensive, production-ready API testing framework** built with RestAssured, TestNG, and **integrated GenAI capabilities** for intelligent test case generation.

---

## 🎯 Purpose

This framework is designed to demonstrate **advanced API automation** with modern testing practices:
- ✅ RESTful API testing using RestAssured
- ✅ Fluent assertions with Hamcrest matchers
- ✅ GenAI-powered test case generation (Claude API integration)
- ✅ Centralized configuration & reusable components
- ✅ CI/CD ready with GitHub Actions

**Target APIs:** ReqRes (User management & registration endpoints)

---

## 📋 Key Features

| Feature | Description |
|---------|-------------|
| **Endpoint Object Model** | POM pattern adapted for API endpoints |
| **Base Test Class** | Reusable HTTP methods (GET, POST, PUT, DELETE) |
| **GenAI Integration** | Claude API for test case & edge case generation |
| **Comprehensive Assertions** | Hamcrest matchers for readable validations |
| **Extent Reports** | Beautiful HTML test reports |
| **Parameterized Tests** | TestNG data provider support |
| **Centralized Config** | Single source of truth for API settings |

---

## 🚀 Quick Start

### Prerequisites

- Java 17+
- Maven 3.8+
- ANTHROPIC_API_KEY environment variable (for GenAI features)

### Installation

```bash
# Clone the repository
git clone https://github.com/Sidpng/QA-RestAssured-API-Framework.git
cd QA-RestAssured-API-Framework

# Set up your Claude API key
export ANTHROPIC_API_KEY="sk-ant-your-key-here"

# Run tests
mvn clean test
```

### Set API Key (Windows PowerShell)

```powershell
$env:ANTHROPIC_API_KEY = "sk-ant-your-key-here"
mvn clean test
```

---

## 📁 Project Structure

```
QA-RestAssured-API-Framework/
├── src/test/java/com/qa/api/
│   ├── base/
│   │   └── BaseTest.java                    # Common setup & HTTP methods
│   ├── endpoints/
│   │   └── UserEndpoint.java               # Endpoint mappers
│   ├── models/
│   │   └── User.java                       # POJO models
│   ├── tests/
│   │   └── UserAPITest.java                # Test cases
│   ├── genai/
│   │   └── TestCaseGenerator.java          # Claude API integration
│   └── utils/
│       └── ReportingUtils.java             # Report utilities
├── src/test/resources/
│   ├── config.properties                   # Configuration
│   └── testng.xml                          # Test suite config
├── docs/
│   ├── FRAMEWORK_ARCHITECTURE.md           # Detailed design
│   └── API_TESTING_WITH_GENAI.md          # GenAI guide
├── pom.xml
└── README.md
```

---

## 🧪 Usage Examples

### Running All Tests

```bash
mvn clean test
```

### Running Specific Test Class

```bash
mvn clean test -Dtest=UserAPITest
```

### Running Specific Test Method

```bash
mvn clean test -Dtest=UserAPITest#testGetAllUsers
```

### Generate Test Cases with GenAI

```bash
mvn exec:java -Dexec.mainClass="com.qa.api.genai.TestCaseGenerator"
```

---

## 📚 Test Examples

### Example 1: Simple GET Request

```java
@Test
public void testGetAllUsers() {
    Response response = getRequest("/users");
    
    response.then()
        .statusCode(200)
        .body("data", notNullValue())
        .body("data.size()", greaterThan(0));
}
```

### Example 2: POST Request with Assertion

```java
@Test
public void testCreateUser() {
    String body = "{\n" +
            "  \"name\": \"Siddhartha\",\n" +
            "  \"job\": \"QA Engineer\"\n" +
            "}";
    
    Response response = postRequest("/users", body);
    
    response.then()
        .statusCode(201)
        .body("name", equalTo("Siddhartha"))
        .body("id", notNullValue());
}
```

### Example 3: GenAI-Powered Test Generation

```java
TestCaseGenerator generator = new TestCaseGenerator();

// Generate edge cases for email field
String edgeCases = generator.generateEdgeCases("email", "email");
System.out.println(edgeCases);

// Analyze response and get assertion suggestions
String response = getRequest("/users/1").getBody().asString();
String suggestions = generator.analyzeResponseAndSuggestAssertions(response);
System.out.println(suggestions);
```

---

## 🤖 GenAI Features

### 1. Intelligent Test Case Generation

```java
String testCases = generator.generateTestCases(
    "/users",
    "POST",
    "{\"name\": \"string\", \"email\": \"string\"}"
);
```

Generates:
- Happy path scenarios
- Edge cases (empty, null, max length)
- Error scenarios (400, 404, 500)
- Validation rules
- Performance considerations

### 2. Edge Case Discovery

```java
String edgeCases = generator.generateEdgeCases("name", "string");
```

Suggests:
- Boundary values
- Special characters
- Length limits
- Format variations
- Invalid inputs

### 3. Smart Assertion Recommendations

```java
String suggestions = generator.analyzeResponseAndSuggestAssertions(actualResponse);
```

Recommends:
- Critical fields to validate
- Data type checks
- Value range validations
- Hamcrest matcher suggestions

---

## 📊 Test Reports

After running tests, Extent Reports generates an HTML report:

```
target/ExtentReports/ExtentReport.html
```

Open in browser to see:
- Test execution summary
- Pass/fail breakdown
- Detailed logs for each test
- Screenshots (if enabled)
- Response/request details

---

## 🔌 Configuration

**config.properties:**

```properties
# API Configuration
base.uri=https://reqres.in
base.path=/api
content.type=application/json

# Timeout settings (milliseconds)
request.timeout=5000

# Logging
log.level=INFO

# GenAI Configuration
anthropic.api.key=${ANTHROPIC_API_KEY}
genai.model=claude-opus-4-1
```

---

## 🛠️ Development Workflow

### Adding a New Test

1. Create test class extending `BaseTest`
2. Use `@Test` annotation with description
3. Follow **AAA Pattern** (Arrange-Act-Assert)
4. Use generated helper methods (getRequest, postRequest, etc.)
5. Add assertions using Hamcrest matchers

```java
@Test(description = "Verify user creation with valid data")
public void testCreateNewUser() {
    // Arrange
    String payload = "{\"name\": \"Test User\", \"job\": \"QA\"}";
    
    // Act
    Response response = postRequest("/users", payload);
    
    // Assert
    response.then()
        .statusCode(201)
        .body("name", equalTo("Test User"));
}
```

### Using GenAI for Test Enhancement

```java
// Step 1: Run initial tests to understand response
Response response = postRequest("/users", payload);
String responseBody = response.getBody().asString();

// Step 2: Get GenAI suggestions
String improvements = generator.analyzeResponseAndSuggestAssertions(responseBody);

// Step 3: Enhance your assertions
response.then()
    .statusCode(201)
    // Add suggestions from GenAI
    .body("createdAt", notNullValue())
    .body("id", isA(Integer.class));
```

---

## 📈 Performance Metrics

Current test execution:
- **Total Tests:** 6 test cases
- **Average Execution Time:** ~2-3 seconds per test
- **CI/CD Integration:** GitHub Actions ready

---

## 🚦 CI/CD Integration

This framework is ready for GitHub Actions:

```yaml
# .github/workflows/api-tests.yml
- name: Run API Tests
  run: mvn clean test

- name: Publish Report
  if: always()
  uses: actions/upload-artifact@v3
  with:
    name: extent-report
    path: target/ExtentReports/
```

---

## 📖 Documentation

- **[Framework Architecture](./FRAMEWORK_ARCHITECTURE.md)** - Detailed design decisions
- **[API Testing with GenAI](./docs/API_TESTING_WITH_GENAI.md)** - Complete GenAI integration guide
- **[Test Execution Guide](./docs/TEST_EXECUTION_GUIDE.md)** - Running & debugging tests

---

## ✨ Latest Updates

**2026-06-22** *(Initial Release)*
- ✅ RestAssured framework setup with TestNG
- ✅ Base test class with reusable HTTP methods
- ✅ User API test cases for ReqRes (CRUD operations)
- ✅ GenAI integration with Claude API for test case generation
- ✅ Extensive documentation with examples
- ✅ Ready for CI/CD integration with GitHub Actions
- 📝 **Note:** Currently testing against ReqRes API; local database testing coming in v1.1

---

## 🤝 Contributing

Contributions are welcome! Please:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add some amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Siddhartha Upadhyay**
- 8+ years QA Professional
- Cognizant Senior Associate – QA
- GitHub: [@Sidpng](https://github.com/Sidpng)
- Email: razor9nfs@gmail.com

---

## 📞 Support

- **Questions?** Open an issue on [GitHub Issues](https://github.com/Sidpng/QA-RestAssured-API-Framework/issues)
- **Want to collaborate?** Let's connect on [LinkedIn](https://linkedin.com/in/sidpng)
- **Found a bug?** Report it with reproduction steps

---

**Last Updated:** June 22, 2026  
**Status:** Active & Maintained ✅

---

## 🎓 Learning Resources

- [RestAssured Documentation](https://rest-assured.io/)
- [TestNG Documentation](https://testng.org/)
- [Hamcrest Matchers](http://hamcrest.org/JavaHamcrest/tutorial)
- [Claude API Documentation](https://docs.anthropic.com/)

---

**Happy Testing! 🚀**
