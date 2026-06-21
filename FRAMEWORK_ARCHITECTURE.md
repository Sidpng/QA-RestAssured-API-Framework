# RestAssured API Testing Framework - Architecture & Design

**Author:** Siddhartha Upadhyay  
**Version:** 1.0  
**Last Updated:** June 2026

---

## 📋 Overview

This framework is designed for **comprehensive API automation** using RestAssured + TestNG, with integrated **GenAI capabilities** for intelligent test case generation and coverage analysis.

### Key Principles:
- **Page Object Model (POM) Pattern** adapted for API endpoints (Endpoint Object Model)
- **Reusable Request/Response Handlers**
- **GenAI-Powered Test Case Generation** using Claude API
- **Centralized Configuration Management**
- **Detailed Reporting** with Extent Reports

---

## 🏗️ Project Structure

```
QA-RestAssured-API-Framework/
├── src/
│   ├── test/
│   │   ├── java/com/qa/api/
│   │   │   ├── base/
│   │   │   │   └── BaseTest.java              # Common setup & HTTP methods
│   │   │   ├── endpoints/
│   │   │   │   ├── UserEndpoint.java          # User API endpoint mapper
│   │   │   │   └── LoginEndpoint.java         # Auth endpoint mapper
│   │   │   ├── models/
│   │   │   │   └── User.java                  # POJO for request/response
│   │   │   ├── tests/
│   │   │   │   ├── UserAPITest.java           # User CRUD test cases
│   │   │   │   └── AuthAPITest.java           # Auth test cases
│   │   │   ├── genai/
│   │   │   │   └── TestCaseGenerator.java     # Claude API integration
│   │   │   └── utils/
│   │   │       └── ReportingUtils.java        # Extent Reports helper
│   │   └── resources/
│   │       ├── config.properties              # Global configuration
│   │       └── testng.xml                     # Test suite configuration
├── docs/
│   ├── API_TESTING_WITH_GENAI.md              # GenAI integration guide
│   └── TEST_EXECUTION_GUIDE.md                # How to run tests
├── pom.xml                                     # Maven dependencies
└── README.md
```

---

## 🔧 Core Components

### 1. **BaseTest.java** - Common Setup & HTTP Methods

Provides reusable methods for all API interactions:

```java
// Example usage:
Response response = getRequest("/users/2");
Response response = postRequest("/users", requestBody);
Response response = putRequest("/users/2", updatedBody);
Response response = deleteRequest("/users/2");
```

**Benefits:**
- Consistent request formatting
- Automatic header injection
- Base URI & path management
- Centralized logging

### 2. **Endpoint Object Model**

Similar to POM but for API endpoints:

```java
// UserEndpoint.java
public class UserEndpoint extends BaseTest {
    private static final String USERS = "/users";
    
    public Response getAllUsers() {
        return getRequest(USERS);
    }
    
    public Response getUserById(int id) {
        return getRequest(USERS + "/" + id);
    }
}
```

**Why:** Encapsulates endpoint URLs, reducing duplication and improving maintainability.

### 3. **GenAI Test Case Generator**

Uses Claude API to automatically generate intelligent test scenarios:

```java
TestCaseGenerator generator = new TestCaseGenerator();

// Generate test cases for an endpoint
String testCases = generator.generateTestCases(
    "/users",
    "POST",
    "{\"name\": \"string\", \"email\": \"string\"}"
);

// Generate edge cases for field validation
String edgeCases = generator.generateEdgeCases("email", "email");
```

**Capabilities:**
- ✅ Happy path test generation
- ✅ Edge case identification
- ✅ Error scenario suggestions
- ✅ Validation assertion recommendations
- ✅ Performance test case suggestions

### 4. **Test Organization**

Tests follow the **AAA Pattern** (Arrange-Act-Assert):

```java
@Test
public void testGetAllUsers() {
    // Arrange
    String endpoint = "/users";
    
    // Act
    Response response = getRequest(endpoint);
    
    // Assert
    response.then()
        .statusCode(200)
        .body("data", notNullValue());
}
```

---

## 🤖 GenAI Integration - How It Works

### Step 1: Manual Test Case Definition

Write your test case using RestAssured:

```java
@Test
public void testCreateUser() {
    String body = "{\"name\": \"John\", \"job\": \"QA\"}";
    Response response = postRequest("/users", body);
    response.then().statusCode(201);
}
```

### Step 2: GenAI Enhancement

Use TestCaseGenerator to identify missing edge cases:

```java
String edgeCases = generator.generateEdgeCases("name", "string");
// Output: Suggestions for null, empty, special chars, max length, etc.
```

### Step 3: Implement Suggested Cases

Add the additional test cases based on GenAI recommendations.

### Step 4: Analyze Response Validation

Let GenAI suggest optimal assertions:

```java
String suggestions = generator.analyzeResponseAndSuggestAssertions(
    "{\"id\": 1, \"name\": \"John\", \"createdAt\": \"2026-06-22\"}"
);
// Output: Recommended Hamcrest matchers & validation points
```

---

## 📊 Test Execution Flow

```
1. TestNG reads testng.xml
2. BaseTest.setup() initializes RequestSpecification
3. Each @Test method executes API calls
4. Extent Reports captures results
5. HTML report generated in target/ExtentReports/
```

---

## 🔌 Configuration Management

**config.properties:**

```properties
# Base Configuration
base.uri=https://reqres.in
base.path=/api
content.type=application/json

# GenAI Configuration
anthropic.api.key=${ANTHROPIC_API_KEY}
genai.model=claude-opus-4-1

# Reporting
report.path=target/ExtentReports/
report.title=API Test Automation Report
```

---

## 🚀 Best Practices Implemented

1. **Separation of Concerns**: Endpoints, tests, utilities, GenAI logic are seperated
2. **DRY Principle**: Reusable request/response methods in BaseTest
3. **Data-Driven Tests**: Support for parameterized tests via TestNG
4. **Meaningful Assertions**: Using Hamcrest matchers for readability
5. **Comprehensive Logging**: All requests/responses logged for debugging
6. **GenAI-Augmented Coverage**: Automated edge case & scenario discovery

---

## 🧪 Running Tests

### Run All Tests:
```bash
mvn clean test
```

### Run Specific Test Class:
```bash
mvn clean test -Dtest=UserAPITest
```

### Run Specific Test Method:
```bash
mvn clean test -Dtest=UserAPITest#testGetAllUsers
```

### Generate GenAI Test Suggestions:
```bash
mvn exec:java -Dexec.mainClass="com.qa.api.genai.TestCaseGenerator"
```

---

## 📈 Future Enhancements

- [ ] API Contract Testing (Pact)
- [ ] Performance Baseline Comparisons
- [ ] Automated Test Data Generation
- [ ] GraphQL API Support
- [ ] WebSocket Testing
- [ ] OAuth2 Flow Automation

---

## 📞 Support

For framework enhancements or issues, refer to the [GitHub Issues](https://github.com/Sidpng/QA-RestAssured-API-Framework/issues) section.

---

**Last Reviewed:** June 22, 2026  
**Maintained By:** Siddhartha Upadhyay
