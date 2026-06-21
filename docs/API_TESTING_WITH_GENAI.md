# API Testing with GenAI - Complete Guide

**Version:** 1.0  
**Author:** Siddhartha Upadhyay  
**Date:** June 2026

---

## 🎯 What is GenAI-Augmented API Testing?

Traditional API testing focuses on **predetermined test cases**. GenAI-Augmented testing uses **Claude AI** to:

✅ **Intelligently generate test scenarios** you might have missed  
✅ **Identify edge cases** automatically based on schema analysis  
✅ **Suggest optimal assertions** for validation  
✅ **Analyze API responses** and recommend coverage improvements  
✅ **Generate test data** for complex payloads  

---

## 🚀 Getting Started

### 1. Set Up Claude API Access

```bash
# Export your API key (Linux/Mac)
export ANTHROPIC_API_KEY="sk-ant-..."

# Or for Windows PowerShell:
$env:ANTHROPIC_API_KEY = "sk-ant-..."
```

### 2. Add Anthropic SDK to pom.xml

Already included in this framework:

```xml
<dependency>
    <groupId>com.anthropic</groupId>
    <artifactId>anthropic-sdk-java</artifactId>
    <version>0.2.0</version>
</dependency>
```

### 3. Run Test Case Generator

```bash
mvn exec:java -Dexec.mainClass="com.qa.api.genai.TestCaseGenerator"
```

---

## 📚 Usage Examples

### Example 1: Generate Test Cases for POST /users

**Prompt:**
```
Generate test cases for POST /users with payload: {"name": "string", "email": "string"}
```

**GenAI Output:**
```
1. Happy Path - Valid User Creation
   - Input: {"name": "John Doe", "email": "john@example.com"}
   - Expected: 201 Created with user ID

2. Edge Case - Empty Name
   - Input: {"name": "", "email": "john@example.com"}
   - Expected: 400 Bad Request with validation error

3. Edge Case - Invalid Email Format
   - Input: {"name": "John Doe", "email": "not-an-email"}
   - Expected: 400 Bad Request

4. Boundary - Max Name Length (255 chars)
   - Input: {"name": "A" * 255, "email": "john@example.com"}
   - Expected: 201 or 400 depending on API spec

5. Performance - Bulk User Creation (async scenario)
   - Input: Multiple concurrent POST requests
   - Expected: All succeed within acceptable latency
```

---

### Example 2: Generate Edge Cases for Email Field

**Code:**
```java
TestCaseGenerator generator = new TestCaseGenerator();
String edgeCases = generator.generateEdgeCases("email", "email");
System.out.println(edgeCases);
```

**Output:**
```
Valid Cases:
✓ john@example.com
✓ john.doe+tag@example.co.uk
✓ user123@subdomain.example.com

Invalid Cases (Expected 400):
✗ john@example (no TLD)
✗ @example.com (no local part)
✗ john @example.com (space in local part)
✗ john@exam ple.com (space in domain)

Boundary Cases:
• Very long email (255+ chars) - should either validate or reject
• Special chars: john+tag@example.com (RFC 5322 compliant)
• International domain: user@example.中国
```

---

### Example 3: Analyze Response and Get Assertion Recommendations

**Code:**
```java
String response = """
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "createdAt": "2026-06-22T10:30:00Z",
  "role": "user",
  "active": true
}
""";

String assertions = generator.analyzeResponseAndSuggestAssertions(response);
System.out.println(assertions);
```

**Output:**
```
Critical Assertions:
1. Status Code: .statusCode(201 or 200)
2. ID Presence: .body("id", notNullValue())
3. Email Format: .body("email", matchesPattern(emailRegex))
4. CreatedAt Format: .body("createdAt", matchesPattern(isoDateTimeRegex))
5. Active Status: .body("active", is(true or false))

Nested Validations:
- Verify all required fields are present
- Check timestamp is within last 5 minutes
- Validate role enum (user/admin/moderator)

Performance Checks:
- Response time < 200ms
- Response size < 10KB
```

---

## 🎓 Workflow Example: Complete Test Development

### Scenario: Testing a User Management API

#### Step 1: Define API Specification

```
Endpoint: POST /users
Method: POST
Input Schema:
{
  "name": "string (required, 1-255 chars)",
  "email": "string (required, valid email format)",
  "phone": "string (optional, 10-15 digits)",
  "role": "enum[user, admin, moderator]"
}

Output Schema:
{
  "id": "integer (auto-generated)",
  "name": "string",
  "email": "string",
  "phone": "string or null",
  "role": "string",
  "createdAt": "ISO8601 datetime",
  "updatedAt": "ISO8601 datetime"
}
```

#### Step 2: Use GenAI to Generate Initial Test Cases

```java
TestCaseGenerator generator = new TestCaseGenerator();

String testCases = generator.generateTestCases(
    "/users",
    "POST",
    "{\"name\": \"string\", \"email\": \"string\", \"phone\": \"string\", \"role\": \"enum\"}"
);

System.out.println("=== Generated Test Scenarios ===");
System.out.println(testCases);
```

#### Step 3: Implement Generated Test Cases

Based on GenAI output, create test class:

```java
@Test(priority = 1)
public void testCreateUserWithValidData() {
    String body = "{\"name\": \"John Doe\", \"email\": \"john@example.com\", \"role\": \"user\"}";
    Response response = postRequest("/users", body);
    
    response.then()
        .statusCode(201)
        .body("id", notNullValue())
        .body("email", equalTo("john@example.com"))
        .body("role", equalTo("user"));
}

@Test(priority = 2)
public void testCreateUserWithMissingEmail() {
    String body = "{\"name\": \"John Doe\"}";
    Response response = postRequest("/users", body);
    
    response.then()
        .statusCode(400)
        .body("error", containsString("email is required"));
}

@Test(priority = 3)
public void testCreateUserWithInvalidEmail() {
    String body = "{\"name\": \"John Doe\", \"email\": \"not-an-email\"}";
    Response response = postRequest("/users", body);
    
    response.then()
        .statusCode(400)
        .body("error", containsString("invalid email"));
}
```

#### Step 4: Refine Assertions with GenAI

```java
// Get first response from test
Response response = postRequest("/users", validPayload);
String responseBody = response.getBody().asString();

// Ask GenAI for optimization
String improvements = generator.analyzeResponseAndSuggestAssertions(responseBody);
System.out.println(improvements);

// Enhance assertions based on recommendations
response.then()
    .statusCode(201)
    .body("id", notNullValue())
    .body("createdAt", matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z"))
    .body("role", isOneOf("user", "admin", "moderator"));
```

---

## 💡 Pro Tips for GenAI Integration

### Tip 1: Use Structured Prompts

**Good:**
```
Generate boundary test cases for a 'name' field that accepts 1-255 characters.
Include: empty string, single char, max length, max+1 length.
Format: [Input] | [Expected Result]
```

**Bad:**
```
Test cases for name field
```

### Tip 2: Iterative Refinement

```java
// First pass: Get basic scenarios
String scenarios = generator.generateTestCases(endpoint, method, schema);

// Second pass: Get edge cases for critical fields
String edges = generator.generateEdgeCases("email", "email");

// Third pass: Optimize assertions
String assertions = generator.analyzeResponseAndSuggestAssertions(sampleResponse);
```

### Tip 3: Document Why Tests Exist

```java
@Test(description = "GenAI identified: empty email bypasses validation in v1.0")
public void testCreateUserWithEmptyEmail() {
    // This test was suggested by GenAI analysis of schema validation rules
    // ...
}
```

### Tip 4: Balance GenAI Output with Domain Knowledge

GenAI is **smart but not perfect**:
- ✅ Use for edge case discovery
- ✅ Use for schema analysis suggestions
- ✅ Use for assertion recommendations
- ❌ Don't blindly copy all suggestions
- ❌ Verify generated scenarios against actual API behavior

---

## 🔍 Troubleshooting

### Issue: GenAI API Key Not Found

```bash
# Verify it's set
echo $ANTHROPIC_API_KEY  # Linux/Mac
echo $env:ANTHROPIC_API_KEY  # PowerShell

# Re-export if needed
export ANTHROPIC_API_KEY="sk-ant-..."
```

### Issue: Slow Test Generation

- Claude API calls take 2-5 seconds per request
- Cache results in files for reuse
- Use `--offline` mode with pre-generated scenarios

### Issue: Generated Cases Don't Match API

- API spec might differ from schema provided
- Always test generated cases manually first
- Report discrepencies and refine the prompt

---

## 📋 GenAI Test Case Template

Use this template for consistent test generation:

```markdown
## Test Scenario: [Name]
**Generated By:** Claude API | TestCaseGenerator  
**Purpose:** [What this test validates]

### Input Data:
```json
{payload}
```

### Expected Behavior:
- Status Code: [Expected HTTP status]
- Response Body: [Expected structure]
- Side Effects: [Any backend changes]

### Assertions:
```java
response.then()
    .statusCode(EXPECTED_CODE)
    .body("field", matcher)
    // ... additional assertions
```

### Why This Case Matters:
[Explanation of why GenAI suggested this scenario]
```

---

## 🎯 Key Takeaways

1. **GenAI augments, not replaces** manual testing expertise
2. **Structured prompts** yield better results
3. **Iterative refinement** improves test coverage
4. **Domain knowledge** should validate AI suggestions
5. **Document the "why"** for future maintainability

---

## 📞 Next Steps

- ✅ Set up ANTHROPIC_API_KEY
- ✅ Run TestCaseGenerator
- ✅ Review generated scenarios
- ✅ Implement 2-3 suggested edge cases
- ✅ Compare coverage before/after

Happy testing! 🚀

---

**Last Updated:** June 22, 2026  
**Maintained By:** Siddhartha Upadhyay
