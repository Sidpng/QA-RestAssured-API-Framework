package com.qa.api.genai;

import com.anthropic.client.Anthropic;
import com.anthropic.models.Message;
import com.anthropic.models.MessageCreateParams;

/**
 * GenAI-Powered Test Case Generator
 * Uses Claude API to intelligently generate test cases for API endpoints
 *
 * HOW TO USE:
 * 1. Set ANTHROPIC_API_KEY environment variable with your Claude API key
 * 2. Call generateTestCases() with API specification
 * 3. Review and adapt generated test scenarios
 */
public class TestCaseGenerator {

    private final Anthropic client;

    public TestCaseGenerator() {
        // Initialize Anthropic client (reads ANTHROPIC_API_KEY from env)
        this.client = Anthropic.builder().build();
    }

    /**
     * Generate test scenarios for a given API endpoint
     * @param apiEndpoint The API endpoint (e.g., /users)
     * @param httpMethod HTTP method (GET, POST, PUT, DELETE)
     * @param apiSchema Expected request/response schema
     * @return Generated test scenarios
     */
    public String generateTestCases(String apiEndpoint, String httpMethod, String apiSchema) {
        String prompt = String.format("""
            You are an expert API QA engineer. Generate comprehensive test cases for the following API endpoint.

            Endpoint: %s
            HTTP Method: %s
            Schema: %s

            Generate test cases covering:
            1. Happy path scenarios
            2. Edge cases
            3. Error scenarios (4xx, 5xx responses)
            4. Validation errors
            5. Performance/load considerations

            Format the output as a numbered list of test cases with:
            - Test Case Name
            - Description
            - Input Data
            - Expected Result
            - Assertion Points
            """, apiEndpoint, httpMethod, apiSchema);

        Message message = client.messages().create(MessageCreateParams.builder()
                .model("claude-opus-4-1")
                .maxTokens(2048)
                .messages(java.util.List.of(
                        new MessageCreateParams.MessageParam.UserMessageParam(prompt)
                ))
                .build());

        return message.content().getFirst().text();
    }

    /**
     * Generate edge case scenarios for API validation
     * @param fieldName Name of the field to test
     * @param fieldType Data type (string, integer, email, etc.)
     * @return Edge case test data suggestions
     */
    public String generateEdgeCases(String fieldName, String fieldType) {
        String prompt = String.format("""
            Generate edge case test data for API field validation.

            Field Name: %s
            Field Type: %s

            Provide:
            1. Boundary values
            2. Invalid formats
            3. Special characters
            4. Null/empty scenarios
            5. Max/min length scenarios

            Format as: [Value] | [Expected Behavior]
            """, fieldName, fieldType);

        Message message = client.messages().create(MessageCreateParams.builder()
                .model("claude-opus-4-1")
                .maxTokens(1024)
                .messages(java.util.List.of(
                        new MessageCreateParams.MessageParam.UserMessageParam(prompt)
                ))
                .build());

        return message.content().getFirst().text();
    }

    /**
     * Analyze API response and suggest validation assertions
     * @param actualResponse API response JSON
     * @return Suggested assertions based on response structure
     */
    public String analyzeResponseAndSuggestAssertions(String actualResponse) {
        String prompt = String.format("""
            Analyze the following API response and suggest key validation assertions for automated testing.

            Response:
            %s

            Provide:
            1. Critical fields to validate
            2. Data type checks
            3. Value range validations
            4. Dependency validations
            5. Hamcrest matcher suggestions

            Format as: assertion | reasoning
            """, actualResponse);

        Message message = client.messages().create(MessageCreateParams.builder()
                .model("claude-opus-4-1")
                .maxTokens(1536)
                .messages(java.util.List.of(
                        new MessageCreateParams.MessageParam.UserMessageParam(prompt)
                ))
                .build());

        return message.content().getFirst().text();
    }

    public static void main(String[] args) {
        TestCaseGenerator generator = new TestCaseGenerator();

        // Example: Generate test cases for /users endpoint
        String testCases = generator.generateTestCases(
                "/users",
                "POST",
                "{\"name\": \"string\", \"email\": \"string\", \"job\": \"string\"}"
        );

        System.out.println("=== Generated Test Cases ===");
        System.out.println(testCases);

        // Example: Generate edge cases for email field
        String edgeCases = generator.generateEdgeCases("email", "email");
        System.out.println("\n=== Edge Cases for Email Field ===");
        System.out.println(edgeCases);
    }
}
